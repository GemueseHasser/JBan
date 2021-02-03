package de.jonas.jban.commands;

import lombok.SneakyThrows;
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
 * Implementiert den Command, mit dem {@link Player Spieler} wieder entmuted werden.
 */
public class Unmute implements CommandExecutor {

    //<editor-fold desc="implementation">
    @Override
    @SneakyThrows
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if sender has required permissions
        if (!sender.hasPermission("jban.unmute")) {
            sender.sendMessage(PREFIX + "Dazu hast du keine Rechte!");
            return true;
        }

        // check command-length
        if (args.length != 1) {
            sender.sendMessage(PREFIX + "Bitte benutze /unmute <Player>");
            return true;
        }

        final File file = new File("plugins/JBan", "muted.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        final File fileTemp = new File("plugins/JBan", "tempMuted.yml");
        final FileConfiguration cfgTemp = YamlConfiguration.loadConfiguration(fileTemp);

        // check if player is muted
        if (cfg.getString(args[0]) == null && cfgTemp.getString(args[0] + ".reason") == null) {
            sender.sendMessage(PREFIX + "Der Spieler ist nicht gemuted!");
            return true;
        }

        // remove player from muted players
        cfg.set(args[0], null);
        cfgTemp.set(args[0], null);
        cfg.save(file);
        cfgTemp.save(fileTemp);

        // send done-message
        sender.sendMessage(PREFIX + "Du hast den Spieler " + ChatColor.DARK_GRAY + args[0]
            + ChatColor.GRAY + " entmuted!");

        return true;
    }
    //</editor-fold>
}
