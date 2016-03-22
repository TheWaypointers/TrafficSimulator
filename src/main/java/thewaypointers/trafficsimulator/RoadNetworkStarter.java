package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;

import java.util.function.Supplier;

public class RoadNetworkStarter {
    static final long VEHICLE_MOVEMENT_SPEED = 15;
    static final long TIME_STEP = 800;
    public static final Supplier<IStateProvider> PROVIDER_SUPPLIER = () -> new RoadNetworkProvider();

    public static void main(String[] args){
        new Bootstrapper(
                PROVIDER_SUPPLIER,
                TIME_STEP,
                VEHICLE_MOVEMENT_SPEED,
                null
        )
                .bootstrap(args);
    }
}
