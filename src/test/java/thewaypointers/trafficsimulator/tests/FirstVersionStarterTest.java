package thewaypointers.trafficsimulator.tests;

import org.junit.Test;
import thewaypointers.trafficsimulator.FirstVersionStarter;
import thewaypointers.trafficsimulator.common.helpers.FirstVersionProvider;

import static org.junit.Assert.*;

public class FirstVersionStarterTest {
    @Test
    public void providerTest(){
        assertTrue(FirstVersionStarter.PROVIDER_SUPPLIER.get().getClass().equals(FirstVersionProvider.class));
    }
}