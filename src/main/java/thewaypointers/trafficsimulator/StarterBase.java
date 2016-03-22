package thewaypointers.trafficsimulator;


import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class StarterBase {
    private IStateProvider stateProvider;
    private long timeStep;
    private long simulationTimeStep;
    private WorldStateDTO initialWorldState;

    public StarterBase(IStateProvider stateProvider,
                       long timeStep,
                       long simulationTimeStep,
                       WorldStateDTO initialWorldState){
        this.stateProvider = stateProvider;
        this.timeStep = timeStep;
        this.simulationTimeStep = simulationTimeStep;
        this.initialWorldState = initialWorldState;
    }

    public void bootstrap(String[] args){
        SimulationController simulationController = new SimulationController(timeStep, simulationTimeStep);
        simulationController.start();
        MainFrame mainFrame=new MainFrame(initialWorldState);
        mainFrame.setSimulationController(simulationController);
        simulationController.setOutput(mainFrame.mapContainerPanel.mapPanel);
        simulationController.setSimulation(stateProvider);
    }
}
