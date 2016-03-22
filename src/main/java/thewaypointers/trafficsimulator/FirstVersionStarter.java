package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.FirstVersionProvider;

public class FirstVersionStarter {

    static final long VEHICLE_MOVEMENT_SPEED = 500;
    static final long TIME_STEP = 500;
    public static final IStateProvider PROVIDER = new FirstVersionProvider();

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

