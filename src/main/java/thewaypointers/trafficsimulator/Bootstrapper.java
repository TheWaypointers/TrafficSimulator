package thewaypointers.trafficsimulator;


import thewaypointers.trafficsimulator.common.IStateProvider;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.gui.MainFrame;

public class Bootstrapper {
    private IStateProvider stateProvider;
    private long timeStep;
    private long simulationTimeStep;
    private WorldStateDTO initialWorldState;

    public Bootstrapper(IStateProvider stateProvider,
                        long timeStep,
                        long simulationTimeStep,
                        WorldStateDTO initialWorldState){
        this.stateProvider = stateProvider;
        this.timeStep = timeStep;
        this.simulationTimeStep = simulationTimeStep;
        this.initialWorldState = initialWorldState;
    }

    public void bootstrap(String[] args){
        StateProviderController stateProviderController = new StateProviderController(timeStep, simulationTimeStep);
        stateProviderController.start();
        MainFrame mainFrame=new MainFrame(initialWorldState);
        mainFrame.setSimulationController(stateProviderController);
        stateProviderController.setOutput(mainFrame.mapContainerPanel.mapPanel);
        stateProviderController.setSimulation(stateProvider);
    }
}
