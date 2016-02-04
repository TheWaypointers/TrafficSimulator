package thewaypointers.trafficsimulator.common.simulation.models;

import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.common.enums.VehicleType;

import java.util.Stack;

/**
 * Created by User on 4.2.2016..
 */
public class Vehicle {

    private float topSpeed;
    private float currentSpeed;
    private VehicleType vehicleType;
    private float distanceTravelled;
    private Stack<String> decisionPath;
    private String originNode;
    private Lane lane;

    public Vehicle(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, String originNode, Lane lane){
        this.vehicleType = type;
        this.decisionPath = decisionPath;
        this.originNode = originNode;
        this.lane = lane;
        calculateTopSpeed(roadSpeedLimit);
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

    private void calculateTopSpeed(float roadSpeedLimit){

        if(vehicleType != null){
            switch (vehicleType){
                case CarNormal:
                    setTopSpeed(roadSpeedLimit);
                    break;
                case CarReckless:
                    break;
                case CarCautious:
                    break;
                case Bus:
                    break;
                case EmergencyService:
                    break;
                default:
                    break;
            }
        }

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
}
