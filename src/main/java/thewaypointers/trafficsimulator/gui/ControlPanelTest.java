package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import java.awt.*;

public class ControlPanelTest extends JFrame{

    ControlPanel cp = new ControlPanel();

    public ControlPanelTest(){
        this.setBackground(Color.LIGHT_GRAY);
        this.setSize(600,600);
        this.setLocation(0, 0);
        this.setVisible(true);
        this.add(cp);
    }

    public static void main(String[] args){

        ControlPanelTest tm = new ControlPanelTest();
    }
}

