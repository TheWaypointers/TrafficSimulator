package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.FirstVersionProvider;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class FirstVersionStarter {

    static final long VEHICLE_MOVEMENT_SPEED = 500;
    static final long TIME_STEP = 500;
    static final IStateProvider PROVIDER = new FirstVersionProvider();

    public static void main(String[] args){
        new StarterBase(
                PROVIDER,
                TIME_STEP,
                VEHICLE_MOVEMENT_SPEED,
                null
        )
                .bootstrap(args);
    }
}

