package thewaypointers.trafficsimulator;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.simulation.Simulation;
import thewaypointers.trafficsimulator.simulation.factories.MapWorldStateFactory;

public class TrafficSimulatorStarter {
    final static long TIME_STEP = 100;
    final static long SIMULATION_TIME_STEP = 300;
    final static String PATH_TO_MAP = "roadmaps/complex.xml";

    public final static WorldStateDTO worldState;
    public final static IStateProvider simulation;

    static{
        MapWorldStateFactory factory = new MapWorldStateFactory(PATH_TO_MAP);
        worldState = factory.getWorldState();
        simulation = new Simulation(worldState);
    }

    public static void main(String[] args) {
        new Bootstrapper(simulation, TIME_STEP, SIMULATION_TIME_STEP, worldState).bootstrap(args);
    }
}
