package thewaypointers.trafficsimulator.gui;

import thewaypointers.trafficsimulator.StateProviderController;
import thewaypointers.trafficsimulator.common.ISimulationInputListener;
import thewaypointers.trafficsimulator.common.SimulationInputListener;
import thewaypointers.trafficsimulator.common.WorldStateDTO;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    public static MapContainerPanel mapContainerPanel =null;
    public static ControlPanel controlPanel =  null;
    public static  JLabel timeLabelPanel =null;
    public static ISimulationInputListener simulationInputListener;

    public MainFrame(){

        this(null);
    }

    public MainFrame(WorldStateDTO worldStateDTO){
        controlPanel = new ControlPanel();
        mapContainerPanel =  new MapContainerPanel();
        timeLabelPanel =new JLabel("Simulation time: ");
        timeLabelPanel.setForeground(Color.black);
        timeLabelPanel.setOpaque(true);
        timeLabelPanel.setBackground(Color.white);
        timeLabelPanel.setFont(new Font("Arial",Font.PLAIN,15));
        timeLabelPanel.setSize(600,60);

        simulationInputListener = new SimulationInputListener();
        if (worldStateDTO != null){
            mapContainerPanel.mapPanel.processjunctionlocation(worldStateDTO);
            mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        }

        this.setLayout(null);
        mapContainerPanel.setBounds(0,0,600,540);
        controlPanel.setBounds(600,0,200,600);
        timeLabelPanel.setBounds(0,540,600,60);
        this.add(mapContainerPanel);
        this.add(controlPanel);
        this.add(timeLabelPanel);
        this.setSize(800,600);

        this.setTitle("traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100, 0);
        this.setVisible(true);

        this.addComponentListener(new MainFrameEventHandle());

    }

    public void setSimulationController(StateProviderController controller){
        controlPanel.setStateProviderController(controller);
    }

}

