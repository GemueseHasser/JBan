package de.jonas.jban.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static de.jonas.JBan.PREFIX;

/**
 * Das {@link Event Event}, mit dem das Nachrichten-Schreiben verhindert wird, wenn ein
 * {@link Player Spieler} gemuted ist.
 */
public class OnChat implements Listener {

    //<editor-fold desc="event-handling">
    @EventHandler
    public void onChat(@NotNull final AsyncPlayerChatEvent e) {
        String name = e.getPlayer().getName();

        File file = new File("plugins/JBan", "muted.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (cfg.getString(name) == null) {
            return;
        }

        e.setCancelled(true);
        e.getPlayer().sendMessage(
            PREFIX + "Du bist gemutet (für \""
            + Objects.requireNonNull(cfg.getString(name)).substring(1)
            + "\")"
        );
    }
    //</editor-fold>

}
