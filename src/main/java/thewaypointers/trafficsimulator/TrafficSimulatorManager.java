package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;

import static thewaypointers.trafficsimulator.gui.MainFrame.*;

public class TrafficSimulatorManager{

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;
    WorldStateDTO worldState;
    private static MainFrame mainFrame;
    private static IStateProvider simulation;
    private static TrafficSimulatorManager trafficSimulatorManager;
    private static boolean run = false;


    //Simulation simulation = new Simulation();

    private TrafficSimulatorManager(){

    }


    public static void setSimulation(IStateProvider simulation) {
        TrafficSimulatorManager.simulation = simulation;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        TrafficSimulatorManager.mainFrame = mainFrame;
    }

    public static TrafficSimulatorManager GetInstance()
    {

        if (trafficSimulatorManager == null)
        {
            trafficSimulatorManager = new TrafficSimulatorManager();
        }
        return trafficSimulatorManager;
    }

    public void run(){
        while(true){
            System.out.println("test");
            worldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
            mapContainerPanel.mapPanel.NewStateReceived(worldState);
            try {
                Thread.sleep((long) (1f / STATES_PER_SECOND * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseSimulation(){

    }

    public void stopSimulation(){

    }

}
