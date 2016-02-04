package thewaypointers.trafficsimulator.gui;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame{

    MapPanel mapPanel =  new MapPanel();
    ControlPanel controlPanel = new ControlPanel();



    public MainFrame(){
        this.setSize(800,600);
        this.setVisible(true);
        this.add(mapPanel);
        this.add(controlPanel);

    }


    public static void main(String[] args){
        MainFrame test = new MainFrame();

    }

}

