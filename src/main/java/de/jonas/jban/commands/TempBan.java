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
 * Implementiert den Command, mit dem {@link Player Spieler} für eine gewisse Zeit gebannt werden.
 */
public class TempBan implements CommandExecutor {

    //<editor-fold desc="CONSTANTS">
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
        if (!sender.hasPermission("jban.tempban")) {
            sender.sendMessage(PREFIX + JBan.getInstance().getNoPermsMessage());
            return true;
        }

        // check the length of the command
        if (args.length < MINIMUM_ARGS_LENGTH) {
            sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
                "Bitte benutze /tempban <Player> <Stunden> <Grund>",
                "Please use /tempban <Player> <Hours> <Reason>"
            ));
            return true;
        }

        // declare temporary-banned player
        final Player target = Bukkit.getPlayer(args[0]);

        final double hours = Double.parseDouble(args[1].replace(",", "."));

        // check if temporary-banned player is null
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + JBan.getInstance().getPlayerIsNotOnlineMessage());
            return true;
        }

        final StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        // write player into temporary-banned players
        final File file = new File("plugins/JBan", "tempBanned.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(target.getName() + ".reason", reason.toString());
        cfg.set(target.getName() + ".bannedPoint", Instant.now().toString());
        cfg.set(target.getName() + ".hours", hours);
        cfg.save(file);

        // kick player
        target.kickPlayer(JBan.getInstance().chooseCorrectLanguage(
            ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + "\n" + reason + "\n" + ChatColor.GRAY + " für "
                + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " Stunde gebannt!" : " Stunden gebannt!"),
            ChatColor.GRAY + "You were banned for " + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + "\n" + reason + "\n" + ChatColor.GRAY + " for "
                + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " hour!" : " hours!")
        ));

        // send done-message
        sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
            ChatColor.GRAY + "Du hast den Spieler " + target.getName() + " für "
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason + ChatColor.GRAY
                + "für " + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " Stunde gebannt!" : " Stunden gebannt!"),
            ChatColor.GRAY + "You banned the player " + target.getName() + " for "
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason + ChatColor.GRAY
                + "for " + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
                + (hours == 1 ? " hour!" : " hours!")
        ));
        return true;
    }
    //</editor-fold>
}
