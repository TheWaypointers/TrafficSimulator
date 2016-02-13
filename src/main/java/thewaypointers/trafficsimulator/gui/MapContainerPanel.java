package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import java.awt.*;

//map can be dragged on this panel
public class MapContainerPanel extends JPanel{

    public MapPanel mapPanel;


    // configurable parameters
    public static final int MAP_CONTAINER_PANEL_WIDTH = 600;
    public static final int MAP_CONTAINER_PANEL_HEIGHT = 600;
    public static final Color BACKGROUND_COLOR = Color.white;

    public MapContainerPanel(){
        mapPanel = new MapPanel();
        this.setVisible(true);
        this.setSize(MAP_CONTAINER_PANEL_WIDTH, MAP_CONTAINER_PANEL_HEIGHT);
        this.setBackground(BACKGROUND_COLOR);
        this.add(mapPanel);
    }



}
