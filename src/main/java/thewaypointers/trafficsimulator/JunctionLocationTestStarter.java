package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.utils.FloatPoint;

import static thewaypointers.trafficsimulator.TrafficSimulatorManager.*;

public class JunctionLocationTestStarter {

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;
    public static void main(String[] args){

        IStateProvider simulation = new JunctionTestProvider();
        MainFrame mainFrame=new MainFrame(simulation.getNextState(0));
        TrafficSimulatorManager.setMainFrame(mainFrame);
        TrafficSimulatorManager.setSimulation(simulation);

        //TrafficSimulatorManager.GetInstance().run();
        //noinspection InfiniteLoopStatement
//        while(true){
//            WorldStateDTO newWorldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
//            MainFrame.mapContainerPanel.mapPanel.NewStateReceived(newWorldState);
//            try {
//                Thread.sleep((long)(1f/STATES_PER_SECOND * 1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}