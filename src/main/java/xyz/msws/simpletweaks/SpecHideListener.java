package xyz.msws.simpletweaks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpecHideListener implements Listener, PlayerHider {
    private final Plugin plugin;

    public SpecHideListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGamemode(PlayerGameModeChangeEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateVisibility(event.getPlayer());
            }
        }.runTaskLater(plugin, 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateVisibility(player);
    }

    public void updateVisibility() {
        for (Player player : Bukkit.getOnlinePlayers())
            updateVisibility(player);
    }

    public void updateVisibility(Player player) {
        for (Player p : plugin.getServer().getOnlinePlayers())
            if (p != player) updateVisibility(player, p);
        player.setSleepingIgnored(hidePlayer(player));
    }

    public void updateVisibility(Player a, Player b) {
        if (hidePlayer(a) == hidePlayer(b)) {
            a.showPlayer(plugin, b);
            b.showPlayer(plugin, a);
            return;
        }
        if (hidePlayer(a)) b.hidePlayer(plugin, a);
        else b.showPlayer(plugin, a);
        if (hidePlayer(b)) a.hidePlayer(plugin, b);
        else a.showPlayer(plugin, b);
    }

    public boolean hidePlayer(HumanEntity player) {
        return player.getGameMode() == GameMode.SPECTATOR || player.hasPermission("simpletweaks.hide");
    }
}
