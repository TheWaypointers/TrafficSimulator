package thewaypointers.trafficsimulator.simulation.models.vehicles;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.common.TrafficLightColor;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.DirectionFromNode;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.managers.VehicleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (nextPossiblePosition >= roadLength) {
            //get the upcoming node


            if (nextNode.getNodeType() == NodeType.JunctionTrafficLights) {

                TrafficLightNode tlNode = ((TrafficLightNode) nextNode);

                if (lightFromThisDirection(tlNode) == TrafficLightColor.Green) {
                    if (canGoTroughJunction(nodeGraphMap, tlNode, timeStep)) {
                        float overLap = nextPossiblePosition - roadLength;
                        VehicleManager.removeVehicle(currentRoad, this);
                        RoadEdge nextRoadEdge = calculateNextRoad(nodeGraphMap);

                        currentRoad = nextRoadEdge.getRoad();
                        roadLength = nextRoadEdge.getRoadLength();
                        VehicleManager.getVehicleMap().get(currentRoad).add(this);

                        this.setDistanceTravelled(overLap);

                        return;
                    }
                    System.out.println("propustam suprotnog" );
                    if (getDistanceTravelled() < roadLength - 20) {
                        setDistanceTravelled(roadLength - 20);
                    }
                    currentSpeed = 0;
                    return;
                } else {
                    if (getDistanceTravelled() < roadLength - 20) {
                        setDistanceTravelled(roadLength - 20);
                    }
                    currentSpeed = 0;
                    return;
                }
            } else if (nextNode.getNodeType() == NodeType.JunctionNormal) {
                float overLap = nextPossiblePosition - roadLength;
                VehicleManager.removeVehicle(currentRoad, this);
                RoadEdge nextRoadEdge = calculateNextRoad(nodeGraphMap);

                currentRoad = nextRoadEdge.getRoad();
                roadLength = nextRoadEdge.getRoadLength();
                VehicleManager.getVehicleMap().get(currentRoad).add(this);

                this.setDistanceTravelled(overLap);

                return;
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

    private boolean canGoTroughJunction(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, TrafficLightNode tlNode, long timeStep) {
        DirectionFromNode direciton = DirectionVehicleIsApproachingJunction(nodeGraphMap, tlNode);
        if (!isVehicleIsTurningLeft(tlNode, direciton)) {
            return true;
        }

        RoadEdge oppositeRoad = getOppositeRoad(tlNode, direciton);

        if (oppositeRoad != null) {
            List<IVehicle> carsFromTheOppositeRoad = VehicleManager.getVehicleMap().get(oppositeRoad.getRoad());

            if (carsFromTheOppositeRoad.size() > 0) {

                for (IVehicle vehicle : carsFromTheOppositeRoad) {
                    if (vehicle.getVehiclesDistanceTravelled() >= 260) {
                        if (vehicle.getVehiclesNextDestinationNode().equals(this.getOriginNode())) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private RoadEdge getOppositeRoad(TrafficLightNode tlNode, DirectionFromNode direciton) {
        switch (direciton) {
            case Left:
                return tlNode.getRightRoad();
            case Up:
                return tlNode.getDownRoad();
            case Right:
                return tlNode.getLeftRoad();
            case Down:
                return tlNode.getUpRoad();
            default:
                break;
        }
        return null;
    }

    private boolean isVehicleIsTurningLeft(TrafficLightNode tlNode, DirectionFromNode direciton) {
        String nextNodeName = getVehiclesNextDestinationNode();


        switch (direciton) {
            case Left:
                if (tlNode.getUpRoad() != null) {
                    if (tlNode.getUpRoad().getDestination().equals(nextNodeName)) {
                        return true;
                    }
                }
                break;
            case Up:
                if (tlNode.getRightRoad() != null) {
                    if (tlNode.getRightRoad().getDestination().equals(nextNodeName)) {
                        return true;
                    }
                }
                break;
            case Right:
                if (tlNode.getDownRoad() != null) {
                    if (tlNode.getDownRoad().getDestination().equals(nextNodeName)) {
                        return true;
                    }
                }
                break;
            case Down:
                if (tlNode.getLeftRoad() != null) {
                    if (tlNode.getLeftRoad().getDestination().equals(nextNodeName)) {
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    private DirectionFromNode DirectionVehicleIsApproachingJunction(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, TrafficLightNode tlNode) {
        for (RoadEdge re : nodeGraphMap.get(tlNode)) {
            if (re.getDestination().equals(this.getOriginNode())) {
                return re.getDirection();
            }
        }
        return null;
    }

    private TrafficLightColor lightFromThisDirection(TrafficLightNode tlNode) {
        if (tlNode.getLeftRoad() != null) {
            if (tlNode.getLeftRoad().getDestination().equals(originNode)) {
                return tlNode.getLeft();
            }
        }
        if (tlNode.getRightRoad() != null) {
            if (tlNode.getRightRoad().getDestination().equals(originNode)) {
                return tlNode.getRight();
            }
        }
        if (tlNode.getUpRoad() != null) {
            if (tlNode.getUpRoad().getDestination().equals(originNode)) {
                return tlNode.getUp();
            }
        }
        return tlNode.getDown();
    }

    private RoadEdge calculateNextRoad(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        String currentNodeName = decisionPath.pop();
        String nextNodeName = decisionPath.peek();

        this.setOriginNode(currentNodeName);

        Node currentNode = null;

        for (Node node : nodeGraphMap.keySet()) {
            if (node.getNodeName().equals(currentNodeName)) {
                currentNode = node;
            }
        }

        for (RoadEdge road : nodeGraphMap.get(currentNode)) {
            if (road.getOrigin().equals(currentNodeName) && road.getDestination().equals(nextNodeName)) {
                return road;
            }
        }

        throw new AssertionError("Road not found!");
    }

    private Node calculateNextNode(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        for (Node node : nodeGraphMap.keySet()) {
            if (node.getNodeName().equals(getDecisionPath().peek())) {
                return node;
            }
        }
        throw new AssertionError("Node not found in map!");
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

    @Override
    public String getVehiclesNextDestinationNode() {
        if(decisionPath.size() >= 2){
            return decisionPath.get(decisionPath.size() - 2);
        }
        else{
            return decisionPath.peek();
        }
    }

    @Override
    public float getVehiclesCurrentRoadLength() {
        return this.roadLength;
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