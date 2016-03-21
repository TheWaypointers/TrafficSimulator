package thewaypointers.trafficsimulator;

import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class SimulationController extends Thread {

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;
    private static IStateChangeListener output;
    private static IStateProvider simulation;
    private WorldStateDTO worldState;
    private boolean isSleep = true;

    public SimulationController() {
    }

    public static void setSimulation(IStateProvider simulation) {
        SimulationController.simulation = simulation;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        output = mainFrame.mapContainerPanel.mapPanel;
    }

    public synchronized void pauseSimulation() {
        isSleep = !isSleep;
    }

    public synchronized void clearSimulation() {
        isSleep = true;
        simulation = new JunctionTestProvider();
//        mainFrame.setVisible(false);
//        mainFrame = new MainFrame(simulation.getNextState(0),new SimulationController());
    }

    public void run() {
        while (true) {
            if (!isSleep) {
                worldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
                output.NewStateReceived(worldState);
            }
            try {
                Thread.sleep((long) (1f / STATES_PER_SECOND * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
