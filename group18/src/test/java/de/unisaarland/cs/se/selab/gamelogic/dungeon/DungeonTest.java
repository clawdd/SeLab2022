package de.unisaarland.cs.se.selab.gamelogic.dungeon;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionFactoryImplementation;
import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DungeonTest {

    static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    static final ServerConnection<ActionCommand> SC = new ServerConnection<>(5004, -1,
            ACTION_FACTORY);

    DungeonTile tnl(final int kind) {
        switch (kind) {
            case 1 -> {
                return new Tunnel();
            }
            case 2 -> {
                final var tunnel = new Tunnel();
                tunnel.setConquered(true);
                return tunnel;
            }
            default -> {
                return null;
            }
        }
    }

    @Test
    void kacke() {
        final List<DungeonLord> list = new ArrayList<>();
        final Dungeon d = new Dungeon(4, SC, list);
        final DungeonTile[][] square = {
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(0), tnl(0), tnl(1)},
                {tnl(2), tnl(2), tnl(2), tnl(1)},
                {tnl(0), tnl(0), tnl(1), tnl(0)}
        };
        d.setDungeonSquare(square);
        Assertions.assertTrue(d.placeBattleGround(new Point(2, 3)));
        Assertions.assertFalse(d.placeBattleGround(new Point(2, 2)));
        Assertions.assertTrue(d.placeBattleGround(new Point(3, 2)));
        Assertions.assertFalse(d.placeBattleGround(new Point(0, 0)));
        Assertions.assertFalse(d.placeBattleGround(new Point(3, 0)));
        Assertions.assertFalse(d.placeBattleGround(new Point(3, 1)));
    }

    @Test
    void irgendeineKacke() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Tobais", 0, 1234, 5,
                3, 3, 3, 5, SC, list);
        list.add(dl);
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(5, 0)));

    }

    @Test
    void irgendeineKacke2() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Jonathen", 0, 1234, 5,
                30, 1, 6, 13, SC, list);
        list.add(dl);
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 0)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 4)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));

        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 0)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 2)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 3)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(4, 4)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 4)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 4)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 4)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 4)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 3)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
    }

    @Test
    void irgendeineKacke3() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Mertin", 0, 1234, 5,
                3, 3, 3, 5, SC, list);
        list.add(dl);
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 1)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 0)));

    }

    @Test
    void irgendeineKacke4() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Culin", 0, 1234, 5,
                3, 3, 3, 5, SC, list);
        list.add(dl);
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(1, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 2)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(2, 3)));
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 2)));
        Assertions.assertFalse(dl.getDungeon().placeTunnel(new Tunnel(), new Point(3, 3)));

    }

    @Test
    void irgendeineKacke5() {
        final List<DungeonLord> list = new ArrayList<>();
        final DungeonLord dl = new DungeonLord("Jashoa", 0, 1234, 5,
                3, 3, 3, 5, SC, list);
        list.add(dl);
        Assertions.assertTrue(dl.getDungeon().placeTunnel(new Tunnel(), new Point(0, 1)));
        Assertions.assertTrue(dl.getDungeon().placeBattleGround(new Point(0, 0)));
        dl.getDungeon().getDungeonSquare()[0][0].setConquered(true);
        Assertions.assertTrue(dl.getDungeon().placeBattleGround(new Point(0, 1)));
        dl.getDungeon().getDungeonSquare()[0][1].setConquered(true);
        Assertions.assertTrue(dl.getDungeon().isDungeonFullyConquered());

    }

}