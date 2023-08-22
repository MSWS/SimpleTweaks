package xyz.msws.simpletweaks;

import com.comphenix.packetwrapper.wrappers.status.clientbound.WrapperStatusServerServerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpecHideTabber implements PacketListener {
    private final Plugin plugin;

    public SpecHideTabber(Plugin plugin) {
        this.plugin = plugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        if (packetEvent.getPacketType() != PacketType.Status.Server.SERVER_INFO)
            return;
        WrapperStatusServerServerInfo info = new WrapperStatusServerServerInfo(packetEvent.getPacket());
        WrappedServerPing status = info.getStatus();
        List<WrappedGameProfile> players = new ArrayList<>(status.getPlayers());
        players.removeIf(profile -> Bukkit.getPlayer(profile.getUUID()).getGameMode() == GameMode.SPECTATOR);
        status.setPlayers(players);
        status.setPlayersOnline(players.size());
        info.setStatus(status);
        packetEvent.setPacket(info.getHandle());
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent) {
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.newBuilder().types(PacketType.Status.Server.SERVER_INFO).build();
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
