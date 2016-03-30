package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.ISimulationInputListener;
import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.simulation.Simulation;

import java.util.function.Function;
import java.util.function.Supplier;

public class StateProviderController extends Thread implements ISimulationInputListener {

    private IStateChangeListener output;
    private IStateProvider simulation;
    private WorldStateDTO worldState;
    private boolean isSleep = true;
    private long timeStep;
    private long simulationTimeStep;
    private Supplier<IStateProvider> providerSupplier;

    public StateProviderController(long timeStep, long simulationTimeStep) {
        this.timeStep = timeStep;
        this.simulationTimeStep = simulationTimeStep;
    }

    public void setProviderSupplier(Supplier<IStateProvider> providerSupplier) {
        this.providerSupplier = providerSupplier;
        simulation = providerSupplier.get();
    }

    public void setOutput(IStateChangeListener output) {
        this.output = output;
    }

    public synchronized void pauseSimulation() {
        isSleep = !isSleep;
    }

    public synchronized void clearSimulation() {
        isSleep = true;
        simulation = providerSupplier.get();
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


    @Override
    public void SimulationParameterChanged(String parameterName, String value) {
        if(!simulation.getClass().equals(Simulation.class)){
            System.out.println(String.format(
                    "Warning: %s is not %s, so it can't receive changing input parameters",
                    simulation.getClass().getName(),
                    Simulation.class.getName()));
            return;
        }
        ((Simulation)simulation).SimulationParameterChanged(parameterName, value);
    }
}
