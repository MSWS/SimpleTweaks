package xyz.msws.simpletweaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleTweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        registerEvents();
        startTabber();

        if(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib"))
            new SpecHideTabber(this);
    }

    private void registerEvents() {
        ChatListener chatListener = new ChatListener();
        DeathListener deathListener = new DeathListener();
        SpecHideListener specHideListener = new SpecHideListener(this);

        Bukkit.getPluginManager().registerEvents(chatListener, this);
        Bukkit.getPluginManager().registerEvents(deathListener, this);
        Bukkit.getPluginManager().registerEvents(specHideListener, this);
    }

    private void startTabber() {
        new Tabber().runTaskTimer(this, 100, 20);
    }
}
