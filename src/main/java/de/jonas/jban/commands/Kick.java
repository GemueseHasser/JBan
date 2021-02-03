package de.jonas.jban.commands;

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
            sender.sendMessage(PREFIX + "Dazu hast du keine Rechte!");
            return true;
        }

        // check command-length
        if (args.length < 2) {
            sender.sendMessage(PREFIX + "Bitte benutze /kick <Player> <Grund>");
            return true;
        }

        // declare kicked player
        Player target = Bukkit.getPlayer(args[0]);

        // check if kicked player is online
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + "Der Spieler ist nicht online!");
            return true;
        }

        StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        // kick player
        target.kickPlayer(ChatColor.GRAY + "Du wurdest für "
            + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + reason
            + ChatColor.GRAY + " gekickt!");

        // send done-message
        sender.sendMessage(PREFIX + ChatColor.GRAY + "Du hast den Spieler " + target.getName() + " für "
            + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + reason
            + ChatColor.GRAY + " gekickt!");

        return true;
    }
    //</editor-fold>
}
