package thewaypointers.trafficsimulator.gui;

import thewaypointers.trafficsimulator.JunctionLocationTestStarter;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainFrameEventHandle implements ComponentListener {
    @Override
    public void componentResized(ComponentEvent e) {
        JunctionLocationTestStarter.mainFrame.setLayout(null);
        MainFrame.mapContainerPanel.setBounds(0,0, JunctionLocationTestStarter.mainFrame.getWidth()-200, JunctionLocationTestStarter.mainFrame.getHeight());
        MainFrame.controlPanel.setBounds(JunctionLocationTestStarter.mainFrame.getWidth()-200,0,200, JunctionLocationTestStarter.mainFrame.getHeight());
        //JunctionLocationTestStarter.mainFrame.setVisible(true);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}