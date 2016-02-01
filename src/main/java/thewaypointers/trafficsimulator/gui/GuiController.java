package thewaypointers.trafficsimulator.gui;

import thewaypointers.trafficsimulator.common.ISimulationInputListener;

public class GuiController {

    ISimulationInputListener simulationInputListener;

    public GuiController(ISimulationInputListener simulationInputListener){
        this.simulationInputListener = simulationInputListener;
    }

    // example of passing changed simulation parameters to the listener
    public void numberOfVehiclesChanged(int newValue){
        simulationInputListener.SimulationParameterChanged("numberOfVehicles", String.valueOf(newValue));
    }
}
