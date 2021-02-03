package de.jonas.jban.listener;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import static de.jonas.JBan.PREFIX;

/**
 * Das {@link Event Event}, mit dem das Nachrichten-Schreiben verhindert wird, wenn ein
 * {@link Player Spieler} gemuted ist.
 */
public class OnChat implements Listener {

    //<editor-fold desc="event-handling">
    @SneakyThrows
    @EventHandler
    public void onChat(@NotNull final AsyncPlayerChatEvent e) {
        String name = e.getPlayer().getName();

        File file = new File("plugins/JBan", "muted.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        File fileTemp = new File("plugins/JBan", "tempMuted.yml");
        FileConfiguration cfgTemp = YamlConfiguration.loadConfiguration(fileTemp);

        if (cfg.getString(name) == null && cfgTemp.getString(name + ".reason") == null) {
            return;
        }

        if (cfgTemp.getString(name + ".reason") != null) {
            final String mutedPointString = cfgTemp.getString(name + ".mutePoint");
            assert mutedPointString != null;
            final Instant mutedPoint = Instant.parse(mutedPointString);
            final double hours = cfgTemp.getDouble(name + ".hours");
            final double duration = (double) getDuration(mutedPoint).toMinutes() / 60D;
            if (duration < hours) {
                e.getPlayer().sendMessage(
                    PREFIX + "Du wurdest für " + ChatColor.DARK_RED.toString()
                        + ChatColor.BOLD + cfgTemp.getString(name + ".reason")
                        + ChatColor.GRAY + " für " + ChatColor.GRAY.toString() + ChatColor.BOLD
                        + cfgTemp.getDouble(name + ".hours") + ChatColor.GRAY.toString() + ChatColor.BOLD + " "
                        + ChatColor.GRAY + (hours == 1 ? "Stunde gemutet!" : "Stunden gemutet!")
                );
                e.setCancelled(true);
            } else {
                cfgTemp.set(name, null);
                cfgTemp.save(fileTemp);
            }
            return;
        }

        final String reason = cfg.getString(name);

        e.setCancelled(true);
        e.getPlayer().sendMessage(
            PREFIX + "Du bist " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "permanent"
                + ChatColor.GRAY + " gemutet für" + ChatColor.DARK_RED.toString() + ChatColor.BOLD
                + "\"" + reason + "\""
        );
    }
    //</editor-fold>

    private Duration getDuration(Instant instant) {
        return Duration.between(instant, Instant.now());
    }

}
