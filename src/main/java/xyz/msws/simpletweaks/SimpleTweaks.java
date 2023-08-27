package xyz.msws.simpletweaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleTweaks extends JavaPlugin {

    public static final String PREFIX = ChatColor.BLUE + "SimpleTweaks " + ChatColor.AQUA + "> " + ChatColor.GRAY;
    private PlayerHider hider;

    @Override
    public void onEnable() {
        registerEvents();
        startTabber();
        registerCommands();

        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib"))
            new SpecHideTabber(this, hider);
    }

    private void registerEvents() {
        ChatListener chatListener = new ChatListener();
        DeathListener deathListener = new DeathListener();
        SpecHideListener specHideListener = new SpecHideListener(this);

        Bukkit.getPluginManager().registerEvents(chatListener, this);
        Bukkit.getPluginManager().registerEvents(deathListener, this);
        Bukkit.getPluginManager().registerEvents(specHideListener, this);

        this.hider = specHideListener;
    }

    private void registerCommands() {
        getCommand("enderchest").setExecutor(new EnderChestCommand());
    }

    private void startTabber() {
        new Tabber().runTaskTimer(this, 100, 20);
    }
}
