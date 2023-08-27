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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpecHideTabber implements PacketListener {
    private final Plugin plugin;
    private final PlayerHider hider;

    public SpecHideTabber(Plugin plugin, PlayerHider hider) {
        this.plugin = plugin;
        this.hider = hider;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        if (packetEvent.getPacketType() != PacketType.Status.Server.SERVER_INFO)
            return;
        WrapperStatusServerServerInfo info = new WrapperStatusServerServerInfo(packetEvent.getPacket());
        WrappedServerPing status = info.getStatus();
        List<WrappedGameProfile> players = new ArrayList<>(status.getPlayers());
        Iterator<WrappedGameProfile> iterator = players.iterator();
        while (iterator.hasNext()) {
            WrappedGameProfile profile = iterator.next();
            Player player = Bukkit.getPlayer(profile.getUUID());
            if (player == null)
                continue;
            if (hider.hidePlayer(player))
                iterator.remove();
        }
        status.setPlayers(players);
        status.setPlayersOnline(players.size());
        status.setPlayersMaximum(players.size() + 1);
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
