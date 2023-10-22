package de.unisaarland.cs.se.selab.statemachine;

import static de.unisaarland.cs.se.selab.statemachine.StateEnum.REGISTRATION;

import de.unisaarland.cs.se.selab.actioncommand.ActionCommand;
import de.unisaarland.cs.se.selab.actioncommand.ActionCommandEnum;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.gamelogic.DungeonLord;
import java.util.List;

/**
 * This state handles the registration of the players
 */
public class RegistrationState extends State {

    private final String configString;

    /**
     * REGISTRATION hard-coded enum-value
     *
     * @param stateMachine in order to access the StateMachine and e.g. updateState
     * @param configString the config to send to all players
     */
    public RegistrationState(final StateMachine stateMachine, final String configString) {
        super(stateMachine, REGISTRATION);
        this.configString = configString;
    }

    /**
     * @return configString
     */
    public String getConfigString() {
        return configString;
    }

    /**
     * concrete implementation of run in this state: - wait for nextAction - invoke execute-method
     * in Register to let players register (ActionCommand) - invoke execute-method in StartGame to
     * start the game (ActionCommand) - invoke updateState-method in StateMachine, transition to
     * BiddingPlaceState (State)
     */
    @Override
    public void run() throws TimeoutException {

        final int maxPlayers = getStateMachine().getGameBoard().getMaxPlayers();
        final List<DungeonLord> dungeonLordList = stateMachine.getGameBoard().getPlayers();

        try { // in case of timeout

            int counter = 0;
            while (counter < maxPlayers) {
                // request action from new player and decide next step in switch
                final ActionCommand nextCommandClient = stateMachine.getSc().nextAction();
                final var registeredCommIds =
                        List.copyOf(getStateMachine().getGameBoard().getPlayers().stream().map(
                                DungeonLord::getCommId).toList());
                final ActionCommandEnum returnValueEnum =
                        nextCommandClient.execute(registeredCommIds);

                switch (returnValueEnum) {
                    // set counter to current number of Players, this handles player(s) left
                    case REGISTER -> counter = getStateMachine().getGameBoard().getPlayers().size();
                    // skip for-loop
                    case STARTGAME -> counter = maxPlayers;
                    // if a player leaves, the LeaveCommand sends out the corresponding event
                    case LEAVE -> {                     // check for remaining players
                        if (checkAllPlayersLeftAndUpdateState()) {
                            return;
                        }
                    }
                    default -> stateMachine.getSc().sendActionFailed(
                            nextCommandClient.getCommId(),
                            "Invalid command. Register, StartGame or Leave expected.");

                    /* that's the old handling of this event; in case I misread it
                    // broadcast RegistrationAborted
                    default -> dungeonLordList.forEach(
                            p -> stateMachine.getSc().sendRegistrationAborted(p.getCommId()));

                     */
                }
            }

            // send GameStarted
            dungeonLordList.forEach(p -> stateMachine.getSc().sendGameStarted(p.getCommId()));

            // send Player to all players, in ascending order of player IDs
            for (final DungeonLord receivingPlayer : dungeonLordList) {
                for (final DungeonLord respectivePlayer : dungeonLordList) {
                    stateMachine.getSc().sendPlayer(
                            receivingPlayer.getCommId(),
                            respectivePlayer.getName(),
                            respectivePlayer.getId());
                }
            }
            stateMachine.updateState(new BiddingPlaceState(stateMachine));

        } catch (TimeoutException e) {

            // broadcast RegistrationAborted
            dungeonLordList.forEach(
                    p -> stateMachine.getSc().sendRegistrationAborted(p.getCommId()));
            throw e;
        }
    }
}
