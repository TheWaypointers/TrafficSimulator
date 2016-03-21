package thewaypointers.trafficsimulator.gui;
import thewaypointers.trafficsimulator.SimulationController;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.event.ActionEvent;


public class ControlPanel extends JPanel {

    //private JSplitPane worldControlPanel = new JSplitPane();
    private JLabel worldStateLabel = new JLabel();
    private JButton startPauseButton = new JButton();
    // private JSeparator firstLevelSep2 = new JSeparator();
    private JButton clearButton = new JButton();
    //private JSeparator firstLevelSep3 = new JSeparator();
    private JLabel timeStepLabel = new JLabel();
    private JSlider timeStepSlider = new JSlider(0, 100);
    //private JSeparator secondLevelSep1 = new JSeparator();

    //private JSplitPane vehicleControlPanel = new JSplitPane();
    private JLabel vehiclesLabel = new JLabel();
    private JLabel cautionCarLabel = new JLabel();
    private JTextField cautionCarsNumberTextField = new JTextField();
    private JLabel normalCarLabel = new JLabel();
    private JTextField normalCarsNumberTextField = new JTextField();
    private JLabel recklessCarLabel = new JLabel();
    private JTextField reckCarsNumberTextField = new JTextField();
    private JLabel busLabel = new JLabel();
    private JTextField busNumberTextField = new JTextField();
    private JLabel ambulanceLabel = new JLabel();
    private JTextField ambulanceTextField = new JTextField();
    private JButton submitButton = new JButton();

    //private JSplitPane trafficLightControlPanel = new JSplitPane();
    private JLabel trafficLightLabel = new JLabel();
    private JLabel trafficLightTimeLabel = new JLabel();
    private JSlider trafficLightTimeSlider = new JSlider(0, 100);

    Graphics2D g2;

    ChangeListener timeStepChange = e -> {
        int tmp = timeStepSlider.getValue();
        //System.out.println("time step: "+tmp);
    };

    ChangeListener trafficLightTimeChange = e -> {
        int tmp = trafficLightTimeSlider.getValue();
        //System.out.println("traffic light time: "+tmp);
    };

    private SimulationController simulationController;

    public ControlPanel() {
        this.setLayout(null);
        this.setBackground(Color.white);
        this.setVisible(true);
        this.setSize(200, 600);
        this.initComponents();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        Line2D.Float line1 = new Line2D.Float(0, 35, 200, 35);
        Line2D.Float line2 = new Line2D.Float(0, 160, 200, 160);
        Line2D.Float line3 = new Line2D.Float(0, 195, 200, 195);
        Line2D.Float line4 = new Line2D.Float(0, 440, 200, 440);
        Line2D.Float line5 = new Line2D.Float(0, 475, 200, 475);

        g2.draw(line2);
        g2.draw(line4);

        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(line1);
        g2.draw(line3);
        g2.draw(line5);
    }

    private void initComponents() {
        initTitle(worldStateLabel);
        worldStateLabel.setText("World");
        worldStateLabel.setLocation(10, 0);

        initButton(startPauseButton);
        startPauseButton.setText("Start");
        startPauseButton.setLocation(0, 40);
        startPauseButton.addActionListener(this::startPausePerformed);

        initButton(clearButton);
        clearButton.setText("Clear");
        clearButton.setLocation(0, 80);
        clearButton.addActionListener(this::clearPerformed);

        initLabel(timeStepLabel);
        timeStepLabel.setText("Time step");
        timeStepLabel.setLocation(22, 120);

        initSlider(timeStepSlider);
        timeStepSlider.setLocation(80, 120);
        timeStepSlider.setMajorTickSpacing(50);
        timeStepSlider.setMinorTickSpacing(10);
        timeStepSlider.addChangeListener(timeStepChange);


        initTitle(vehiclesLabel);
        vehiclesLabel.setText("Vehicles number");
        vehiclesLabel.setLocation(10, 160);

        initLabel(cautionCarLabel);
        cautionCarLabel.setText("Caution car");
        cautionCarLabel.setLocation(22, 200);

        initTextField(cautionCarsNumberTextField);
        cautionCarsNumberTextField.setLocation(120, 200);

        initLabel(normalCarLabel);
        normalCarLabel.setText("Normal car");
        normalCarLabel.setLocation(22, 240);

        initTextField(normalCarsNumberTextField);
        normalCarsNumberTextField.setLocation(120, 240);

        initLabel(recklessCarLabel);
        recklessCarLabel.setText("Reckless car");
        recklessCarLabel.setLocation(22, 280);

        initTextField(reckCarsNumberTextField);
        reckCarsNumberTextField.setLocation(120, 280);

        initLabel(busLabel);
        busLabel.setText("Bus");
        busLabel.setLocation(22, 320);

        initTextField(busNumberTextField);
        busNumberTextField.setLocation(120, 320);

        initLabel(ambulanceLabel);
        ambulanceLabel.setText("Ambulance");
        ambulanceLabel.setLocation(22, 360);

        initTextField(ambulanceTextField);
        ambulanceTextField.setLocation(120, 360);

        initButton(submitButton);
        submitButton.setText("Add vehicles");
        submitButton.setLocation(0, 400);
        submitButton.addActionListener(this::submitPerformed);

        initTitle(trafficLightLabel);
        trafficLightLabel.setText("Traffic light");
        trafficLightLabel.setLocation(10, 440);

        initLabel(trafficLightTimeLabel);
        trafficLightTimeLabel.setText("Flip Interval");
        trafficLightTimeLabel.setLocation(22, 480);

        initSlider(trafficLightTimeSlider);
        trafficLightTimeSlider.setLocation(80, 480);
        trafficLightTimeSlider.setMajorTickSpacing(20);
        trafficLightTimeSlider.setMinorTickSpacing(10);
        trafficLightTimeSlider.addChangeListener(trafficLightTimeChange);

        this.add(worldStateLabel);
        this.add(startPauseButton);
        this.add(clearButton);
        this.add(timeStepLabel);
        this.add(timeStepSlider);
        this.add(vehiclesLabel);
        this.add(cautionCarLabel);
        this.add(cautionCarsNumberTextField);
        this.add(normalCarLabel);
        this.add(normalCarsNumberTextField);
        this.add(recklessCarLabel);
        this.add(reckCarsNumberTextField);
        this.add(busLabel);
        this.add(busNumberTextField);
        this.add(ambulanceLabel);
        this.add(ambulanceTextField);
        this.add(submitButton);
        this.add(trafficLightLabel);
        this.add(trafficLightTimeLabel);
        this.add(trafficLightTimeSlider);


    }


    private void initButton(JButton button) {
        button.setBackground(null);
        button.setSize(200, 38);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setVisible(true);
        button.setFont(new Font("Arial", 0, 12));
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void initLabel(JLabel label) {
        label.setBackground(null);
        label.setVisible(true);
        label.setSize(80, 38);
        label.setFont(new Font("Arial", 0, 12));

    }

    private void initTitle(JLabel title) {
        title.setBackground(null);
        title.setVisible(true);
        title.setSize(200, 38);
        title.setFont(new Font("Arial", 0, 16));
        //title.setVerticalAlignment(SwingConstants.LEFT);
    }

    private void initSlider(JSlider slider) {
        slider.setSize(120, 38);
        slider.setVisible(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    private void initTextField(JTextField textField) {
        textField.setText("0");
        textField.setSize(60, 30);
        textField.setVisible(true);
        textField.setFont(new Font("Arial", 0, 12));
    }

    private void startPausePerformed(ActionEvent evt) {
        if (simulationController == null){
            System.out.print("Warning: no SimulationController hooked up");
            return;
        }
        startPauseButton.setText(startPauseButton.getText().equals("Pause")? "Start": "Pause");
        simulationController.pauseSimulation();
    }

    private void clearPerformed(ActionEvent evt) {
        if (simulationController == null){
            System.out.print("Warning: no SimulationController hooked up");
            return;
        }
        simulationController.clearSimulation();
    }

    private void submitPerformed(ActionEvent evt) {
        String cautionCarNum = cautionCarsNumberTextField.getText();
        String normalCarNum = normalCarsNumberTextField.getText();
        String recklessCarNum = reckCarsNumberTextField.getText();
        String busNum = busNumberTextField.getText();
        String ambulanceNum = ambulanceTextField.getText();

//        if(cautionCarsNumberTextField.equals(null)){
//
//        }

//        if(cautionCarNum.equals("0")){
//            new JumpOutDialog("test");
//        }
        if(cautionCarNum == null
                ||normalCarNum == null
                ||recklessCarNum == null
                ||busNum == null
                ||ambulanceNum == null){
            new JumpOutDialog("Textfeild can't be empty!");

        }

        //method for transfering these parameters

        cautionCarsNumberTextField.setText("0");
        normalCarsNumberTextField.setText("0");
        reckCarsNumberTextField.setText("0");
        busNumberTextField.setText("0");
        ambulanceTextField.setText("0");
    }

    public void setSimulationController(SimulationController simulationController) {
        this.simulationController = simulationController;
    }
}
