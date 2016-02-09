package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.simulation.Simulation;

public class TrafficSimulatorManager {
    public static void main(String[] args) {

        SimulatorRunnable simulatorRunnable = new SimulatorRunnable(4000);
        Thread t = new Thread(simulatorRunnable);
        t.start();

    }

    public static class SimulatorRunnable implements Runnable {

        long timeStep;

        public SimulatorRunnable(long timeStep) {
            this.timeStep = timeStep;
        }

        public void run() {
            Simulation sim = new Simulation();
            sim.runSimulation(timeStep);
        }
    }

}
