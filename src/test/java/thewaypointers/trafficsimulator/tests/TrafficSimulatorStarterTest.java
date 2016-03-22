package thewaypointers.trafficsimulator.tests;

import org.junit.Test;
import thewaypointers.trafficsimulator.TrafficSimulatorStarter;
import thewaypointers.trafficsimulator.simulation.Simulation;

import static org.junit.Assert.*;

public class TrafficSimulatorStarterTest {
    @Test
    public void providerTest(){
        assertTrue(TrafficSimulatorStarter.simulation.getClass().equals(Simulation.class));
    }
}