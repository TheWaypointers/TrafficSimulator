package thewaypointers.trafficsimulator.tests;

import org.junit.Test;
import thewaypointers.trafficsimulator.JunctionLocationTestStarter;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;

import static org.junit.Assert.*;

public class JunctionLocationTestStarterTest {
    @Test
    public void providerTest(){
        assertTrue(JunctionLocationTestStarter.PROVIDER_SUPPLIER.get().getClass().equals(JunctionTestProvider.class));
    }
}