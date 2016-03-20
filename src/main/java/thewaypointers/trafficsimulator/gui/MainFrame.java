package thewaypointers.trafficsimulator.gui;

import thewaypointers.trafficsimulator.TrafficSimulatorManager;
import thewaypointers.trafficsimulator.common.SimulationInputListener;
import thewaypointers.trafficsimulator.common.WorldStateDTO;

import javax.swing.*;

public class MainFrame extends JFrame{
    public static MapContainerPanel mapContainerPanel =null;
    public static ControlPanel controlPanel =  null;
    public static SimulationInputListener simulationInputListener;

    public MainFrame(){

        this(null,null);
    }

    public MainFrame(WorldStateDTO worldStateDTO, TrafficSimulatorManager trafficSimulatorManager){
        controlPanel = new ControlPanel(trafficSimulatorManager);
        mapContainerPanel =  new MapContainerPanel();
        simulationInputListener = new SimulationInputListener();
        if (worldStateDTO != null){
            mapContainerPanel.mapPanel.processjunctionlocation(worldStateDTO);
            mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        }

        this.setLayout(null);
        mapContainerPanel.setBounds(0,0,600,600);
        controlPanel.setBounds(600,0,200,600);
        this.add(mapContainerPanel);
        this.add(controlPanel);
        this.setSize(800,600);

        this.setTitle("traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100, 0);
        this.setVisible(true);

        this.addComponentListener(new MainFrameEventHandle());

    }

    public void updateMainFrame(WorldStateDTO worldStateDTO, TrafficSimulatorManager trafficSimulatorManager){
        controlPanel = new ControlPanel(trafficSimulatorManager);
        mapContainerPanel =  new MapContainerPanel();
        if (worldStateDTO != null){
            mapContainerPanel.mapPanel.processjunctionlocation(worldStateDTO);
            mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        }
    }

}

