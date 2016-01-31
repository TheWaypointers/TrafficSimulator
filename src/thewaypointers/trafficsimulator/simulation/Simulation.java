package thewaypointers.trafficsimulator.simulation;

import thewaypointers.trafficsimulator.common.ISimulationInputListener;
import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.WorldStateDTO;


public class Simulation implements ISimulationInputListener {

    IStateChangeListener stateChangeListener;

    public Simulation(IStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    @Override
    public void SimulationParameterChanged(String parameterName, String value) {

    }

    public void NextSimulationStep(float timeStep){

        // modify simulation state...

        WorldStateDTO state = new WorldStateDTO();

        // put the new simulation state into the DTO...

        stateChangeListener.NewStateReceived(state);
    }
}
