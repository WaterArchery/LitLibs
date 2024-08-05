package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.hooks.HologramHook;
import me.waterarchery.litlibs.hooks.PriceHook;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import me.waterarchery.litlibs.hooks.hologram.CMIHook;
import me.waterarchery.litlibs.hooks.hologram.DecentHologramsHook;
import me.waterarchery.litlibs.hooks.hologram.HolographicDisplaysHook;
import me.waterarchery.litlibs.hooks.other.PlaceholderHook;
import me.waterarchery.litlibs.hooks.price.AxGensPriceHook;
import me.waterarchery.litlibs.hooks.price.EconomyShopGUI;
import me.waterarchery.litlibs.hooks.price.EssentialsPriceHook;
import me.waterarchery.litlibs.hooks.price.ShopGUIPriceHook;
import me.waterarchery.litlibs.hooks.protection.BentoBoxHook;
import me.waterarchery.litlibs.hooks.protection.SuperiorHook;
import me.waterarchery.litlibs.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class HookHandler {

    private ProtectionHook protectionHook;
    private PriceHook priceHook;
    private HologramHook hologramHook;
    private final Logger logger;
    private final LitLibs litLibs;

    public HookHandler(LitLibs litLibs) {
        this.litLibs = litLibs;
        logger = litLibs.getLogger();
        chooseHologramHook();
        chooseIslandHook();
        registerEconomyHooks();
        registerOtherHooks();

    }

    public void chooseHologramHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")) {
            hologramHook = DecentHologramsHook.getInstance();
            logger.log("Selected hologram hook: DecentHolograms");
        }
        else if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
            hologramHook = CMIHook.getInstance();
            logger.log("Selected hologram hook: CMI");
        }
        else if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            hologramHook = HolographicDisplaysHook.getInstance();
            logger.log("Selected hologram hook: HolographicDisplays");
        }
        else {
            logger.warn("No hologram hook has been found! The plugins that require holograms may not work!");
        }
    }

    public void registerEconomyHooks() {
        Plugin provider = litLibs.getPlugin();
        String defaultPrice = provider.getConfig().getString("PriceHook", "Essentials");
        if (defaultPrice.equalsIgnoreCase("essentials") || defaultPrice.equalsIgnoreCase("essentialsx")) {
            if (Bukkit.getPluginManager().isPluginEnabled("EssentialsX") || Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                priceHook = EssentialsPriceHook.getInstance();
                logger.log("Found price hook: Essentials");
            }
        }
        else if (defaultPrice.equalsIgnoreCase("economyshopgui") || defaultPrice.equalsIgnoreCase("ECONOMYSHOPGUI")) {
            if (Bukkit.getPluginManager().isPluginEnabled("EconomyShopGUI") || Bukkit.getPluginManager().isPluginEnabled("EconomyShopGUI-Premium")) {
                priceHook = EconomyShopGUI.getInstance();
                logger.log("Found price hook: EconomyShopGUI");
            }
        }
        else if (defaultPrice.equalsIgnoreCase("shopguiplus") || defaultPrice.equalsIgnoreCase("shopgui")
                || defaultPrice.equalsIgnoreCase("shopgui+")) {
            if (Bukkit.getPluginManager().isPluginEnabled("ShopGUIPlus")) {
                priceHook = ShopGUIPriceHook.getInstance();
                logger.log("Found price hook: ShopGUIPlus");
            }
        }
        else if (defaultPrice.equalsIgnoreCase("axgens") || defaultPrice.equalsIgnoreCase("axgen")) {
            if (Bukkit.getPluginManager().isPluginEnabled("AxGens")) {
                priceHook = AxGensPriceHook.getInstance();
                logger.log("Found price hook: AxGens");
            }
        }
        if (priceHook == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("EssentialsX") || Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                priceHook = EssentialsPriceHook.getInstance();
                logger.log("Found price hook: Essentials");
            }
            else if (Bukkit.getPluginManager().isPluginEnabled("EconomyShopGUI") || Bukkit.getPluginManager().isPluginEnabled("EconomyShopGUI-Premium")) {
                priceHook = EconomyShopGUI.getInstance();
                logger.log("Found price hook: EconomyShopGUI");
            }
            else if (Bukkit.getPluginManager().isPluginEnabled("ShopGUIPlus")) {
                priceHook = ShopGUIPriceHook.getInstance();
                logger.log("Found price hook: ShopGUIPlus");
            }
            else if (Bukkit.getPluginManager().isPluginEnabled("AxGens")) {
                priceHook = AxGensPriceHook.getInstance();
                logger.log("Found price hook: AxGens");
            }
        }
    }

    public void chooseIslandHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) {
            logger.log("Selected island hook: SuperiorSkyblock2");
            protectionHook = SuperiorHook.getInstance();
        }
        else if (Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
            logger.log("Selected island hook: BentoBox");
            protectionHook = BentoBoxHook.getInstance();
        }
        else {
            logger.log("No protection hook has been found.");
        }
    }

    public void registerOtherHooks() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            logger.log("Registered PlaceholderAPI hook!");
            PlaceholderHook.setIsEnabled(true);
        }
    }

    public HologramHook getHologramHook() { return hologramHook; }
    public ProtectionHook getProtectionHook() { return protectionHook; }
    public PriceHook getPriceHook() { return priceHook; }

}
