package thewaypointers.trafficsimulator.gui;
import java.awt.*;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.WorldStateDTO;

public class MainFrame extends JFrame{

    public static MapPanel mapPanel=null;
    //ControlPanel controlPanel = new ControlPanel();



    public MainFrame(WorldStateDTO worldStateDTO){
        mapPanel  =  new MapPanel();
        mapPanel.getWorldState(worldStateDTO);
        this.setSize(800,600);
        this.setVisible(true);
        this.add(mapPanel);
        this.setTitle("traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(200, 200);

        //this.add(controlPanel);

    }

    public void  repaintpanel(WorldStateDTO worldStateDTO) {
        mapPanel.getWorldState(worldStateDTO);
        mapPanel.repaint();

    }

}

