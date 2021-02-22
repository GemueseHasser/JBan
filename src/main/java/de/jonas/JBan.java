package de.jonas;

import de.jonas.jban.commands.Ban;
import de.jonas.jban.commands.Kick;
import de.jonas.jban.commands.Mute;
import de.jonas.jban.commands.TempBan;
import de.jonas.jban.commands.TempMute;
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
import org.jetbrains.annotations.NotNull;

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
    private static final String LANGUAGE_ERROR = "Please choose a correct language in the config (de or en)";
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

        // load config
        loadConfig();

        // register commands
        Objects.requireNonNull(getCommand("ban")).setExecutor(new Ban());
        Objects.requireNonNull(getCommand("kick")).setExecutor(new Kick());
        Objects.requireNonNull(getCommand("mute")).setExecutor(new Mute());
        Objects.requireNonNull(getCommand("warn")).setExecutor(new Warn());
        Objects.requireNonNull(getCommand("unban")).setExecutor(new Unban());
        Objects.requireNonNull(getCommand("unmute")).setExecutor(new Unmute());
        Objects.requireNonNull(getCommand("tempban")).setExecutor(new TempBan());
        Objects.requireNonNull(getCommand("tempmute")).setExecutor(new TempMute());

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

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    //<editor-fold desc="message-api">
    /**
     * Die Nachricht, die gesendet wird, wenn ein Spieler keine Rechte hat, um eine bestimmte Aktion auszuführen.
     *
     * @return Die Nachricht, die dem Aktions-Sender bescheid gibt, dass er keine Rechte dazu hat.
     */
    public String getNoPermsMessage() {
        if (isGermanSet()) {
            return "Dazu hast du keine Rechte!";
        }
        if (isEnglishSet()) {
            return "You are not allowed to do this!";
        }
        return LANGUAGE_ERROR;
    }

    /**
     * Die Nachricht, die gesendet wird, wenn ein Spieler versucht etwas auszuführen, worin ein anderer Spieler
     * einbezogen ist, der nicht online ist.
     *
     * @return Die Nachricht, die dem Aktions-Sender bescheid gibt, dass der Spieler, der in seiner Aktion vorkommt
     *     nicht online ist.
     */
    public String getPlayerIsNotOnlineMessage() {
        if (isGermanSet()) {
            return "Der Spieler ist nicht online!";
        }
        if (isEnglishSet()) {
            return "The player is not online!";
        }
        return LANGUAGE_ERROR;
    }

    /**
     * Wählt aus einer deutschen und einer englischen Nachricht, je nach Config-Einstellung, die richtige Nachricht
     * aus.
     *
     * @param german  Die Deutsche Nachricht.
     * @param english Die Englische Nachricht.
     *
     * @return Die korrekt ausgewählte Nachricht, anhand der Config-Einstellung.
     */
    public String chooseCorrectLanguage(@NotNull final String german, @NotNull final String english) {
        if (isGermanSet()) {
            return german;
        }
        if (isEnglishSet()) {
            return english;
        }
        return LANGUAGE_ERROR;
    }

    /**
     * Es wird überprüft, ob in der Config die Deutsche Sprache eingestellt ist.
     *
     * @return Ist die Deutsche Sprache eingestellt.
     */
    private boolean isGermanSet() {
        return Objects.requireNonNull(getConfig().getString("Config.Language")).equalsIgnoreCase("de");
    }

    /**
     * Es wird überprüft, ob in der Config die Englische Sprache eingestellt ist.
     *
     * @return Ist die Englische Sprache eingestellt.
     */
    private boolean isEnglishSet() {
        return Objects.requireNonNull(getConfig().getString("Config.Language")).equalsIgnoreCase("en");
    }
    //</editor-fold>

}
