package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;

public class StateProviderController extends Thread {

    private IStateChangeListener output;
    private IStateProvider simulation;
    private WorldStateDTO worldState;
    private boolean isSleep = true;
    private long timeStep;
    private long simulationTimeStep;

    public StateProviderController(long timeStep, long simulationTimeStep) {
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
//        mainFrame = new MainFrame(simulation.getNextState(0),new StateProviderController());
    }

    public void run() {
        while (true) {
            if (!isSleep) {
                worldState = simulation.getNextState(simulationTimeStep);
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
