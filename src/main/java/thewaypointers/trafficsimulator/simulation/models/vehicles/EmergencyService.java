package thewaypointers.trafficsimulator.simulation.models.vehicles;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
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
import java.util.List;
import java.util.Stack;

public class EmergencyService implements IVehicle {

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
    private boolean vehicleIsTurningLeft;

    private final long SPEED_DIFFERENCE = 10;
    private final long DISTANCE_BETWEEN_VEHICLES = 22;
    private final long VEHICLE_LENGTH = 8;

    public EmergencyService(VehicleType type, float roadSpeedLimit, Stack<String> decisionPath, RoadEdge currentRoad, String originNode, Lane lane, float roadLength) {
        initialize(type, roadSpeedLimit, decisionPath, originNode, lane);
        this.currentRoad = currentRoad;
        this.roadLength = roadLength;
    }

    public EmergencyService(VehicleType type,
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
        distanceTravelled = 0;
        calculateVehicleSpeed(roadSpeedLimit);
        this.currentSpeed = this.topSpeed;
    }

    private IVehicle checkVehicleWithinDistance(float nextPossiblePosition) {
        for (IVehicle vehicle : VehicleManager.getVehicleMap().getFromRoad(this.getCurrentRoad().getRoad())) {
            if (vehicle != this) {
                float vehiclePosition = vehicle.getVehiclesDistanceTravelled();
                if (vehiclePosition >= this.getDistanceTravelled() - 2 && vehiclePosition - DISTANCE_BETWEEN_VEHICLES <= nextPossiblePosition
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
        checkVehicleSpeedsInJunction();
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

        if (calculateNextPossiblePosition(nextPossiblePosition, currentSectionLength)) return;

        // advance to next road or junction
        float overLap = nextPossiblePosition - currentSectionLength;
        if (getCurrentRoad() != null) {
            // vehicle is leaving road and entering junction
            Node nextNode = calculateNextNode(nodeGraphMap);
            if (nextNode.getNodeType() == NodeType.JunctionTrafficLights || nextNode.getNodeType() == NodeType.JunctionNormal) {
                if (canGoTroughJunction(nodeGraphMap, nextNode)) {
                    RoadEdge nextRoadEdge = calculateNextRoad(nodeGraphMap);
                    Direction origin = currentRoad.getDirection().opposite();   // opposite because we want the direction from the road's target node, and this direction is from road's origin node
                    Direction target = nextRoadEdge.getDirection();
                    float width = nextNode.getWidth();
                    float height = nextNode.getHeight();
                    currentRoad = null;
                    currentNode = nextNode;
                    checkVehicleSpeedsInJunction();
                    if (calculateNextPossiblePosition(nextPossiblePosition, currentSectionLength)) return;
                    junctionLocation = new JunctionLocationDTO(nextNode.getNodeName(), origin, target, overLap, width, height);

                    VehicleManager.getVehicleMap().remove(this);
                    VehicleManager.getVehicleMap().add(nextNode, this);

                    this.setDistanceTravelled(overLap);

                    return;
                } else {
                    if (getDistanceTravelled() < currentRoad.getLength() - 20) {
                        setDistanceTravelled(currentRoad.getLength() - 20);
                    }
                    currentSpeed = 0;
                    return;
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

    private boolean calculateNextPossiblePosition(float nextPossiblePosition, float currentSectionLength) {
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
            return true;
        }
        return false;
    }

    private void checkVehicleSpeedsInJunction() {
        if (currentRoad == null) {
            List<IVehicle> vehicleListInsideJunction = VehicleManager.getVehicleMap().getFromJunction(currentNode);

            if (vehicleListInsideJunction != null) {
                if (vehicleListInsideJunction.size() > 0) {
                    float speed = returnSlowestSpeedInJunction(vehicleListInsideJunction);
                    currentSpeed = speed;
                }
            }
        }
    }

    private float returnSlowestSpeedInJunction(List<IVehicle> vehicleListInsideJunction) {
        float speed = 0;

        for (IVehicle vehicle : vehicleListInsideJunction) {
            if (speed == 0) {
                speed = vehicle.getVehiclesCurrentSpeed();
            } else if (speed > vehicle.getVehiclesCurrentSpeed()) {
                speed = vehicle.getVehiclesCurrentSpeed();
            }
        }
        return speed;

    }


    private boolean canGoTroughJunction(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, Node node) {
        List<IVehicle> vehicleList = VehicleManager.getVehicleMap().getFromJunction(node);
        Direction origin = currentRoad.getDirection().opposite();

        //check if the next road is full
        RoadEdge nextRoad = getNextRoad(node, nodeGraphMap);
        List<IVehicle> vehicleListForNextRoad = VehicleManager.getVehicleMap().getFromRoad(nextRoad.getRoad());
        float maxNumberOfVehiclesOnRoad = nextRoad.getLength() / (VEHICLE_LENGTH + DISTANCE_BETWEEN_VEHICLES);

        if(vehicleListForNextRoad != null){
            if(vehicleListForNextRoad.size() >= maxNumberOfVehiclesOnRoad){
                return false;
            }
        }

        if(vehicleList == null){
            return true;
        }else if(vehicleList.size() > 0){
            for(IVehicle vehicle : vehicleList){
                if(vehicle.getJunctionLocation().getOrigin() == origin){
                    return true;
                }
                else if (vehicle.getJunctionLocation().getOrigin().opposite() == origin && vehicle.isVehicleTurningLeft() && vehicle.getJunctionLocation().getProgress() < 0.2) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    private RoadEdge getNextRoad(Node node, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {

        for (Node n : nodeGraphMap.keySet()) {
            for (RoadEdge re : nodeGraphMap.get(n)) {
                if (re.getDestination().equals(this.getVehiclesNextDestinationNode()) && re.getOrigin().equals(node.getNodeName())) {
                    return re;
                }
            }
        }
        return null;
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
        setTopSpeed(roadSpeedLimit + (roadSpeedLimit * SPEED_DIFFERENCE / 100));
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

    @Override
    public thewaypointers.trafficsimulator.common.VehicleType getVehiclesType() {
        return thewaypointers.trafficsimulator.common.VehicleType.EmergencyService;
    }

    @Override
    public boolean isVehicleTurningLeft() {
        return vehicleIsTurningLeft;
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
