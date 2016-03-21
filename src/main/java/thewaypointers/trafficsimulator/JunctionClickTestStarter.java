package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class JunctionClickTestStarter {
    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;

    public static void main(String[] args){

        SimulationController simulationController = new SimulationController();
        simulationController.start();
        IStateProvider simulation = new JunctionTestProvider();
        MainFrame mainFrame=new MainFrame(simulation.getNextState(0));
        mainFrame.setSimulationController(simulationController);
        simulationController.setMainFrame(mainFrame);
        simulationController.setSimulation(simulation);
    }
}
