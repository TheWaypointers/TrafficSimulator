package thewaypointers.trafficsimulator;
import javax.swing.JButton;

import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.helpers.JunctionTestProvider;
import thewaypointers.trafficsimulator.common.helpers.RoadNetworkProvider;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class TrafficSimulatorManager extends Thread {

    public static final float VEHICLE_MOVEMENT_SPEED = 2;
    public static final float STATES_PER_SECOND = 1;
    private static MainFrame mainFrame;
    private static IStateProvider simulation;
    private WorldStateDTO worldState;
    private boolean stop = true;
    private boolean isSleep = true;



    private JButton start_pauseButton;

    public TrafficSimulatorManager() {
    }


    public static void setSimulation(IStateProvider simulation) {
        TrafficSimulatorManager.simulation = simulation;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        TrafficSimulatorManager.mainFrame = mainFrame;
    }

    public void setStart_pauseButton(JButton start_pauseButton) {
        this.start_pauseButton = start_pauseButton;
    }
    public TrafficSimulatorManager(JButton start_pause){
        start_pauseButton = start_pause;
    }



    public synchronized void changeState() {
        isSleep = !isSleep;
        start_pauseButton.setText("Start");
    }

    public synchronized void stopState() {
        stop = !stop;
        simulation = new RoadNetworkProvider();
        mainFrame.setVisible(false);
        mainFrame = new MainFrame(simulation.getNextState(0),new TrafficSimulatorManager());
    }

    public void run() {
        while (true) {
            if (stop) {
                if (!isSleep) {
                    //System.out.println("test");
                    start_pauseButton.setText("Pause");
                    worldState = simulation.getNextState(VEHICLE_MOVEMENT_SPEED);
                    mainFrame.mapContainerPanel.mapPanel.NewStateReceived(worldState);
                }
                try {
                    Thread.sleep((long) (1f / STATES_PER_SECOND * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
