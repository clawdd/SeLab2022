package de.unisaarland.cs.se.selab.gamelogic.creatures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class tests the few functionalities as implemented in Imp
 */
class ImpTest {

    final Imp imp = new Imp();

    @Test
    void checkDefaultTask() {
        Assertions.assertEquals(ImpTask.REST, imp.getCurrentTask());
    }

    @Test
    void checkNewTaskGold() {
        imp.setCurrentTask(ImpTask.GOLD);
        Assertions.assertEquals(ImpTask.GOLD, imp.getCurrentTask());
    }

    @Test
    void checkNewTask() {
        imp.setCurrentTask(ImpTask.GOLD);
        imp.setCurrentTask(ImpTask.TUNNEL);
        Assertions.assertEquals(ImpTask.TUNNEL, imp.getCurrentTask());
    }

    @Test
    void checkRoom() {
        imp.setCurrentTask(ImpTask.ROOM);
        Assertions.assertEquals(ImpTask.ROOM, imp.getCurrentTask());
    }

    @Test
    void checkSupervise() {
        imp.setCurrentTask(ImpTask.SUPERVISE);
        Assertions.assertEquals(ImpTask.SUPERVISE, imp.getCurrentTask());
    }



}