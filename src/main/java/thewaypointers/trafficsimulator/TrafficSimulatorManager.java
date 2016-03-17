package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.LocationDTO;
import thewaypointers.trafficsimulator.common.VehicleDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.simulation.Simulation;
import thewaypointers.trafficsimulator.simulation.factories.MapWorldStateFactory;

public class TrafficSimulatorManager {

    final static long TIME_STEP = 100;
    final static long SIMULATION_TIME_STEP = 300;
    static WorldStateDTO worldState;
    static MainFrame mainFrame;

    public static void main(String[] args) {

        MapWorldStateFactory worldStateFactory = new MapWorldStateFactory("roadmaps\\Add1RoadTest.xml");
        worldState = worldStateFactory.getWorldState();
        Simulation simulation = new Simulation(worldState);
        mainFrame = new MainFrame();

        while (true) {
            try {
                Thread.sleep(TIME_STEP);
                worldState = simulation.getNextSimulationStep(SIMULATION_TIME_STEP);
                MainFrame.mapContainerPanel.mapPanel.NewStateReceived(worldState);

            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
