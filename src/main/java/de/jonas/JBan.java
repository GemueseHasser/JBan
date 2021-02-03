package de.jonas;

import de.jonas.jban.commands.Ban;
import de.jonas.jban.commands.Kick;
import de.jonas.jban.commands.Mute;
import de.jonas.jban.commands.TempBan;
import de.jonas.jban.commands.Unban;
import de.jonas.jban.commands.Unmute;
import de.jonas.jban.commands.Warn;
import de.jonas.jban.listener.OnChat;
import de.jonas.jban.listener.OnLogin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Die Haupt- und Main-Klasse des Plugins, worin alle Commands und Listener registriert werden. Zudem wird das Plugin
 * mit dieser Klasse initialisiert.
 */
public class JBan extends JavaPlugin {

    //<editor-fold desc="CONSTANTS">
    /** Der Chat-Prefix, der vor allen Chat-nachrichten angezeigt wird, die von diesem Plugin versendet werden. */
    public static final String PREFIX = ChatColor.WHITE.toString() + ChatColor.BOLD + "["
        + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Ban"
        + ChatColor.WHITE.toString() + ChatColor.BOLD + "] " + ChatColor.GRAY;
    /** Die {@link ConsoleCommandSender Konsole}, der die Initialisierungs-Nachrichten gesendet werden. */
    private static final ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();
    //</editor-fold>


    //<editor-fold desc="STATIC-FIELDS">
    /** Die Instanz, mit der auf das Plugin zugegriffen werden kann. */
    @Getter
    private static JBan instance;
    //</editor-fold>


    //<editor-fold desc="setup and shutdown">
    @Override
    public void onEnable() {
        // declare Plugin-Instance
        instance = this;

        // send activate-message
        CONSOLE.sendMessage(PREFIX + "Das Plugin wurde erfolgreich aktiviert! by Jonas0206 / Gemuese_Hasser");

        // register commands
        Objects.requireNonNull(getCommand("ban")).setExecutor(new Ban());
        Objects.requireNonNull(getCommand("kick")).setExecutor(new Kick());
        Objects.requireNonNull(getCommand("mute")).setExecutor(new Mute());
        Objects.requireNonNull(getCommand("warn")).setExecutor(new Warn());
        Objects.requireNonNull(getCommand("unban")).setExecutor(new Unban());
        Objects.requireNonNull(getCommand("unmute")).setExecutor(new Unmute());
        Objects.requireNonNull(getCommand("tempban")).setExecutor(new TempBan());

        // register listener
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnLogin(), this);
        pm.registerEvents(new OnChat(), this);
    }

    @Override
    public void onDisable() {
        CONSOLE.sendMessage(PREFIX + "Das Plugin wurde deaktiviert!");
    }
    //</editor-fold>

}
