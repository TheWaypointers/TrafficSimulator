package thewaypointers.trafficsimulator;

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
                mainFrame.mapPanel.NewStateReceived(worldState);
                output();
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void output() {
        for(VehicleDTO vehicle : worldState.vehicles){
            System.out.println(vehicle.location);
        }
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
