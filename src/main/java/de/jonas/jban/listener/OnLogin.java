package de.jonas.jban.listener;

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

/**
 * Das {@link Event Event}, mit dem das Joinen verhindert wird, wenn ein
 * {@link Player Spieler} gebannt ist.
 */
public class OnLogin implements Listener {

    //<editor-fold desc="event-handling">
    @EventHandler
    public void onLogin(@NotNull final PlayerLoginEvent e) {
        String name = e.getPlayer().getName();

        File file = new File("plugins/JBan", "banned.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (cfg.getString(name) == null) {
            return;
        }

        e.disallow(
            PlayerLoginEvent.Result.KICK_BANNED,
            ChatColor.GRAY + "Du wurdest für " + ChatColor.DARK_RED.toString()
            + ChatColor.BOLD + "\n" + cfg.getString(name) + "\n" + ChatColor.GRAY + " gebannt!"
        );
    }
    //</editor-fold>

}
