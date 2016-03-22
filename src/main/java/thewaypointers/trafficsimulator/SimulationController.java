package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;

public class SimulationController extends Thread {

    private IStateChangeListener output;
    private IStateProvider simulation;
    private WorldStateDTO worldState;
    private boolean isSleep = true;
    private long timeStep;
    private long simulationTimeStep;

    public SimulationController(long timeStep, long simulationTimeStep) {
        this.timeStep = timeStep;
        this.simulationTimeStep = simulationTimeStep;
    }

    public void setSimulation(IStateProvider simulation) {
        this.simulation = simulation;
    }

    public void setOutput(IStateChangeListener output) {
        this.output = output;
    }

    public synchronized void pauseSimulation() {
        isSleep = !isSleep;
    }

    public synchronized void clearSimulation() {
        isSleep = true;
        simulation = new JunctionTestProvider();
//        mainFrame.setVisible(false);
//        mainFrame = new MainFrame(simulation.getNextState(0),new SimulationController());
    }

    public void run() {
        while (true) {
            if (!isSleep) {
                System.out.println("New State, timestep "+ simulationTimeStep);
                worldState = simulation.getNextState(simulationTimeStep);
                System.out.println("Current simulation time "+ worldState.getClock());
                output.NewStateReceived(worldState);
            }
            try {
                Thread.sleep(timeStep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
