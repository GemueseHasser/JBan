package de.jonas.jban.commands;

import de.jonas.JBan;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static de.jonas.JBan.PREFIX;

/**
 * Implementiert den Command, mit dem {@link Player Spieler} permanent gemuted werden.
 */
public class Mute implements CommandExecutor {

    //<editor-fold desc="CONSTANTS">
    /**
     * Die Anzahl an leeren Nachrichten, die dem {@link Player Spieler} gesendet werden, bevor die Mute-Nachricht
     * angezeigt wird.
     */
    private static final int EMPTY_MESSAGES = 100;
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    @SneakyThrows
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if sender has required permission
        if (!sender.hasPermission("jban.mute")) {
            sender.sendMessage(PREFIX + JBan.getInstance().getNoPermsMessage());
            return true;
        }

        // check command length
        if (args.length < 2) {
            sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
                "Bitte benutze /mute <Player> <Grund>",
                "Please use /mute <Player> <Reason>"
            ));
            return true;
        }

        // declare muted Player
        final Player target = Bukkit.getPlayer(args[0]);

        // check if muted player is online
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + JBan.getInstance().getPlayerIsNotOnlineMessage());
            return true;
        }

        final File file = new File("plugins/JBan", "muted.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        final StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        cfg.set(target.getName(), reason.toString());
        cfg.save(file);

        // send empty messages
        for (int i1 = 0; i1 < EMPTY_MESSAGES; i1++) {
            target.sendMessage(" ");
        }

        // send mute-message
        target.sendMessage(JBan.getInstance().chooseCorrectLanguage(
            ChatColor.RED.toString() + ChatColor.BOLD + "Du wurdest für"
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason
                + ChatColor.RED.toString() + ChatColor.BOLD + " permanent gemutet!",
            ChatColor.RED.toString() + ChatColor.BOLD + "You were permanently muted for"
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason + ChatColor.RED.toString()
                + ChatColor.BOLD + "!"
        ));

        // send done-message
        sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
            "Du hast den Spieler " + target.getName() + ChatColor.DARK_RED.toString() + ChatColor.BOLD
                + "permanent" + ChatColor.GRAY + " für" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason
                + ChatColor.GRAY + " gemutet!",
            "You have " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "permanently"
                + ChatColor.GRAY + " muted the player " + target.getName() + " for" + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + reason + ChatColor.GRAY + "!"
        ));

        return true;
    }
    //</editor-fold>
}
