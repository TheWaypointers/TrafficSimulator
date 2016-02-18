package thewaypointers.trafficsimulator.simulation.models.interfaces;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;

import java.util.ArrayList;
import java.util.HashMap;

public interface IVehicle {

    void calculateVehicleSpeed(float roadSpeedLimit);
    float getVehiclesCurrentSpeed();
    float getVehiclesDistanceTravelled();
    HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> calculateNextPosition(long timeStep, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap);
    boolean isVehicleLeavingRoad();
}
