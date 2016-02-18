package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.LocationDTO;
import thewaypointers.trafficsimulator.common.VehicleDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.simulation.Simulation;

public class TrafficSimulatorManager {

    final static long TIME_STEP = 500;
    static WorldStateDTO worldState;

    public static void main(String[] args) {

        SimulatorRunnable simulatorRunnable = new SimulatorRunnable();
        Thread t = new Thread(simulatorRunnable);
        t.start();


        MainFrame mainFrame = new MainFrame(worldState);
        while(true){
            try{
                Thread.sleep(TIME_STEP);
                worldState = simulatorRunnable.getWorldState();
                MainFrame.mapContainerPanel.mapPanel.NewStateReceived(worldState);
                output();
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
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

    public static class SimulatorRunnable implements Runnable {

        Simulation sim;

        public void run() {
            sim = new Simulation();
            sim.runSimulation();
        }

        public WorldStateDTO getWorldState(){
            return sim.getWorldState();
        }
    }

}
