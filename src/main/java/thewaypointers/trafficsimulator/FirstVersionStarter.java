package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldState;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class FirstVersionStarter {

    public static final float VEHICLE_MOVEMENT_SPEED = 15;
    public static final float STATES_PER_SECOND = 2;

    public static void main(String[] args){

        IStateProvider simulation = new RoadNetworkProvider();   // this will be simulation
        //IStateChangeListener gui = new SimpleStateChangeListener();   // put your GUI here


        //noinspection InfiniteLoopStatement
        while(true){
            WorldStateDTO newWorldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
            MainFrame.mapContainerPanel.mapPanel.NewStateReceived(newWorldState);
            try {
                Thread.sleep((long)(1f/STATES_PER_SECOND * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

