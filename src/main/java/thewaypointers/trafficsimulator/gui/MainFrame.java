package thewaypointers.trafficsimulator.gui;
import java.awt.*;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.WorldStateDTO;

public class MainFrame extends JFrame{
    public static MapContainerPanel mapContainerPanel =null;
    public static MapPanel mapPanel=null;
    ControlPanel controlPanel = new ControlPanel();

    public MainFrame(){
        this(null);
    }

    public MainFrame(WorldStateDTO worldStateDTO){
        mapContainerPanel =  new MapContainerPanel();
        if (worldStateDTO != null){
            mapContainerPanel.mapPanel.processjunctionlocation(worldStateDTO);
            mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        }

        this.setLayout(null);
        mapContainerPanel.setBounds(0,0,600,600);
        controlPanel.setBounds(605,0,200,200);
        this.add(mapContainerPanel);
        this.setSize(800,600);

        this.setTitle("traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100, 0);
        this.setVisible(true);
        this.add(controlPanel);

    }

    public void  repaintpanel(WorldStateDTO worldStateDTO) {
        mapPanel.NewStateReceived(worldStateDTO);
        mapPanel.repaint();

    }

}

