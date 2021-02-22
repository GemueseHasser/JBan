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
 * Implementiert den Command, mit dem {@link Player Spieler} permanent gebannt werden.
 */
public class Ban implements CommandExecutor {

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
        if (!sender.hasPermission("jban.ban")) {
            sender.sendMessage(PREFIX + JBan.getInstance().getNoPermsMessage());
            return true;
        }

        // check the length of the command
        if (args.length < 2) {
            sender.sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
                "Bitte benutze /ban <Player> <Grund>",
                "Please use /ban <Player> <Reason>"
            ));
            return true;
        }

        // declare banned player
        final Player target = Bukkit.getPlayer(args[0]);

        // check if banned player is online
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + JBan.getInstance().getPlayerIsNotOnlineMessage());
            return true;
        }

        final StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        // write player into banned players
        final File file = new File("plugins/JBan", "banned.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(target.getName(), reason.toString());
        cfg.save(file);

        // kick player
        target.kickPlayer(JBan.getInstance().chooseCorrectLanguage(
            ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + "\n" + reason + "\n"
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + " permanent " + ChatColor.GRAY + "gebannt!",
            ChatColor.GRAY + "You were " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "permanently"
                + ChatColor.GRAY + " banned for " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "\n" + reason
                + "\n"
        ));

        // send done-message
        sender.sendMessage(JBan.getInstance().chooseCorrectLanguage(
            PREFIX + "Du hast den Spieler " + target.getName()
                + " für " + ChatColor.DARK_RED.toString() + ChatColor.BOLD
                + reason + "permanent " + ChatColor.GRAY + "gebannt!",
            PREFIX + "You have " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "permanently"
                + ChatColor.GRAY + " banned the player " + target.getName() + " for "
                + ChatColor.DARK_RED.toString() + ChatColor.BOLD + reason.substring(0, reason.length() - 1) + "!"
        ));
        return true;
    }
    //</editor-fold>
}
