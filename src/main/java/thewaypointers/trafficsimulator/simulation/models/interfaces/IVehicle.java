package thewaypointers.trafficsimulator.simulation.models.interfaces;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
import thewaypointers.trafficsimulator.common.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;

import java.util.ArrayList;
import java.util.HashMap;

public interface IVehicle {

    void calculateVehicleSpeed(float roadSpeedLimit);
    float getVehiclesCurrentSpeed();
    float getVehiclesDistanceTravelled();
    JunctionLocationDTO getJunctionLocation();
    void calculateNextPosition(long timeStep, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap);
    RoadEdge getCurrentRoadEdge();
    String getVehiclesOriginNode();
    String getVehiclesDestinationNode();
    String getVehiclesNextDestinationNode();
    float getVehiclesCurrentRoadLength();
    void setVehicleLabel(int label);
    int getVehicleLabel();
    JunctionLocationDTO getVehiclesJunctionLocation();
    VehicleType getVehiclesType();
    boolean isVehicleTurningLeft();
}
