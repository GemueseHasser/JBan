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
 * Implementiert den Command, mit dem Spieler gewarnt werden.
 */
public class Warn implements CommandExecutor {

    //<editor-fold desc="CONSTANTS">
    /**
     * Die Anzahl an leeren Nachrichten, die dem {@link Player Spieler} gesendet werden, bevor die Warn-Nachricht
     * angezeigt wird.
     */
    private static final int EMPTY_MESSAGES = 100;
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        if (!sender.hasPermission("jban.warn")) {
            sender.sendMessage(PREFIX + "Dazu hast du keine Rechte!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(PREFIX + "Bitte benutze /warn <Player> <Grund>");
            return true;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + "Der Spieler ist nicht online!");
            return true;
        }

        final StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        for (int i1 = 0; i1 < EMPTY_MESSAGES; i1++) {
            target.sendMessage(" ");
        }
        target.sendMessage(ChatColor.DARK_RED + "WARNUNG: " + ChatColor.RED + reason);

        sender.sendMessage(PREFIX + "Du hast den Spieler " + target.getName() + " gewarnt!"
            + "(" + reason + ")");

        return true;
    }
    //</editor-fold>
}
