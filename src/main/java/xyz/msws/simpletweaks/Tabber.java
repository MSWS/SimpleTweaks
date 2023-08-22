package xyz.msws.simpletweaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Tabber extends BukkitRunnable {
    private static Object bukkitServer;
    private static Field recentTps;

    public Tabber() {
        try {
            bukkitServer = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".CraftServer").getMethod("getServer").invoke(Bukkit.getServer());
            recentTps = bukkitServer.getClass().getField("recentTps");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException |
                 NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    private double getTps() {
        return getTps(0);
    }

    private double getTps(int index) {
        if (bukkitServer == null || recentTps == null) return -1;
        try {
            double result = ((double[]) recentTps.get(bukkitServer))[index];
            return Math.min(20, Math.max(0, result));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private double[] getTpsArray() {
        if (bukkitServer == null || recentTps == null) return new double[]{-1, -1, -1};
        try {
            double[] result = ((double[]) recentTps.get(bukkitServer));
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.min(20, Math.max(0, result[i]));
            }
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new double[]{-1, -1, -1};
    }

    @Override
    public void run() {
        double[] tpsArray = getTpsArray();
        String tpsLabel = "%sT%sP%sS".formatted(getTPSColor(tpsArray[0]), getTPSColor(tpsArray[1]), getTPSColor(tpsArray[2]));
        double tps = tpsArray[0];
        String tpsString = ChatColor.GRAY + tpsLabel + ": " + getTPSColor(tps) + "%.2f".formatted(tps);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setPlayerListHeaderFooter(tpsString, null);
        });
    }

    private net.md_5.bungee.api.ChatColor getTPSColor(double tps) {
        // If TPS is between 10 and 20, interpolate between green and red
        // if TPS is < 10, interpolate between red and black
        if (tps <= 10) {
            float hue = 0;
            float brightness = (float) tps / 10f;
            return net.md_5.bungee.api.ChatColor.of(Color.getHSBColor(hue, 1, brightness));
        }

        float hue = ((float) tps - 10f) / 10f * 120f;
        hue /= 360f;
        return net.md_5.bungee.api.ChatColor.of(Color.getHSBColor(hue, 1, 1));
    }
}
