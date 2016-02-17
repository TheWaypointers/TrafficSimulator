package thewaypointers.trafficsimulator.gui;
import java.awt.*;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.WorldStateDTO;

public class MainFrame extends JFrame{

    public static MapContainerPanel mapContainerPanel =null;
    //ControlPanel controlPanel = new ControlPanel();

    public MainFrame(WorldStateDTO worldStateDTO){
        mapPanel  =  new MapPanel();
        mapPanel.NewStateReceived(worldStateDTO);
        mapPanel.setPreferredSize(new Dimension(1000,1000));
        JScrollPane jScrollPane=new JScrollPane(mapPanel);
        jScrollPane.setViewportView(mapPanel);

        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(jScrollPane);
        this.setSize(1200,1200);

        mapContainerPanel =  new MapContainerPanel();
        mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        this.setSize(800,600);
        this.setVisible(true);
        this.add(mapContainerPanel);
        this.setTitle("traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100, 0);
        this.setVisible(true);
        //this.add(controlPanel);

    }


    public void  repaintpanel(WorldStateDTO worldStateDTO) {
        mapContainerPanel.mapPanel.NewStateReceived(worldStateDTO);
        mapContainerPanel.mapPanel.repaint();

    }



}

