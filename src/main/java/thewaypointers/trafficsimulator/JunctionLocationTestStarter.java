package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class JunctionLocationTestStarter {

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;

    public static void main(String[] args){

        SimulationController simulationController = new SimulationController();
        IStateProvider simulation = new JunctionTestProvider();
        MainFrame mainFrame=new MainFrame(simulation.getNextState(0));
        mainFrame.setSimulationController(simulationController);
        simulationController.setMainFrame(mainFrame);
        simulationController.setSimulation(simulation);

        while (true){

            MainFrame.mapContainerPanel.mapPanel.NewStateReceived(simulation.getNextState(2));

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}