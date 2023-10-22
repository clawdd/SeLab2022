package de.unisaarland.cs.se.selab.gamelogic.dungeon;

class BigDungeonMethodsMax {


    //GANZE KLASSE GEHT NICHT WEGEN SERVERCONNECTION ZEUGS
    //GANZE KLASSE GEHT NICHT WEGEN SERVERCONNECTION ZEUGS
    //GANZE KLASSE GEHT NICHT WEGEN SERVERCONNECTION ZEUGS
    //GANZE KLASSE GEHT NICHT WEGEN SERVERCONNECTION ZEUGS

    /*
    final List<DungeonLord> list = new ArrayList<>();

    ServerConnection<ActionCommand> SC = Mockito.mock(ServerConnection.class);
    Dungeon dungeon = new Dungeon(5,SC,list);


    // static final ActionFactory<ActionCommand> ACTION_FACTORY = new ActionFactoryImplementation();
    //
    // static final ServerConnection SC = Mockito.mock(ServerConnection.class);
    //<<--- Eval traps--->>

    //<<--- Basic --->>

    //Without DefuseValue
    @Test
    void evalTraps() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap =
                new TrapBuilder().withAttackStrategy(AttackStrategy.BASIC).withDamage(2).build();
        dl.getDungeon().setTrap(trap);
        
        final Adventurer adventurer1 =
                new AdventurerBuilder().withHealthPoints(5).build();

        final Adventurer adventurer2 =
                new AdventurerBuilder().withHealthPoints(5).build();

        final Adventurer adventurer3 =
                new AdventurerBuilder().withHealthPoints(5).build();

        dl.getDungeon().getAdventurers().add(adventurer1);
        dl.getDungeon().getAdventurers().add(adventurer2);
        dl.getDungeon().getAdventurers().add(adventurer3);

        dl.getDungeon().evalTrap(SC, dl, dl.getDungeon().getAdventurers());

        Assertions.assertEquals(3, adventurer1.getHealthPoints());
        Assertions.assertEquals(5, adventurer2.getHealthPoints());
        Assertions.assertEquals(5, adventurer3.getHealthPoints());

    }

    //With DefuseValue
    @Test
    void evalTraps2() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap =
                new TrapBuilder().withAttackStrategy(AttackStrategy.BASIC).withDamage(5).build();
        dl.getDungeon().setTrap(trap);

        final Adventurer adventurer1 =
                new AdventurerBuilder().withHealthPoints(5).withDefuseValue(3).build();

        final Adventurer adventurer2 =
                new AdventurerBuilder().withHealthPoints(5).build();

        final Adventurer adventurer3 =
                new AdventurerBuilder().withHealthPoints(5).build();

        dl.getDungeon().getAdventurers().add(adventurer1);
        dl.getDungeon().getAdventurers().add(adventurer2);
        dl.getDungeon().getAdventurers().add(adventurer3);

        dl.getDungeon().evalTrap(SC, dl, dl.getDungeon().getAdventurers());

        Assertions.assertEquals(3, adventurer1.getHealthPoints());
        Assertions.assertEquals(5, adventurer2.getHealthPoints());
        Assertions.assertEquals(5, adventurer3.getHealthPoints());

    }

    //Defuse value high enough to defuse whole trap damage
    @Test
    void evalTraps3() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap =
                new TrapBuilder().withAttackStrategy(AttackStrategy.BASIC).withDamage(5).build();
        dl.getDungeon().setTrap(trap);

        final Adventurer adventurer1 =
                new AdventurerBuilder().withHealthPoints(5).withDefuseValue(5).build();

        final Adventurer adventurer2 =
                new AdventurerBuilder().withHealthPoints(5).build();

        final Adventurer adventurer3 =
                new AdventurerBuilder().withHealthPoints(5).build();

        dl.getDungeon().getAdventurers().add(adventurer1);
        dl.getDungeon().getAdventurers().add(adventurer2);
        dl.getDungeon().getAdventurers().add(adventurer3);

        dl.getDungeon().evalTrap(SC, dl, dl.getDungeon().getAdventurers());

        Assertions.assertEquals(5, adventurer1.getHealthPoints());
        Assertions.assertEquals(5, adventurer2.getHealthPoints());
        Assertions.assertEquals(5, adventurer3.getHealthPoints());

    }

    //<<--- Targeted --->>

    @Test
    void evalTargetedTraps() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap =
                new TrapBuilder().withAttackStrategy(AttackStrategy.BASIC).withDamage(5).
                        withTarget(2).build();
        dl.getDungeon().setTrap(trap);

        final Adventurer adventurer1 =
                new AdventurerBuilder().withHealthPoints(5).withDefuseValue(0).build();

        final Adventurer adventurer2 =
                new AdventurerBuilder().withHealthPoints(5).build();

        final Adventurer adventurer3 =
                new AdventurerBuilder().withHealthPoints(5).build();

        dl.getDungeon().getAdventurers().add(adventurer1);
        dl.getDungeon().getAdventurers().add(adventurer2);
        dl.getDungeon().getAdventurers().add(adventurer3);

        dl.getDungeon().evalTrap(SC, dl, dl.getDungeon().getAdventurers());

        Assertions.assertEquals(5, adventurer1.getHealthPoints());
        Assertions.assertEquals(3, adventurer2.getHealthPoints());
        Assertions.assertEquals(5, adventurer3.getHealthPoints());

    }

    //<<--- Multi --->>

    @Test
    void evalMultiTrap() {
        final DungeonLord dl = new DungeonLord("Mox", 69, 1234, 5, 5000, 5000, 5000, 15, SC,
                list);

        list.add(dl);

        final Trap trap =
                new TrapBuilder().withAttackStrategy(AttackStrategy.BASIC).withDamage(5).
                        withTarget(2).build();
        dl.getDungeon().setTrap(trap);

        final Adventurer adventurer1 =
                new AdventurerBuilder().withHealthPoints(5).withDefuseValue(0).build();

        final Adventurer adventurer2 =
                new AdventurerBuilder().withHealthPoints(5).build();

        final Adventurer adventurer3 =
                new AdventurerBuilder().withHealthPoints(5).build();

        dl.getDungeon().getAdventurers().add(adventurer1);
        dl.getDungeon().getAdventurers().add(adventurer2);
        dl.getDungeon().getAdventurers().add(adventurer3);

        dl.getDungeon().evalTrap(SC, dl, dl.getDungeon().getAdventurers());

        Assertions.assertEquals(5, adventurer1.getHealthPoints());
        Assertions.assertEquals(3, adventurer2.getHealthPoints());
        Assertions.assertEquals(5, adventurer3.getHealthPoints());

    }*/
}