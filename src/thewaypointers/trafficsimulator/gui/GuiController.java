package thewaypointers.trafficsimulator.gui;

import thewaypointers.trafficsimulator.common.ISimulationInputListener;

public class GuiController {

    ISimulationInputListener simulationInputListener;

    public GuiController(ISimulationInputListener simulationInputListener){
        this.simulationInputListener = simulationInputListener;
    }

    private void numberOfVehiclesChanged(int newValue){
        simulationInputListener.SimulationParameterChanged("numberOfVehicles", String.valueOf(newValue));
    }
}
