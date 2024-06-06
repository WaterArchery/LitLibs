package me.waterarchery.litlibs.hooks.other;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.inventory.Action;
import me.waterarchery.litlibs.inventory.ActionType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class NBTAPIHook {

    private final LitLibs litLibs;

    public NBTAPIHook(LitLibs litLibs) { this.litLibs = litLibs; }

    public ItemStack setGUIAction(String action, ItemStack item, String guiName, ActionType actionType){
        NBTItem nbti = new NBTItem(item);
        Plugin provider = litLibs.getPlugin();

        nbti.setBoolean(provider.getName(), true);
        nbti.setString("GUIName", guiName);
        if (actionType == ActionType.PLUGIN)
            nbti.setString("GUIAction", action);
        else
            nbti.setString("GUICommand", action);
        return nbti.getItem();
    }

    public @Nullable Action getGUIAction(ItemStack item){
        NBTItem nbti = new NBTItem(item);
        Plugin provider = litLibs.getPlugin();

        if (nbti.hasTag(provider.getName())) {
            Action actionModel;
            String guiName = nbti.getString("GUIName");
            if (nbti.hasTag("GUIAction")) {
                String action = nbti.getString("GUIAction");
                actionModel = new Action(action, guiName, ActionType.PLUGIN);
            }
            else {
                String action = nbti.getString("GUICommand");
                actionModel = new Action(action, guiName, ActionType.COMMAND);
            }
            return actionModel;
        }
        else {
            return null;
        }
    }

    public ItemStack setNBT(ItemStack itemStack, String key, String value) {
        NBTItem nbti = new NBTItem(itemStack);

        nbti.setString(key, value);
        return nbti.getItem();
    }

    public @Nullable String getNBT(ItemStack itemStack, String key) {
        NBTItem nbti = new NBTItem(itemStack);

        if (nbti.hasTag(key))
            return nbti.getString(key);

        return null;
    }

}
