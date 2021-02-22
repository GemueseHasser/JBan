package de.jonas.jban.commands;

import de.jonas.JBan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.jonas.JBan.PREFIX;

/**
 * Implementiert den Command, mit dem {@link Player Spieler} gekickt werden.
 */
public class Kick implements CommandExecutor {

    //<editor-fold desc="implementation">
    @Override
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if sender has required permissions
        if (!sender.hasPermission("jban.kick")) {
            sender.sendMessage(PREFIX + JBan.getInstance().getNoPermsMessage());
            return true;
        }

        // check command-length
        if (args.length < 2) {
            sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
                "Bitte benutze /kick <Player> <Grund>",
                "Please use /kick <Player> <Reason>"
            ));
            return true;
        }

        // declare kicked player
        final Player target = Bukkit.getPlayer(args[0]);

        // check if kicked player is online
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + JBan.getInstance().getPlayerIsNotOnlineMessage());
            return true;
        }

        final StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        // kick player
        target.kickPlayer(JBan.getInstance().chooseCorrectLanguage(
            ChatColor.GRAY + "Du wurdest für "
                + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + reason
                + ChatColor.GRAY + "gekickt!",
            ChatColor.GRAY + "You were kicked for " + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD
                + reason.substring(0, reason.length() - 1) + ChatColor.GRAY + "!"
        ));

        // send done-message
        sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
            "Du hast den Spieler " + target.getName() + " für "
                + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + reason
                + ChatColor.GRAY + "gekickt!",
            "You kicked the player " + target.getName() + " for " + ChatColor.DARK_GRAY.toString()
                + ChatColor.BOLD + reason.substring(0, reason.length() - 1) + ChatColor.GRAY + "!"
        ));

        return true;
    }
    //</editor-fold>
}
