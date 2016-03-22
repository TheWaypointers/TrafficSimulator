package thewaypointers.trafficsimulator.tests;

import org.junit.Test;
import thewaypointers.trafficsimulator.RoadNetworkStarter;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;

import static org.junit.Assert.*;

public class RoadNetworkStarterTest {
    @Test
    public void providerTest(){
        assertTrue(RoadNetworkStarter.PROVIDER.getClass().equals(RoadNetworkProvider.class));
    }
}