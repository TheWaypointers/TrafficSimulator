package thewaypointers.trafficsimulator.gui;
import java.awt.*;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.WorldStateDTO;

public class MainFrame extends JFrame{

    public static MapPanel mapPanel=null;
    //ControlPanel controlPanel = new ControlPanel();

    public MainFrame(WorldStateDTO worldStateDTO){
        mapContainerPanel =  new MapContainerPanel();
        mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        this.setSize(800,600);
        this.setVisible(true);
        this.add(mapContainerPanel);
        this.setTitle("traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(200, 200);

        //this.add(controlPanel);

    }

    public void  repaintpanel(WorldStateDTO worldStateDTO) {
        mapPanel.NewStateReceived(worldStateDTO);
        mapPanel.repaint();

    }

}

