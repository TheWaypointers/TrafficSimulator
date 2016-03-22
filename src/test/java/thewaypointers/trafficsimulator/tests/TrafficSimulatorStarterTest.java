package thewaypointers.trafficsimulator.tests;

import org.junit.Test;
import thewaypointers.trafficsimulator.TrafficSimulatorStarter;
import thewaypointers.trafficsimulator.simulation.Simulation;

import static org.junit.Assert.*;

public class TrafficSimulatorStarterTest {
    @Test
    public void providerTest(){
        assertTrue(TrafficSimulatorStarter.simulationSupplier.get().getClass().equals(Simulation.class));
    }
}