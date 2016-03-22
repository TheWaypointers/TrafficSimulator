package thewaypointers.trafficsimulator;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.simulation.Simulation;
import thewaypointers.trafficsimulator.simulation.factories.MapWorldStateFactory;

public class TrafficSimulatorStarter {
    final static long TIME_STEP = 100;
    final static long SIMULATION_TIME_STEP = 300;
    final static String PATH_TO_MAP = "roadmaps/complex.xml";

    final static WorldStateDTO worldState;
    final static IStateProvider simulation;

    public static void main(String[] args) {
        MapWorldStateFactory factory = new MapWorldStateFactory(PATH_TO_MAP);
        WorldStateDTO worldState = factory.getWorldState();
        Simulation simulation = new Simulation(worldState);

        new StarterBase(simulation, TIME_STEP, SIMULATION_TIME_STEP, worldState).bootstrap(args);
    }
}
