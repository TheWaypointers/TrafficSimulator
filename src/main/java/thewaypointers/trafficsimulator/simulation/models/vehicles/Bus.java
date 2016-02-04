package thewaypointers.trafficsimulator.simulation.models.vehicles;

import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;

import java.util.Stack;

/**
 * Created by User on 4.2.2016..
 */
public class Bus implements IVehicle {

    private float topSpeed;
    private float currentSpeed;
    private VehicleType vehicleType;
    private float distanceTravelled;
    private Stack<String> decisionPath;
    private String originNode;
    private Lane lane;

    private final long SPEED_DIFFERENCE = 10;

    //same constructor as in Car
    public Bus(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, String originNode, Lane lane){
        this.vehicleType = VehicleType.Bus;
        this.decisionPath = decisionPath;
        this.originNode = originNode;
        this.lane = lane;
        calculateVehicleSpeed(roadSpeedLimit);
    }

    //constructor without VehicleType
    public Bus(float roadSpeedLimit, Stack<String> decisionPath, String originNode, Lane lane){
        this.vehicleType = VehicleType.Bus;
        this.decisionPath = decisionPath;
        this.originNode = originNode;
        this.lane = lane;
        calculateVehicleSpeed(roadSpeedLimit);
    }



    public float getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public float getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(float distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public Stack<String> getDecisionPath() {
        return decisionPath;
    }

    public void setDecisionPath(Stack<String> decisionPath) {
        this.decisionPath = decisionPath;
    }

    public String getOriginNode() {
        return originNode;
    }

    public void setOriginNode(String originNode) {
        this.originNode = originNode;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    @Override
    public void calculateVehicleSpeed(float roadSpeedLimit) {
        //bus drives 10% slower than normal cars
        setTopSpeed(roadSpeedLimit - (roadSpeedLimit * SPEED_DIFFERENCE / 100));

    }

    @Override
    public float getVehiclesTopSpeed() {
        return getTopSpeed();
    }
}
