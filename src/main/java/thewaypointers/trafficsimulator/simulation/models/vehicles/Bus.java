package thewaypointers.trafficsimulator.simulation.models.vehicles;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Bus implements IVehicle {

    private float topSpeed;
    private float currentSpeed;
    private VehicleType vehicleType;
    private float distanceTravelled;
    private Stack<String> decisionPath;
    private String originNode;
    private Lane lane;
    private boolean onExit;
    private DefaultWeightedEdge currentRoad;

    private final long SPEED_DIFFERENCE = 10;

    //same constructor as in Car
    public Bus(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, String originNode, Lane lane) {
        this.vehicleType = VehicleType.Bus;
        this.decisionPath = decisionPath;
        this.originNode = originNode;
        this.lane = lane;
        calculateVehicleSpeed(roadSpeedLimit);
    }

    //constructor without VehicleType
    public Bus(float roadSpeedLimit, Stack<String> decisionPath, String originNode, Lane lane) {
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
    public float getVehiclesCurrentSpeed() {
        return getCurrentSpeed();
    }

    @Override
    public float getVehiclesDistanceTravelled() {
        return getDistanceTravelled();
    }

    @Override
    public void calculateNextPosition(long timeStep, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
    }

    @Override
    public DefaultWeightedEdge getCurrentRoadEdge() {
        return getCurrentRoad();
    }

    @Override
    public String getVehiclesOriginNode() {
        return getOriginNode();
    }

    @Override
    public String getVehiclesDestinationNode() {
        return decisionPath.peek();
    }

    public boolean isOnExit() {
        return onExit;
    }

    public void setOnExit(boolean onExit) {
        this.onExit = onExit;
    }

    public DefaultWeightedEdge getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(DefaultWeightedEdge currentRoad) {
        this.currentRoad = currentRoad;
    }
}
