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
import java.time.Instant;

import static de.jonas.JBan.PREFIX;

/**
 * Implementiert den Command, mit dem {@link Player Spieler} für eine gewisse Zeit gemutet werden.
 */
public class TempMute implements CommandExecutor {

    //<editor-fold desc="CONSTANTS">
    /**
     * Die Anzahl an leeren Nachrichten, die dem {@link Player Spieler} gesendet werden, bevor die Mute-Nachricht
     * angezeigt wird.
     */
    private static final int EMPTY_MESSAGES = 100;
    /** Die minimale Argumenten-Länge, die man zum Ausführen dieses Commands braucht. */
    private static final int MINIMUM_ARGS_LENGTH = 3;
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    @SneakyThrows
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if sender has permissions
        if (!sender.hasPermission("jban.tempmute")) {
            sender.sendMessage(PREFIX + JBan.getInstance().getNoPermsMessage());
            return true;
        }

        // check the length of the command
        if (args.length < MINIMUM_ARGS_LENGTH) {
            sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
                "Bitte benutze /tempmute <Player> <Stunden> <Grund>",
                "Please use /tempmute <Player> <Hours> <Reason>"
            ));
            return true;
        }

        // declare temporary-muted player
        final Player target = Bukkit.getPlayer(args[0]);

        final double hours = Double.parseDouble(args[1].replace(",", "."));

        // check if temporary-muted player is null
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + JBan.getInstance().getPlayerIsNotOnlineMessage());
            return true;
        }

        final StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        // write player into temporary-muted players
        final File file = new File("plugins/JBan", "tempMuted.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(target.getName() + ".reason", reason.toString());
        cfg.set(target.getName() + ".mutePoint", Instant.now().toString());
        cfg.set(target.getName() + ".hours", hours);
        cfg.save(file);

        // send empty messages
        for (int i1 = 0; i1 < EMPTY_MESSAGES; i1++) {
            target.sendMessage(" ");
        }

        // send mute-message
        target.sendMessage(JBan.getInstance().chooseCorrectLanguage(
            ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + reason + ChatColor.GRAY + "für "
                + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " Stunde gemutet!" : " Stunden gemutet!"),
            ChatColor.GRAY + "You were muted for " + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + reason + ChatColor.GRAY + "for "
                + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " hour!" : " hours!")
        ));

        // send done-message
        sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
            ChatColor.GRAY + "Du hast den Spieler " + target.getName() + " für "
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason + ChatColor.GRAY
                + "für " + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " Stunde gemutet!" : " Stunden gemutet!"),
            ChatColor.GRAY + "You muted the player " + target.getName() + " for "
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason + ChatColor.GRAY
                + "for " + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " hour!" : " hours!")
        ));
        return true;
    }
    //</editor-fold>
}
