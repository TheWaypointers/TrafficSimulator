package thewaypointers.trafficsimulator.simulation.models.vehicles;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.common.TrafficLightColor;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.managers.VehicleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Car implements IVehicle {

    private float topSpeed;
    private float currentSpeed;
    private VehicleType vehicleType;
    private float distanceTravelled;
    private Stack<String> decisionPath;
    private String originNode;
    private Lane lane;
    private DefaultWeightedEdge currentRoad;
    private float roadLength;

    private final long SPEED_DIFFERENCE = 10;
    private final long DISTANCE_BETWEEN_VEHICLES = 20;

    public Car(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, DefaultWeightedEdge currentRoad, float roadLength, String originNode, Lane lane) {
        this.vehicleType = type;
        this.decisionPath = decisionPath;
        this.originNode = originNode;
        this.lane = lane;
        this.currentRoad = currentRoad;
        distanceTravelled = 0;
        this.roadLength = roadLength;
        calculateVehicleSpeed(roadSpeedLimit);
        this.currentSpeed = this.topSpeed;
    }

    @Override
    public void calculateNextPosition(long timeStep, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {

        currentSpeed = topSpeed;
        float distanceToTravel = calculateDistanceToTravel(currentSpeed, timeStep);
        float nextPossiblePosition = distanceTravelled + distanceToTravel;

        //check if there is a vehicle in the distance to travel
        for (IVehicle vehicle : VehicleManager.getVehicleMap().get(this.getCurrentRoad())) {
            if (vehicle != this) {
                float vehiclePosition = vehicle.getVehiclesDistanceTravelled();
                if (vehiclePosition >= this.getDistanceTravelled() && vehiclePosition - DISTANCE_BETWEEN_VEHICLES <= nextPossiblePosition
                        && vehicle.getVehiclesOriginNode().equals(this.getOriginNode())) {
                    this.setDistanceTravelled(vehicle.getVehiclesDistanceTravelled() - DISTANCE_BETWEEN_VEHICLES);
                    this.setCurrentSpeed(vehicle.getVehiclesCurrentSpeed());
                    return;
                }
            }
        }


        Node nextNode = calculateNextNode(nodeGraphMap);
        //check if a node is coming up
        if (nextPossiblePosition >= roadLength) {
            //get the upcoming node


            if (nextNode.getNodeType() == NodeType.JunctionTrafficLights) {
                TrafficLightNode tlNode = ((TrafficLightNode) nextNode);

                if (tlNode.getColor() == TrafficLightColor.Green) {
                    float overLap = nextPossiblePosition - roadLength;
                    VehicleManager.removeVehicle(currentRoad, this);
                    RoadEdge nextRoadEdge = calculateNextRoad(nodeGraphMap);

                    currentRoad = nextRoadEdge.getRoad();
                    roadLength = nextRoadEdge.getRoadLength();
                    VehicleManager.getVehicleMap().get(currentRoad).add(this);

                    this.setDistanceTravelled(overLap);

                    return;
                } else {
                    if (getDistanceTravelled() < roadLength - 20) {
                        setDistanceTravelled(roadLength - 20);
                    }
                    currentSpeed = 0;
                    return;
                }
            } else if (nextNode.getNodeType() == NodeType.ExitNode) {
                VehicleManager.removeVehicle(currentRoad, this);
                return;
            }
        } else {
            this.setDistanceTravelled(nextPossiblePosition);
        }

        this.setDistanceTravelled(nextPossiblePosition);
        return;
    }

    private RoadEdge calculateNextRoad(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        String currentNodeName = decisionPath.pop();
        String nextNodeName = decisionPath.peek();

        this.setOriginNode(currentNodeName);

        Node currentNode = null;
        Node nextNode = null;

        for (Node node : nodeGraphMap.keySet()) {
            if (node.getNodeName().equals(currentNodeName)) {
                currentNode = node;
            } else if (node.getNodeName().equals(nextNodeName)) {
                nextNode = node;
            }
        }

        for (RoadEdge re : nodeGraphMap.get(currentNode)) {
            for (RoadEdge re1 : nodeGraphMap.get(nextNode)) {
                if (re.getRoad() == re1.getRoad()) {
                    return re;
                }
            }
        }

        return null;
    }

    private Node calculateNextNode(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        for (Node node : nodeGraphMap.keySet()) {
            if (node.getNodeName().equals(getDecisionPath().peek())) {
                return node;
            }
        }
        return null;
    }

    private float calculateDistanceToTravel(float currentSpeed, long timeStep) {
        //multiply by 1000 to get m/h
        float distance = currentSpeed * 1000;
        //divide by 60 to get m/minutes
        distance = distance / 60;
        //divide by 60 to get m/seconds
        distance = distance / 60;

        //to get the distance covered during 1 time step
        return distance * timeStep / 1000;
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

        if (vehicleType != null) {
            switch (vehicleType) {
                case CarNormal:
                    setTopSpeed(roadSpeedLimit);
                    break;
                case CarReckless:
                    //reckless cars drive 10% faster than the speed limit
                    setTopSpeed(roadSpeedLimit + (roadSpeedLimit * SPEED_DIFFERENCE / 100));
                    break;
                case CarCautious:
                    //cautious drivers drive 10% slower than the speed limit
                    setTopSpeed(roadSpeedLimit - (roadSpeedLimit * SPEED_DIFFERENCE / 100));
                    break;
                default:
                    break;
            }
        }

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

    public DefaultWeightedEdge getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(DefaultWeightedEdge currentRoad) {
        this.currentRoad = currentRoad;
    }

    public float getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(float roadLength) {
        this.roadLength = roadLength;
    }
}