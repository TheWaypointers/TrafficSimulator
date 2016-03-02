package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.LocationDTO;
import thewaypointers.trafficsimulator.common.VehicleDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.simulation.Simulation;

public class TrafficSimulatorManager {

    final static long TIME_STEP = 100;
    final static long SIMULATION_TIME_STEP = 300;
    static WorldStateDTO worldState;

    public static void main(String[] args) {

        Simulation simulation = new Simulation();
        MainFrame mainFrame = new MainFrame(worldState);
        while(true){
            try{
                Thread.sleep(TIME_STEP);
                worldState = simulation.getNextSimulationStep(SIMULATION_TIME_STEP);
                MainFrame.mapContainerPanel.mapPanel.NewStateReceived(worldState);
                output();
            }
            catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void waitForThread() {
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void output() {
        LocationDTO loc = worldState.getVehicleList().getAll().get(0).getLocation();
        System.out.format("Vehicle 0 position: %f en route from %s to %s",
                loc.getDistanceTravelled(),
                loc.getRoad().getFrom().getLabel(),
                loc.getRoad().getTo().getLabel());
        System.out.println();
    }

}
