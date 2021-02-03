package de.jonas.jban.listener;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * Das {@link Event Event}, mit dem das Joinen verhindert wird, wenn ein
 * {@link Player Spieler} gebannt ist.
 */
public class OnLogin implements Listener {

    //<editor-fold desc="event-handling">
    @SneakyThrows
    @EventHandler
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public void onLogin(@NotNull final PlayerLoginEvent e) {
        // declare players name
        final String name = e.getPlayer().getName();

        final File file = new File("plugins/JBan", "banned.yml");
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        final File fileTemp = new File("plugins/JBan", "tempBanned.yml");
        final FileConfiguration cfgTemp = YamlConfiguration.loadConfiguration(fileTemp);

        // check if player is banned
        if (cfg.getString(name) == null && cfgTemp.getString(name + ".reason") == null) {
            return;
        }

        // check if player is just temporary banned
        if (cfgTemp.getString(name + ".reason") != null) {
            final String bannedPointString = cfgTemp.getString(name + ".bannedPoint");
            assert bannedPointString != null;
            final Instant bannedPoint = Instant.parse(bannedPointString);
            final double hours = cfgTemp.getDouble(name + ".hours");
            final double duration = (double) getDuration(bannedPoint).toMinutes() / 60D;
            if (duration < hours) {
                e.disallow(
                    PlayerLoginEvent.Result.KICK_BANNED,
                    ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
                        + ChatColor.BOLD + "\n" + cfgTemp.getString(name + ".reason") + "\n"
                        + ChatColor.GRAY + " für " + ChatColor.GRAY.toString() + ChatColor.BOLD
                        + cfgTemp.getDouble(name + ".hours") + ChatColor.GRAY.toString() + ChatColor.BOLD + " "
                        + ChatColor.GRAY + (hours == 1 ? "Stunde gebannt!" : "Stunden gebannt!")
                );
            } else {
                cfgTemp.set(name, null);
                cfgTemp.save(fileTemp);
                e.allow();
            }
            return;
        }

        // player is permanent banned
        e.disallow(
            PlayerLoginEvent.Result.KICK_BANNED,
            ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
            + ChatColor.BOLD + "\n" + cfg.getString(name) + "\n" + ChatColor.DARK_RED.toString()
                + ChatColor.BOLD + "permanent " + ChatColor.GRAY + "gebannt!"
        );
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
