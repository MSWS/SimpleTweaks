package xyz.msws.simpletweaks;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location loc = player.getLocation();

        String coords = "%d %d %d".formatted(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        player.sendMessage(ChatColor.GRAY + "You died at " + ChatColor.YELLOW + coords + ChatColor.GRAY + ".");
    }
}
