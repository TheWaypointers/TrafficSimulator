package thewaypointers.trafficsimulator.gui;

import sun.applet.Main;
import thewaypointers.trafficsimulator.JunctionLocationTestStarter;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainFrameEventHandle implements ComponentListener {

    private MainFrame processComponent(Component c){
        if (!c.getClass().equals(MainFrame.class)){
            throw new AssertionError(String.format(
                    "%s being used for %s instead of %s",
                    getClass().getName(),
                    c.getClass().getName(),
                    MainFrame.class.getName()
            ));
        }
        return (MainFrame) c;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        MainFrame mainFrame = processComponent(e.getComponent());
        mainFrame.setLayout(null);
        mainFrame.mapContainerPanel.setBounds(0,0, mainFrame.getWidth()-200, mainFrame.getHeight()-100);
        mainFrame.controlPanel.setBounds(mainFrame.getWidth()-200,0,200, mainFrame.getHeight());
        //mainFrame.timeLabelPanel.setBounds(0, mainFrame.getHeight()-60, mainFrame.getWidth()-200,60);
        mainFrame.statisticsPanel.setBounds(0, mainFrame.getHeight()-100, mainFrame.getWidth()-200, 100);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        processComponent(e.getComponent());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        processComponent(e.getComponent());
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        processComponent(e.getComponent());
    }
}
