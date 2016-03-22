package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;

public class RoadNetworkStarter {
    static final long VEHICLE_MOVEMENT_SPEED = 15;
    static final long TIME_STEP = 800;
    public static final IStateProvider PROVIDER = new RoadNetworkProvider();

    public static void main(String[] args){
        new Bootstrapper(
                PROVIDER,
                TIME_STEP,
                VEHICLE_MOVEMENT_SPEED,
                null
        )
                .bootstrap(args);
    }
}
