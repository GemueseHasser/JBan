package de.jonas.jban.listener;

import de.jonas.JBan;
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
 * Das {@link Event Event}, mit dem das Nachrichten-Schreiben verhindert wird, wenn ein {@link Player Spieler} gemuted
 * ist.
 */
public class OnChat implements Listener {

    //<editor-fold desc="event-handling">
    @SneakyThrows
    @EventHandler
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public void onChat(@NotNull final AsyncPlayerChatEvent e) {
        // declare players name
        final String name = e.getPlayer().getName();

        final File file = new File("plugins/JBan", "muted.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        final File fileTemp = new File("plugins/JBan", "tempMuted.yml");
        final FileConfiguration cfgTemp = YamlConfiguration.loadConfiguration(fileTemp);

        // check if player is muted
        if (cfg.getString(name) == null && cfgTemp.getString(name + ".reason") == null) {
            return;
        }

        // check if player is just temporary muted
        if (cfgTemp.getString(name + ".reason") != null) {
            final String mutedPointString = cfgTemp.getString(name + ".mutePoint");
            assert mutedPointString != null;
            final Instant mutedPoint = Instant.parse(mutedPointString);
            final double hours = cfgTemp.getDouble(name + ".hours");
            final double duration = (double) getDuration(mutedPoint).toMinutes() / 60D;
            if (duration < hours) {
                e.getPlayer().sendMessage(
                    PREFIX + JBan.getInstance().chooseCorrectLanguage(
                        "Du wurdest für " + ChatColor.DARK_RED.toString()
                            + ChatColor.BOLD + cfgTemp.getString(name + ".reason")
                            + ChatColor.GRAY + "für " + ChatColor.GRAY.toString() + ChatColor.BOLD
                            + cfgTemp.getDouble(name + ".hours") + ChatColor.GRAY.toString() + ChatColor.BOLD + " "
                            + ChatColor.GRAY + (hours == 1 ? "Stunde gemutet!" : "Stunden gemutet!"),
                        "You were muted for " + ChatColor.DARK_RED.toString()
                            + ChatColor.BOLD + cfgTemp.getString(name + ".reason")
                            + ChatColor.GRAY + "for " + ChatColor.GRAY.toString() + ChatColor.BOLD
                            + cfgTemp.getDouble(name + ".hours") + ChatColor.GRAY.toString() + ChatColor.BOLD + " "
                            + ChatColor.GRAY + (hours == 1 ? "hour!" : "hours!")
                    ));
                e.setCancelled(true);
            } else {
                cfgTemp.set(name, null);
                cfgTemp.save(fileTemp);
            }
            return;
        }

        final String reason = cfg.getString(name);

        // player is permanent muted
        e.setCancelled(true);
        e.getPlayer().sendMessage(PREFIX + JBan.getInstance().chooseCorrectLanguage(
            "Du bist " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "permanent"
                + ChatColor.GRAY + " gemutet für" + ChatColor.DARK_RED.toString() + ChatColor.BOLD
                + reason,
            "You are " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "permanently"
                + ChatColor.GRAY + " muted for" + ChatColor.DARK_RED.toString() + ChatColor.BOLD
                + reason
        ));
    }
    //</editor-fold>

    /**
     * Berechnet die {@link Duration Dauer}, zwischen einem gewissen {@link Instant} und jetzt.
     *
     * @param instant Der {@link Instant}, von dem die Dauer bis jetzt berechnet wird.
     *
     * @return Die {@link Duration Dauer} zwischen den beiden {@link Instant Instants}.
     */
    private Duration getDuration(final Instant instant) {
        return Duration.between(instant, Instant.now());
    }

}
