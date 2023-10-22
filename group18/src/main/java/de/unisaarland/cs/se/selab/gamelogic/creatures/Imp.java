package de.unisaarland.cs.se.selab.gamelogic.creatures;

/**
 * This class represents an imp
 */
public class Imp {

    private ImpTask currentTask;

    /**
     * creates an Imp with currentTask
     */
    public Imp() {
        this.currentTask = ImpTask.REST;
    }

    /**
     * @return returns current Task form Enum ImpTask
     */
    public ImpTask getCurrentTask() {
        return currentTask;
    }

    /**
     * @param currentTask sets current Task
     */
    public void setCurrentTask(final ImpTask currentTask) {
        this.currentTask = currentTask;
    }
}
