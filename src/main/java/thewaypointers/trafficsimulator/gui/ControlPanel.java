package thewaypointers.trafficsimulator.gui;

import thewaypointers.trafficsimulator.StateProviderController;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;


public class ControlPanel extends JPanel {
    public static final Color VEHICLE_CarNormal_COLOR = Color.blue;
    public static final Color VEHICLE_CarCautious_COLOR = Color.yellow;
    public static final Color VEHICLE_EmergencyService_COLOR = Color.black;
    public static final Color VEHICLE_Bus_COLOR = Color.cyan;
    public static final Color VEHICLE_CarReckless_COLOR = Color.red;
    public static final int VEHICLE_WIDTH = 8;
    public static final int VEHICLE_HEIGHT = 16;

    private JLabel worldStateLabel = new JLabel();
    private JButton startPauseButton = new JButton();
    private JButton clearButton = new JButton();
    private JLabel simulationSpeedLabel = new JLabel();
    private JSlider simulationSpeedSlider = new JSlider();
    private JLabel statesPerSecondsLabel = new JLabel();
    private JSlider statesPerSecondsSlider = new JSlider();
    private JLabel debugLabel = new JLabel();
    private JCheckBox debugCheckBox = new JCheckBox();


    private JLabel vehiclesLabel = new JLabel();
    private JLabel cautionCarLabel = new JLabel();
    public JTextField cautionCarsNumberTextField = new JTextField();
    private JLabel normalCarLabel = new JLabel();
    public JTextField normalCarsNumberTextField = new JTextField();
    private JLabel recklessCarLabel = new JLabel();
    public JTextField reckCarsNumberTextField = new JTextField();
    private JLabel ambulanceLabel = new JLabel();
    public JTextField ambulanceTextField = new JTextField();
    private JButton submitButton = new JButton();
    private JLabel totalVehiclesLabel = new JLabel();
    private JSlider totalVehiclesSlider = new JSlider();


    private JLabel trafficLightLabel = new JLabel();
    private JLabel trafficLightTimeLabel = new JLabel();
    private JSlider trafficLightTimeSlider = new JSlider(0, 100);

    public JLabel timer = new JLabel();

    Graphics2D g2;

    ChangeListener simulationSpeedChange = e -> {
        double tmp = simulationSpeedSlider.getValue();
        MainFrame.simulationInputListener.SimulationParameterChanged("simulationSpeed", Double.toString(tmp / 100));
    };

    ChangeListener statesPerSecondsChange = e -> {
        int tmp = statesPerSecondsSlider.getValue();
        MainFrame.simulationInputListener.SimulationParameterChanged("timeStepSlider", Integer.toString(tmp));
    };

    ChangeListener debugChange = e -> {
        Boolean debug = debugCheckBox.isSelected();
        MainFrame.mapContainerPanel.mapPanel.SetDebug(debug);
    };

    ChangeListener totalVehiclesChange = e -> {
        int tmp = totalVehiclesSlider.getValue();
        MainFrame.simulationInputListener.SimulationParameterChanged("totalVehicles", Integer.toString(tmp));
    };


    ChangeListener trafficLightTimeChange = e -> {
        int tmp = trafficLightTimeSlider.getValue();
        MainFrame.simulationInputListener.SimulationParameterChanged("timeStepSlider", Integer.toString(tmp));
    };

    private StateProviderController stateProviderController;

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
        Line2D.Float line2 = new Line2D.Float(0, 240, 200, 240);
        Line2D.Float line3 = new Line2D.Float(0, 275, 200, 275);
        Line2D.Float line4 = new Line2D.Float(0, 520, 200, 520);
        Line2D.Float line5 = new Line2D.Float(0, 555, 200, 555);
        Line2D.Float line6 = new Line2D.Float(0, 600, 200, 600);
        g2.draw(line2);
        g2.draw(line4);
        g2.draw(line6);
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(line1);
        g2.draw(line3);
        g2.draw(line5);

        Rectangle2D.Float cautionCar = new Rectangle2D.Float(100, 295, VEHICLE_HEIGHT, VEHICLE_WIDTH);
        g2.setColor(VEHICLE_CarCautious_COLOR);
        g2.fill(cautionCar);

        Rectangle2D.Float normalCar = new Rectangle2D.Float(100, 335, VEHICLE_HEIGHT, VEHICLE_WIDTH);
        g2.setColor(VEHICLE_CarNormal_COLOR);
        g2.fill(normalCar);

        Rectangle2D.Float reckCar = new Rectangle2D.Float(100, 375, VEHICLE_HEIGHT, VEHICLE_WIDTH);
        g2.setColor(VEHICLE_CarReckless_COLOR);
        g2.fill(reckCar);

        Rectangle2D.Float ambulance =new Rectangle2D.Float(100,415,VEHICLE_HEIGHT,VEHICLE_WIDTH);
        g2.setColor(VEHICLE_EmergencyService_COLOR);
        g2.fill(ambulance);
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

        initLabel(simulationSpeedLabel);
        simulationSpeedLabel.setText("Speed");
        simulationSpeedLabel.setLocation(22, 120);

        initSlider(simulationSpeedSlider);
        simulationSpeedSlider.setLocation(80, 120);
        simulationSpeedSlider.setValue(25);
        simulationSpeedSlider.setMaximum(1600);
        simulationSpeedSlider.setMajorTickSpacing(800);
        statesPerSecondsSlider.setMinorTickSpacing(100);
        simulationSpeedSlider.addChangeListener(simulationSpeedChange);

        initLabel(statesPerSecondsLabel);
        statesPerSecondsLabel.setText("States/s");
        statesPerSecondsLabel.setLocation(22, 160);

        initSlider(statesPerSecondsSlider);
        statesPerSecondsSlider.setLocation(80, 160);
        statesPerSecondsSlider.setMaximum(20);
        statesPerSecondsSlider.setMinimum(0);
        statesPerSecondsSlider.setValue(0);
        statesPerSecondsSlider.setMajorTickSpacing(5);
        statesPerSecondsSlider.setMinorTickSpacing(1);
        statesPerSecondsSlider.addChangeListener(statesPerSecondsChange);
        statesPerSecondsSlider.setSnapToTicks(true);

        initLabel(debugLabel);
        debugLabel.setText("Debug");
        debugLabel.setLocation(22, 200);

        debugCheckBox.setVisible(true);
        debugCheckBox.setSize(40, 40);
        debugCheckBox.setLocation(80, 200);
        debugCheckBox.addChangeListener(debugChange);


        initTitle(vehiclesLabel);
        vehiclesLabel.setText("Vehicles percentage");
        vehiclesLabel.setLocation(10, 240);

        initLabel(cautionCarLabel);
        cautionCarLabel.setText("Caution car");
        cautionCarLabel.setLocation(22, 280);

        initTextField(cautionCarsNumberTextField);
        cautionCarsNumberTextField.setLocation(120, 280);
        Document cautionCars = cautionCarsNumberTextField.getDocument();
        cautionCars.addDocumentListener(new ControlPanelDocumentListener(cautionCarsNumberTextField, this));


        initLabel(normalCarLabel);
        normalCarLabel.setText("Normal car");
        normalCarLabel.setLocation(22, 320);

        initTextField(normalCarsNumberTextField);
        normalCarsNumberTextField.setLocation(120, 320);
        Document normalCars = normalCarsNumberTextField.getDocument();
        normalCars.addDocumentListener(new ControlPanelDocumentListener(normalCarsNumberTextField, this));

        initLabel(recklessCarLabel);
        recklessCarLabel.setText("Reckless car");
        recklessCarLabel.setLocation(22, 360);

        initTextField(reckCarsNumberTextField);
        reckCarsNumberTextField.setLocation(120, 360);
        Document reckCars = reckCarsNumberTextField.getDocument();
        reckCars.addDocumentListener(new ControlPanelDocumentListener(reckCarsNumberTextField, this));

        initLabel(ambulanceLabel);
        ambulanceLabel.setText("Ambulance");
        ambulanceLabel.setLocation(22, 400);

        initTextField(ambulanceTextField);
        ambulanceTextField.setLocation(120, 400);
        Document ambulances = ambulanceTextField.getDocument();
        ambulances.addDocumentListener(new ControlPanelDocumentListener(ambulanceTextField, this));

        initButton(submitButton);
        submitButton.setText("Set percentage");
        submitButton.setLocation(0, 440);
        submitButton.addActionListener(this::submitPerformed);

        initLabel(totalVehiclesLabel);
        totalVehiclesLabel.setText("Total Num");
        totalVehiclesLabel.setLocation(22, 480);

        initSlider(totalVehiclesSlider);
        totalVehiclesSlider.setLocation(80, 480);
        totalVehiclesSlider.setMaximum(100);
        totalVehiclesSlider.setMinimum(0);
        totalVehiclesSlider.setValue(0);
        totalVehiclesSlider.setMajorTickSpacing(20);
        totalVehiclesSlider.setMinorTickSpacing(10);
        totalVehiclesSlider.addChangeListener(totalVehiclesChange);
        totalVehiclesSlider.setSnapToTicks(true);

        initTitle(trafficLightLabel);
        trafficLightLabel.setText("Traffic light");
        trafficLightLabel.setLocation(10, 520);

        initLabel(trafficLightTimeLabel);
        trafficLightTimeLabel.setText("Flip Interval");
        trafficLightTimeLabel.setLocation(22, 560);

        initSlider(trafficLightTimeSlider);
        trafficLightTimeSlider.setLocation(80, 560);
        trafficLightTimeSlider.setMinimum(0);
        trafficLightTimeSlider.setMaximum(20);
        trafficLightTimeSlider.setMajorTickSpacing(5);
        trafficLightTimeSlider.setMinorTickSpacing(2);
        trafficLightTimeSlider.addChangeListener(trafficLightTimeChange);
        trafficLightTimeSlider.setSnapToTicks(true);

        initTitle(timer);
        timer.setText("Simulation Time: ");
        timer.setLocation(10, 600);

        this.add(worldStateLabel);
        this.add(startPauseButton);
        this.add(clearButton);
        this.add(simulationSpeedLabel);
        this.add(simulationSpeedSlider);
        this.add(statesPerSecondsLabel);
        this.add(statesPerSecondsSlider);
        this.add(debugLabel);
        this.add(debugCheckBox);
        this.add(vehiclesLabel);
        this.add(cautionCarLabel);
        this.add(cautionCarsNumberTextField);
        this.add(normalCarLabel);
        this.add(normalCarsNumberTextField);
        this.add(recklessCarLabel);
        this.add(reckCarsNumberTextField);
        this.add(ambulanceLabel);
        this.add(ambulanceTextField);
        this.add(submitButton);
        this.add(totalVehiclesLabel);
        this.add(totalVehiclesSlider);
        this.add(trafficLightLabel);
        this.add(trafficLightTimeLabel);
        this.add(trafficLightTimeSlider);
        this.add(timer);
    }


    private void initButton(JButton button) {
        button.setBackground(null);
        button.setSize(200, 38);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setVisible(true);
        button.setFont(new Font("Arial", 0, 12));
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
        });
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
        if (stateProviderController == null) {
            System.out.println("Warning: no StateProviderController hooked up");
            return;
        }
        startPauseButton.setText(startPauseButton.getText().equals("Pause") ? "Start" : "Pause");
        stateProviderController.pauseSimulation();
    }

    private void clearPerformed(ActionEvent evt) {
        if (stateProviderController == null) {
            System.out.println("Warning: no StateProviderController hooked up");
            return;
        }
        startPauseButton.setText("Start");
        stateProviderController.clearSimulation();
        if (MapPanel.STATISTICS_Normal_INFORMATION.size() > 0) {
            MapPanel.STATISTICS_Normal_INFORMATION.clear();
        }
        if (MapPanel.STATISTICS_Reckless_INFORMATION.size() > 0) {
            MapPanel.STATISTICS_Reckless_INFORMATION.clear();
        }
        if (MapPanel.STATISTICS_Cautious_INFORMATION.size() > 0) {
            MapPanel.STATISTICS_Cautious_INFORMATION.clear();
        }
        if (MapPanel.STATISTICS_EmergencyService_INFORMATION.size() > 0) {
            MapPanel.STATISTICS_EmergencyService_INFORMATION.clear();
        }
    }

    private void submitPerformed(ActionEvent evt) {

        String cautionCarPercentage = cautionCarsNumberTextField.getText();
        String normalCarPercentage = normalCarsNumberTextField.getText();
        String recklessCarPercentage = reckCarsNumberTextField.getText();
        String ambulancePercentage = ambulanceTextField.getText();

        if (cautionCarPercentage.equals("")
                || normalCarPercentage.equals("")
                || recklessCarPercentage.equals("")
                || ambulancePercentage.equals("")) {
            new JumpOutDialog("Textfeild can't be empty!");
        } else {
            int cautionCar = Integer.parseInt(cautionCarPercentage);
            int normalCar = Integer.parseInt(normalCarPercentage);
            int reckCar = Integer.parseInt(recklessCarPercentage);
            int ambulance = Integer.parseInt(ambulancePercentage);
            int total = cautionCar + normalCar + reckCar + ambulance;

            if (total < 100) {
                new JumpOutDialog("The total percentage is smaller than 100!");
            } else if (total > 100) {
                new JumpOutDialog("The total percentage is greater than 100!");
            } else {
                MainFrame.simulationInputListener.SimulationParameterChanged("cautionCarPercentage", cautionCarPercentage);
                MainFrame.simulationInputListener.SimulationParameterChanged("normalCarPercentage", normalCarPercentage);
                MainFrame.simulationInputListener.SimulationParameterChanged("recklessCarPercentage", recklessCarPercentage);
                MainFrame.simulationInputListener.SimulationParameterChanged("ambulancePercentage", ambulancePercentage);
            }
        }

    }

    public void setStateProviderController(StateProviderController stateProviderController) {
        this.stateProviderController = stateProviderController;
    }
}
