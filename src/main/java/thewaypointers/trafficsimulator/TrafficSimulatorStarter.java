package thewaypointers.trafficsimulator;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.simulation.Simulation;

public class TrafficSimulatorStarter {
    final static long TIME_STEP = 100;
    final static long SIMULATION_TIME_STEP = 300;
    static WorldStateDTO worldState;
    static MainFrame mainFrame;

    public static void main(String[] args) {

        Simulation simulation = new Simulation();
        mainFrame = new MainFrame();

        while(true){
            try{
                Thread.sleep(TIME_STEP);
                worldState = simulation.getNextSimulationStep(SIMULATION_TIME_STEP);
                MainFrame.mapContainerPanel.mapPanel.NewStateReceived(worldState);
            }
            catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
