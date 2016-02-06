package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.SimpleStateChangeListener;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldStateProvider;

public class FirstVersionStarter {

    public static final float VEHICLE_MOVEMENT_SPEED = 15;
    public static final float STATES_PER_SECOND = 2;

    public static void main(String[] args){

        IStateChangeListener gui = new SimpleStateChangeListener();   // put your GUI here
        SimpleWorldStateProvider simulation = new SimpleWorldStateProvider();   // this will be simulation

        while(true){
            WorldStateDTO newWorldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
            gui.NewStateReceived(newWorldState);   // in this method the GUI draws the new world state
            try {
                Thread.sleep((long)(1f/STATES_PER_SECOND * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
