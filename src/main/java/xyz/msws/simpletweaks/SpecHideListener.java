package xyz.msws.simpletweaks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

public class SpecHideListener implements Listener {
    private final Plugin plugin;

    public SpecHideListener(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            updateVisibility();
        }, 0, 20);
    }

    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent event) {
        updateVisibility(event.getPlayer());
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {

    }

    public void updateVisibility() {
        for (Player player : Bukkit.getOnlinePlayers())
            updateVisibility(player);
    }

    public void updateVisibility(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers())
            if (p != player)
                updateVisibility(player, p);
    }

    public void updateVisibility(Player a, Player b) {
        if (a.getGameMode() == b.getGameMode()) {
            a.showPlayer(plugin, b);
            b.showPlayer(plugin, a);
            return;
        }
        if (a.getGameMode() == GameMode.SPECTATOR)
            b.hidePlayer(plugin, a);
        else
            b.showPlayer(plugin, a);
        if (b.getGameMode() == GameMode.SPECTATOR)
            a.hidePlayer(plugin, b);
        else
            a.showPlayer(plugin, b);
    }
}
