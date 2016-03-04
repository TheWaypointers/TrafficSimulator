package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldState;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldStateProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.utils.FloatPoint;

public class JunctionLocationTestStarter {

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;

    public static void main(String[] args){

        SimpleWorldStateProvider simulation = new SimpleWorldStateProvider(SimpleWorldState.JUNCTION_LOCATION_TEST);
        MainFrame mainFrame=new MainFrame(simulation.getNextState(0));

        //noinspection InfiniteLoopStatement
        while(true){
            WorldStateDTO newWorldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
            JunctionLocationDTO loc = (JunctionLocationDTO) newWorldState
                    .getVehicleList().getVehicle("V1").getLocation();
            FloatPoint coords = loc.getJunctionCoordinates();
            System.out.println(String.format(
                    "Vehicle coordinates: (%f, %f), angle: %f",
                    coords.getX(),
                    coords.getY(),
                    loc.getAngle()));
            MainFrame.mapContainerPanel.mapPanel.NewStateReceived(newWorldState);
            try {
                Thread.sleep((long)(1f/STATES_PER_SECOND * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}