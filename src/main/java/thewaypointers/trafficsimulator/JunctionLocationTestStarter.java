package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class JunctionLocationTestStarter {

    static final long VEHICLE_MOVEMENT_SPEED = 2;
    static final long TIME_STEP = 800;
    static final IStateProvider PROVIDER = new JunctionTestProvider();

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