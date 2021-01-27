package de.jonas.jban.commands;

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
 * Implementiert den Command, mit dem {@link Player Spieler} gebannt werden.
 */
public class Ban implements CommandExecutor {
    
    //<editor-fold desc="implementation">
    @Override
    @SneakyThrows
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if sender has permissions
        if (!sender.hasPermission("jban.ban")) {
            sender.sendMessage(PREFIX + "Dazu hast du keine Rechte!");
            return true;
        }

        // check the length of the command
        if (args.length < 2) {
            sender.sendMessage(PREFIX + "Bitte benutze /ban <Player> <Grund>");
            return true;
        }

        // declare banned player
        Player target = Bukkit.getPlayer(args[0]);

        // check if banned player is null
        if (target == null) {
            sender.sendMessage(PREFIX + "Der Spieler ist nicht online!");
            return true;
        }

        StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        // write player into banned players
        File file = new File("plugins/JBan", "banned.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(target.getName(), reason.toString());
        cfg.save(file);

        // kick player
        target.kickPlayer(ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
            + ChatColor.BOLD + "\n" + reason + "\n" + ChatColor.GRAY + " gebannt!");

        // send done-message
        sender.sendMessage(PREFIX + "Du hast den Spieler " + target.getName()
            + "für " + ChatColor.DARK_GRAY + "\"" + reason + "\"" + " gebannt!");
        return true;
    }
    //</editor-fold>
}
