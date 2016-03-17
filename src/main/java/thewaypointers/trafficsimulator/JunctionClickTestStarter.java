package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class JunctionClickTestStarter {
    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;
    public static MainFrame mainFrame;


    public static void main(String[] args){

        TrafficSimulatorManager trafficSimulatorManager = new TrafficSimulatorManager();
        IStateProvider simulation = new RoadNetworkProvider();
        mainFrame=new MainFrame(simulation.getNextState(0),trafficSimulatorManager);
        trafficSimulatorManager.setMainFrame(mainFrame);
        trafficSimulatorManager.setSimulation(simulation);
    }
}
