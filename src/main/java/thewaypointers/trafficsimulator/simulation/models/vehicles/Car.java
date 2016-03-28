package thewaypointers.trafficsimulator.simulation.models.vehicles;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.common.RoadLocationDTO;
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
    private RoadEdge currentRoad;
    private Node currentNode;
    private float roadLength;
    private JunctionLocationDTO junctionLocation;
    private int label;
    private int junctionBlocked;

    private final long SPEED_DIFFERENCE = 10;
    private final long DISTANCE_BETWEEN_VEHICLES = 20;
    private final int BLOCKED_JUNCTION_COUNTER = 25;

    public Car(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, RoadEdge currentRoad, String originNode, Lane lane, float roadLength) {
        initialize(type, roadSpeedLimit, decisionPath, originNode, lane);
        this.currentRoad = currentRoad;
        this.roadLength = roadLength;
    }

    public Car(VehicleType type,
               float roadSpeedLimit,
               Stack<String> decisionPath,
               TrafficLightNode currentNode,
               String originNode,
               Lane lane,
               Direction origin,
               Direction target) {
        initialize(type, roadSpeedLimit, decisionPath, originNode, lane);
        this.currentNode = currentNode;
        this.junctionLocation = new JunctionLocationDTO(currentNode.getNodeName(), origin, target, 0);
    }

    private void initialize(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, String originNode, Lane lane) {
        this.vehicleType = type;
        this.decisionPath = decisionPath;
        this.originNode = originNode;
        this.lane = lane;
        this.currentRoad = currentRoad;
        distanceTravelled = 0;
        this.roadLength = roadLength;
        calculateVehicleSpeed(roadSpeedLimit);
        this.currentSpeed = this.topSpeed;
        junctionBlocked = 0;
    }

    private IVehicle checkVehicleWithinDistance(float nextPossiblePosition) {
        for (IVehicle vehicle : VehicleManager.getVehicleMap().getFromRoad(this.getCurrentRoad().getRoad())) {
            if (vehicle != this) {
                float vehiclePosition = vehicle.getVehiclesDistanceTravelled();
                if (vehiclePosition >= this.getDistanceTravelled() && vehiclePosition - DISTANCE_BETWEEN_VEHICLES <= nextPossiblePosition
                        && vehicle.getVehiclesOriginNode().equals(this.getOriginNode())) {
                    return vehicle;
                }
            }
        }
        return null;
    }

    @Override
    public void calculateNextPosition(long timeStep, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        currentSpeed = topSpeed;
        float distanceTravelled = getCurrentRoad() != null ? getDistanceTravelled() : getJunctionLocation().getDistanceTravelled(getCurrentNode().getWidth(), getCurrentNode().getHeight());
        float distanceToTravel = calculateDistanceToTravel(currentSpeed, timeStep);
        float nextPossiblePosition = distanceTravelled + distanceToTravel;

        if (currentRoad != null) {
            IVehicle vehicleInFront = checkVehicleWithinDistance(nextPossiblePosition);
            if (vehicleInFront != null) {
                this.setDistanceTravelled(vehicleInFront.getVehiclesDistanceTravelled() - DISTANCE_BETWEEN_VEHICLES);
                this.setCurrentSpeed(vehicleInFront.getVehiclesCurrentSpeed());
                return;
            }
        }

        float currentSectionLength =
                currentRoad != null ?
                        currentRoad.getLength() :
                        junctionLocation.getRouteLength(currentNode.getWidth(), currentNode.getWidth());


        if (nextPossiblePosition <= currentSectionLength) {
            // can move freely inside current section
            if (currentRoad != null) {
                this.setDistanceTravelled(nextPossiblePosition);
            } else {
                this.setJunctionLocation(new JunctionLocationDTO(
                        getJunctionLocation(),
                        nextPossiblePosition,
                        getCurrentNode().getWidth(),
                        getCurrentNode().getHeight()
                ));
            }
            return;
        }

        // advance to next road or junction
        float overLap = nextPossiblePosition - currentSectionLength;
        if (getCurrentRoad() != null) {
            // vehicle is leaving road and entering junction
            Node nextNode = calculateNextNode(nodeGraphMap);
            if (nextNode.getNodeType() == NodeType.JunctionTrafficLights) {
                TrafficLightNode tlNode = ((TrafficLightNode) nextNode);
                if (lightFromThisDirection(tlNode) == TrafficLightColor.Green) {
                    if (canGoTroughJunction(nodeGraphMap, tlNode)) {
                        RoadEdge nextRoadEdge = calculateNextRoad(nodeGraphMap);
                        Direction origin = currentRoad.getDirection().opposite();   // opposite because we want the direction from the road's target node, and this direction is from road's origin node
                        Direction target = nextRoadEdge.getDirection();
                        float width = tlNode.getWidth();
                        float height = tlNode.getHeight();
                        currentRoad = null;
                        currentNode = tlNode;
                        junctionBlocked = 0;
                        junctionLocation = new JunctionLocationDTO(tlNode.getNodeName(), origin, target, overLap, width, height);

                        VehicleManager.getVehicleMap().remove(this);
                        VehicleManager.getVehicleMap().add(tlNode, this);

                        this.setDistanceTravelled(overLap);

                        return;
                    } else {
                        if (getDistanceTravelled() < currentRoad.getLength() - 20) {
                            setDistanceTravelled(currentRoad.getLength() - 20);
                        }
                    }
                }  else {
                    if (getDistanceTravelled() < currentRoad.getLength() - 20) {
                        setDistanceTravelled(currentRoad.getLength() - 20);
                    }
                    currentSpeed = 0;
                    return;
                }
            } else if(nextNode.getNodeType() == NodeType.JunctionNormal){
                if (canGoTroughJunction(nodeGraphMap, nextNode)) {
                    RoadEdge nextRoadEdge = calculateNextRoad(nodeGraphMap);
                    Direction origin = currentRoad.getDirection().opposite();   // opposite because we want the direction from the road's target node, and this direction is from road's origin node
                    Direction target = nextRoadEdge.getDirection();
                    float width = nextNode.getWidth();
                    float height = nextNode.getHeight();
                    currentRoad = null;
                    currentNode = nextNode;
                    junctionBlocked = 0;
                    junctionLocation = new JunctionLocationDTO(nextNode.getNodeName(), origin, target, overLap, width, height);

                    VehicleManager.getVehicleMap().remove(this);
                    VehicleManager.getVehicleMap().add(nextNode, this);

                    this.setDistanceTravelled(overLap);

                    return;
                } else {
                    if (getDistanceTravelled() < currentRoad.getLength() - 20) {
                        setDistanceTravelled(currentRoad.getLength() - 20);
                    }
                }
            } else if (nextNode.getNodeType() == NodeType.ExitNode) {
                VehicleManager.getVehicleMap().remove(this);
                return;
            }
        } else {
            // vehicle is leaving junction and entering road
            RoadEdge nextRoad = nodeGraphMap.get(getCurrentNode()).stream()
                    .filter(x -> x.getDirection().equals(getJunctionLocation().getTarget()))
                    .findFirst()
                    .get();
            setCurrentRoad(nextRoad);
            setDistanceTravelled(overLap);
            setCurrentNode(null);
            setJunctionLocation(null);
            VehicleManager.getVehicleMap().remove(this);
            VehicleManager.getVehicleMap().add(nextRoad.getRoad(), this);
            return;
        }
    }


    private boolean canGoTroughJunction(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, Node node) {
        Direction direction = DirectionVehicleIsApproachingJunction(nodeGraphMap, node);
        List<IVehicle> vehicleList = VehicleManager.getVehicleMap().getFromJunction(node);

        //check for blocked junction
        if(vehicleList == null){
            return true;
        }else{
            if(vehicleList.size() == 0){
                junctionBlocked++;
            }
        }

        if(junctionBlocked >= BLOCKED_JUNCTION_COUNTER){
            return true;
        }


        //check for vehicles inside junction
        if (vehicleList != null) {
            if (vehicleList.size() > 0) {
                for (IVehicle vehicle : vehicleList) {
                    if(node.getNodeType() == NodeType.JunctionTrafficLights){
                        if (vehicle.getJunctionLocation().getOrigin() != currentRoad.getDirection().opposite() && vehicle.getJunctionLocation().getOrigin() != currentRoad.getDirection()) {
                            return false;
                        }
                    }else{
                        if (vehicle.getJunctionLocation().getOrigin() != currentRoad.getDirection().opposite() && vehicle.getJunctionLocation().getOrigin() != currentRoad.getDirection()) {
                            return false;
                        }else if(vehicle.getJunctionLocation().getTarget() != currentRoad.getDirection().opposite().toLeft() && vehicle.getJunctionLocation().getOrigin() != currentRoad.getDirection().opposite()){
                            return false;
                        }
                    }
                }
            }
        }

        //special junction rules when there are no traffic lights (let the car on the right pass first)
        if(node.getNodeType() == NodeType.JunctionNormal){
            RoadEdge rightRoad = roadRightFromThis(node, direction, nodeGraphMap);
            List<IVehicle> vehiclesFromRightSide = VehicleManager.getVehicleMap().getFromRoad(rightRoad.getRoad());

            if(vehiclesFromRightSide != null){
                if(vehiclesFromRightSide.size() > 0){
                    for(IVehicle vehicle : vehiclesFromRightSide){
                        if (vehicle.getVehiclesDistanceTravelled() >= vehicle.getVehiclesCurrentRoadLength() - 20) {
                            return false;
                        }
                    }
                }
            }

        }

        //check if vehicle is turning left
        if (!isVehicleIsTurningLeft(node, direction)) {
            return true;
        }
        RoadEdge oppositeRoad = getOppositeRoad(node, direction, nodeGraphMap);

        //check if the vehicle from the opposite road is going to collide with this vehicle
        if (oppositeRoad != null) {
            List<IVehicle> carsFromTheOppositeRoad = VehicleManager.getVehicleMap().getFromRoad((oppositeRoad.getRoad()));

            if (carsFromTheOppositeRoad != null) {
                if (carsFromTheOppositeRoad.size() > 0) {

                    for (IVehicle vehicle : carsFromTheOppositeRoad) {
                        if (vehicle.getVehiclesDistanceTravelled() >= vehicle.getVehiclesCurrentRoadLength() - 40) {
                            if (vehicle.getVehiclesNextDestinationNode().equals(this.getOriginNode())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private RoadEdge getOppositeRoad(Node tlNode, Direction direction, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        switch (direction) {
            case Left:
                return otherSideOfTheRoad(tlNode.getRightRoad(), nodeGraphMap);
            case Up:
                return otherSideOfTheRoad(tlNode.getDownRoad(), nodeGraphMap);
            case Right:
                return otherSideOfTheRoad(tlNode.getLeftRoad(), nodeGraphMap);
            case Down:
                return otherSideOfTheRoad(tlNode.getUpRoad(), nodeGraphMap);
            default:
                break;
        }
        return null;
    }

    private RoadEdge otherSideOfTheRoad(RoadEdge road, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {

        if (road != null) {
            for (Node node : nodeGraphMap.keySet()) {
                for (RoadEdge re : nodeGraphMap.get(node)) {
                    if (re.getDestination().equals(road.getOrigin()) && re.getOrigin().equals(road.getDestination())) {
                        return re;
                    }
                }
            }
        }
        return null;
    }

    private boolean isVehicleIsTurningLeft(Node tlNode, Direction direction) {
        String nextNodeName = getVehiclesNextDestinationNode();


        switch (direction) {
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

    private RoadEdge roadRightFromThis(Node node, Direction direction, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {

        switch (direction) {
            case Left:
                if (node.getDownRoad() != null) {
                    return otherSideOfTheRoad(node.getDownRoad(), nodeGraphMap);
                }
                break;
            case Up:
                if (node.getLeftRoad() != null) {
                    return otherSideOfTheRoad(node.getLeftRoad(), nodeGraphMap);
                }
                break;
            case Right:
                if (node.getUpRoad() != null) {
                    return otherSideOfTheRoad(node.getUpRoad(), nodeGraphMap);
                }
                break;
            case Down:
                if (node.getRightRoad() != null) {
                    return otherSideOfTheRoad(node.getRightRoad(), nodeGraphMap);
                }
                break;
            default:
                break;
        }
        return null;
    }

    private Direction DirectionVehicleIsApproachingJunction(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, Node tlNode) {
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
    public RoadEdge getCurrentRoadEdge() {
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
        if (decisionPath.size() >= 2) {
            return decisionPath.get(decisionPath.size() - 2);
        } else {
            return decisionPath.peek();
        }
    }

    @Override
    public float getVehiclesCurrentRoadLength() {
        return this.roadLength;
    }

    @Override
    public void setVehicleLabel(int label) {
        this.label = label;
    }

    @Override
    public int getVehicleLabel() {
        return label;
    }

    @Override
    public JunctionLocationDTO getVehiclesJunctionLocation() {
        return this.junctionLocation;
    }

    public RoadEdge getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(RoadEdge currentRoad) {
        this.currentRoad = currentRoad;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(TrafficLightNode currentNode) {
        this.currentNode = currentNode;
    }

    public JunctionLocationDTO getJunctionLocation() {
        return junctionLocation;
    }

    public void setJunctionLocation(JunctionLocationDTO junctionLocation) {
        this.junctionLocation = junctionLocation;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}