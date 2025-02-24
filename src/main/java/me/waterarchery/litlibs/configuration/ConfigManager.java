package me.waterarchery.litlibs.configuration;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.logger.LogSeverity;
import me.waterarchery.litlibs.utils.ChatUtils;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfigManager {

    private final LitLibs litLibs;
    private FileConfiguration yml;
    private File file;
    private final String name;
    private boolean createdNow;
    private final boolean saveAfterLoad;

    public ConfigManager(LitLibs litLibs, String folder, String name, boolean saveAfterLoad, boolean useHeaders) {
        this.litLibs = litLibs;
        this.saveAfterLoad = saveAfterLoad;
        this.name = name;

        initializer(litLibs, folder, name, saveAfterLoad, useHeaders);
    }

    public ConfigManager(LitLibs litLibs, String folder, String name, boolean saveAfterLoad) {
        this.litLibs = litLibs;
        this.saveAfterLoad = saveAfterLoad;
        this.name = name;

        initializer(litLibs, folder, name, saveAfterLoad, true);
    }

    private void initializer(LitLibs litLibs, String folder, String name, boolean saveAfterLoad, boolean useHeaders) {
        Plugin provider = litLibs.getPlugin();;
        File f = new File(provider.getDataFolder().getPath() + "/" + folder);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                litLibs.getLogger().log("Can't create folder: " + f.getPath(), LogSeverity.ERROR);
                return;
            }
        }

        file = new File(f, name + ".yml");
        if (!file.exists()) {
            String finalPath = f.getPath() + "\\" + name + ".yml";
            finalPath = finalPath.replace("plugins\\", "");
            finalPath = finalPath.replace(provider.getName() + "\\", "");
            litLibs.getLogger().log("Creating file: " + file.getPath(), LogSeverity.NORMAL);
            if (provider.getResource(finalPath) != null) {
                provider.saveResource(finalPath, false);
            }
            else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    litLibs.getLogger().log("Can't create file: " + file.getPath(), LogSeverity.ERROR);
                }
            }
            createdNow = true;
        }

        yml = YamlConfiguration.loadConfiguration(file);
        reload();

        setHeader(useHeaders);

        getYml().options().copyDefaults(true);
        initializeDefaults();

        if (saveAfterLoad) {
            save(false);
        }
    }

    public void initializeDefaults() {

    }

    public void addDefault(ConfigPart configPart) {
        String path = configPart.getPath();
        Object value = configPart.getValue();
        List<String> comments = configPart.getComments();

        if (value != null)
            yml.addDefault(path, value);

        if (VersionHandler.getInstance().isServerNewerThan(Version.v1_18) && comments != null && !comments.isEmpty())
            yml.setComments(path, comments);
    }

    public void addOptional(ConfigPart configPart) {
        if (isCreatedNow()) {
            addDefault(configPart);
        }
    }

    public void setHeader(boolean pluginInfo) {
        Plugin plugin = litLibs.getPlugin();
        PluginDescriptionFile desc = plugin.getDescription();

        if (pluginInfo) {
            getYml().options().header(
                    desc.getName() + " by " + desc.getAuthors().get(0) + "\n" +
                            "Wiki: https://waterarchery.gitbook.io/" + plugin.getName().toLowerCase() + "-wiki/\n" +
                            "Spigot: https://www.spigotmc.org/members/waterarchery.963492/\n" +
                            "Builtbybit: https://builtbybit.com/members/waterarchery.164059/\n" +
                            "\nFile Name: " + name + "\n" +
                            "Plugin Version: " + desc.getVersion() + "\n"
            );
        }
        else {
            getYml().options().header(
                    desc.getName() + " by " + desc.getAuthors().get(0) + "\n" +
                            "\nFile Name: " + name + "\n" +
                            "Plugin Version: " + desc.getVersion() + "\n"
            );
        }
    }

    public String getString(String path) {
        String str = yml.getString(path, "Error in: " + path);
        return ChatUtils.colorizeLegacy(str);
    }

    public String getString(String path, String defaultValue) {
        String str = yml.getString(path, defaultValue);
        return ChatUtils.colorizeLegacy(str);
    }

    public void save(boolean async) {
        if (async)
            CompletableFuture.runAsync(saveRunnable()).thenAccept((a) -> {
                reload();
            });
        else
            try {
                if (VersionHandler.getInstance().isServerNewerThan(Version.v1_18))
                    yml.options().parseComments(true);
                yml.save(file);
            } catch (IOException e) {
                litLibs.getLogger().log("Error while saving file: " + file.getPath(), LogSeverity.ERROR);
            }
    }

    public void reload() {
        yml = YamlConfiguration.loadConfiguration(file);
    }

    private Runnable saveRunnable() {
        return () -> {
            try {
                yml.options().parseComments(true);
                yml.save(file);
            } catch (IOException e) {
                litLibs.getLogger().log("Error while saving file: " + file.getPath(), LogSeverity.ERROR);
            }
        };
    }

    public boolean isCreatedNow() {
        return createdNow;
    }

    public FileConfiguration getYml() {
        return yml;
    }

    public File getFile() {
        return file;
    }


    public String getName() {
        return name;
    }

    public boolean isSaveAfterLoad() { return saveAfterLoad; }

}
