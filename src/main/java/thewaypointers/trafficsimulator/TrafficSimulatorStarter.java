package thewaypointers.trafficsimulator;
import jdk.nashorn.internal.runtime.ECMAException;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.simulation.Simulation;
import thewaypointers.trafficsimulator.simulation.factories.MapWorldStateFactory;

import javax.swing.*;
import java.util.function.Supplier;

public class TrafficSimulatorStarter {
    final static long TIME_STEP = 100;
    final static long SIMULATION_TIME_STEP = 300;
    final static String PATH_TO_MAP = "roadmap.xml";

    public final static WorldStateDTO worldState;
    public final static Supplier<IStateProvider> simulationSupplier;

    static{
        MapWorldStateFactory factory = null;
        try{
            factory = new MapWorldStateFactory(PATH_TO_MAP);
        }catch (Exception e){
            String message = "Please provide a valid roadmap.xml file in current directory.";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        worldState = factory.getWorldState();
        simulationSupplier = () -> new Simulation(worldState);
    }

    public static void main(String[] args) {
        new Bootstrapper(simulationSupplier, TIME_STEP, SIMULATION_TIME_STEP, worldState).bootstrap(args);
    }
}
