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
import java.time.Instant;

import static de.jonas.JBan.PREFIX;

public class TempMute implements CommandExecutor {

    private static final int EMPTY_MESSAGES = 100;

    @Override
    @SneakyThrows
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command cmd,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if sender has permissions
        if (!sender.hasPermission("jban.tempmute")) {
            sender.sendMessage(PREFIX + "Dazu hast du keine Rechte!");
            return true;
        }

        // check the length of the command
        if (args.length < 3) {
            sender.sendMessage(PREFIX + "Bitte benutze /tempmute <Player> <hours> <Grund>");
            return true;
        }

        // declare muted player
        Player target = Bukkit.getPlayer(args[0]);

        final double hours = Double.parseDouble(args[1].replace(",", "."));

        // check if muted player is null
        if (target == null || !target.isOnline()) {
            sender.sendMessage(PREFIX + "Der Spieler ist nicht online!");
            return true;
        }

        StringBuilder reason = new StringBuilder();

        // form reason
        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        // write player into muted players
        File file = new File("plugins/JBan", "tempMuted.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set(target.getName() + ".reason", reason.toString());
        cfg.set(target.getName() + ".mutePoint", Instant.now().toString());
        cfg.set(target.getName() + ".hours", hours);
        cfg.save(file);

        // send empty messages
        for (int i1 = 0; i1 < EMPTY_MESSAGES; i1++) {
            target.sendMessage(" ");
        }

        // send mute-message
        target.sendMessage(ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
            + ChatColor.BOLD + "\n" + reason + "\n" + ChatColor.GRAY + " für "
            + ChatColor.DARK_RED.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
            + (hours == 1 ? " Stunde gemutet!" : " Stunden gemutet!"));

        // send done-message
        sender.sendMessage(PREFIX + ChatColor.GRAY + "Du hast den Spieler " + target.getName() + " für "
            + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "\"" + reason + "\"" + ChatColor.GRAY
            + " für " + ChatColor.GRAY.toString() + ChatColor.BOLD + hours + ChatColor.GRAY
            + (hours == 1 ? " Stunde gemutet!" : " Stunden gemutet!"));
        return true;
    }
}
