package me.waterarchery.litlibs.logger;

import org.bukkit.Bukkit;

public class Logger {

    private final String name;
    private boolean debugMessages;

    public Logger(String name, boolean debugMessages) {
        this.name = name;
        this.debugMessages = debugMessages;
    }

    public void log(String mes, LogSeverity severity) {
        if (severity == LogSeverity.NORMAL)
            log(mes);
        else if (severity == LogSeverity.WARN)
            warn(mes);
        else if (severity == LogSeverity.ERROR)
            error(mes);
        else if (severity == LogSeverity.DEBUG)
            debug(mes);
    }

    public void log(String mes) {
        mes = mes.replace("&", "§");
        Bukkit.getConsoleSender().sendMessage("§7[§b" + name + "§7] §f" + mes);
    }

    public void debug(String mes) {
        if (debugMessages) {
            mes = mes.replace("&", "§");
            Bukkit.getConsoleSender().sendMessage("§7[§b" + name + "§7] §a[DEBUG] §f" + mes);
        }
    }

    public void error(String mes) {
        mes = mes.replace("&", "§");
        Bukkit.getConsoleSender().sendMessage("§7[§b" + name + "§7] §4[ERROR] §f" + mes);
    }

    public void warn(String mes) {
        mes = mes.replace("&", "§");
        Bukkit.getConsoleSender().sendMessage("§7[§b" + name + "§7] §e[WARN] §f" + mes);
    }

    public void setDebugMessages(boolean debugMessages) {
        this.debugMessages = debugMessages;
    }

    public static void logMessage(String mes) {
        mes = mes.replace("&", "§");
        Bukkit.getConsoleSender().sendMessage("§7[§bLitLibs§7] §f" + mes);
    }

}
