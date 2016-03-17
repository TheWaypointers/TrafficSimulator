package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class JunctionLocationTestStarter {

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;
    public static MainFrame mainFrame;


    public static void main(String[] args){

        TrafficSimulatorManager trafficSimulatorManager = new TrafficSimulatorManager();
        IStateProvider simulation = new JunctionTestProvider();
        mainFrame=new MainFrame(simulation.getNextState(0),trafficSimulatorManager);
        trafficSimulatorManager.setMainFrame(mainFrame);
        trafficSimulatorManager.setSimulation(simulation);
    }
}