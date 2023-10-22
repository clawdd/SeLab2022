package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.builder.RoomBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TunnelTestHCP {

    @Test
    void tunnelIsTunnel() {
        final DungeonTile tunnel = new Tunnel();
        Assertions.assertFalse(tunnel.getConquered());
    }

    @Test
    void roomIsNotTunnel() {
        final RoomBuilder roomBuilder = new RoomBuilder();
        final DungeonTile room = roomBuilder.build();
        Assertions.assertFalse(room.getConquered());
    }

    @Test
    void tunnelIsNotConquered() {
        final DungeonTile tunnel = new Tunnel();
        Assertions.assertFalse(tunnel.getConquered());
    }

    @Test
    void tunnelIsConquered() {
        final DungeonTile tunnel = new Tunnel();
        tunnel.setConquered(true);
        Assertions.assertTrue(tunnel.getConquered());
    }
}