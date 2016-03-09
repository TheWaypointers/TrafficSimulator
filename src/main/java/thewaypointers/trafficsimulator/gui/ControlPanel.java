package thewaypointers.trafficsimulator.gui;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ControlPanel extends JPanel {


    //private JSplitPane worldControlPanel = new JSplitPane();
    private JLabel worldStateLable = new JLabel();
    private JButton startPauseButton = new JButton();
    // private JSeparator firstLevelSep2 = new JSeparator();
    private JButton clearButton = new JButton();
    //private JSeparator firstLevelSep3 = new JSeparator();
    private JLabel timeStepLable = new JLabel();
    private JSlider timeStepSlider = new JSlider(0, 100);
    //private JSeparator secondLevelSep1 = new JSeparator();

    //private JSplitPane vehicleControlPanel = new JSplitPane();
    private JLabel vehiclesLable = new JLabel();
    private JLabel cautionCarLable = new JLabel();
    private JTextField cautionCarsNumberTextField = new JTextField();
    private JLabel normalCarLable = new JLabel();
    private JTextField normalCarsNumberTextField = new JTextField();
    private JLabel recklessCarLable = new JLabel();
    private JTextField reckCarsNumberTextField = new JTextField();
    private JLabel busLable = new JLabel();
    private JTextField busNumberTextField = new JTextField();
    private JLabel ambulanceLable = new JLabel();
    private JTextField ambulanceTextField = new JTextField();
    private JButton submitButton = new JButton();

    //private JSplitPane trafficLightControlPanel = new JSplitPane();
    private JLabel trafficLightLable = new JLabel();
    private JLabel trafficLightTimeLable = new JLabel();
    private JSlider trafficLightTimeSlider = new JSlider(0, 100);

    Graphics2D g2;

    ChangeListener timeStepChange = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            int tmp = timeStepSlider.getValue();
            //System.out.println("time step: "+tmp);
        }
    };

    ChangeListener trafficLightTimeChange = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            int tmp = trafficLightTimeSlider.getValue();
            //System.out.println("traffic light time: "+tmp);
        }
    };


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
        initTitle(worldStateLable);
        worldStateLable.setText("World");
        worldStateLable.setLocation(10, 0);

        initButton(startPauseButton);
        startPauseButton.setText("Start");
        startPauseButton.setLocation(0, 40);
        startPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPausePerformed(evt);
            }
        });

        initButton(clearButton);
        clearButton.setText("Clear");
        clearButton.setLocation(0, 80);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearPerformed(evt);
            }
        });

        initLable(timeStepLable);
        timeStepLable.setText("Time step");
        timeStepLable.setLocation(22, 120);

        initSlider(timeStepSlider);
        timeStepSlider.setLocation(80, 120);
        timeStepSlider.setMajorTickSpacing(50);
        timeStepSlider.setMinorTickSpacing(10);
        timeStepSlider.addChangeListener(timeStepChange);


        initTitle(vehiclesLable);
        vehiclesLable.setText("Vehicles number");
        vehiclesLable.setLocation(10, 160);

        initLable(cautionCarLable);
        cautionCarLable.setText("Caution car");
        cautionCarLable.setLocation(22, 200);

        initTextField(cautionCarsNumberTextField);
        cautionCarsNumberTextField.setLocation(120, 200);

        initLable(normalCarLable);
        normalCarLable.setText("Normal car");
        normalCarLable.setLocation(22, 240);

        initTextField(normalCarsNumberTextField);
        normalCarsNumberTextField.setLocation(120, 240);

        initLable(recklessCarLable);
        recklessCarLable.setText("Reckless car");
        recklessCarLable.setLocation(22, 280);

        initTextField(reckCarsNumberTextField);
        reckCarsNumberTextField.setLocation(120, 280);

        initLable(busLable);
        busLable.setText("Bus");
        busLable.setLocation(22, 320);

        initTextField(busNumberTextField);
        busNumberTextField.setLocation(120, 320);

        initLable(ambulanceLable);
        ambulanceLable.setText("Ambulance");
        ambulanceLable.setLocation(22, 360);

        initTextField(ambulanceTextField);
        ambulanceTextField.setLocation(120, 360);

        initButton(submitButton);
        submitButton.setText("Add vehicles");
        submitButton.setLocation(0, 400);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                submitPerformed(evt);
            }
        });

        initTitle(trafficLightLable);
        trafficLightLable.setText("Traffic light");
        trafficLightLable.setLocation(10, 440);

        initLable(trafficLightTimeLable);
        trafficLightTimeLable.setText("Flip Interval");
        trafficLightTimeLable.setLocation(22, 480);

        initSlider(trafficLightTimeSlider);
        trafficLightTimeSlider.setLocation(80, 480);
        trafficLightTimeSlider.setMajorTickSpacing(20);
        trafficLightTimeSlider.setMinorTickSpacing(10);
        trafficLightTimeSlider.addChangeListener(trafficLightTimeChange);

        this.add(worldStateLable);
        this.add(startPauseButton);
        this.add(clearButton);
        this.add(timeStepLable);
        this.add(timeStepSlider);
        this.add(vehiclesLable);
        this.add(cautionCarLable);
        this.add(cautionCarsNumberTextField);
        this.add(normalCarLable);
        this.add(normalCarsNumberTextField);
        this.add(recklessCarLable);
        this.add(reckCarsNumberTextField);
        this.add(busLable);
        this.add(busNumberTextField);
        this.add(ambulanceLable);
        this.add(ambulanceTextField);
        this.add(submitButton);
        this.add(trafficLightLable);
        this.add(trafficLightTimeLable);
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

    private void initLable(JLabel lable) {
        lable.setBackground(null);
        lable.setVisible(true);
        lable.setSize(80, 38);
        lable.setFont(new Font("Arial", 0, 12));

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
        String action = startPauseButton.getText();
        if(action.equals("Start")){
            startPauseButton.setText("Pause");
            //method for start
        }
        if(action.equals("Pause")){
            startPauseButton.setText("Start");
            //method for pause
        }
    }

    private void clearPerformed(ActionEvent evt) {
        //method for stop
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
        if((cautionCarNum.equals(null))
                ||(normalCarNum.equals(null))
                ||(recklessCarNum.equals(null))
                ||(busNum.equals(null))
                || (ambulanceNum.equals(null))){
            new JumpOutDialog("Textfeild can't be empty!");

        }

        //method for transfering these parameters

        cautionCarsNumberTextField.setText("0");
        normalCarsNumberTextField.setText("0");
        reckCarsNumberTextField.setText("0");
        busNumberTextField.setText("0");
        ambulanceTextField.setText("0");
    }

}
