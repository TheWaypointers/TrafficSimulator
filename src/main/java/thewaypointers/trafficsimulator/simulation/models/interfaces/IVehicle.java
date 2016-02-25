package thewaypointers.trafficsimulator.simulation.models.interfaces;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;

import java.util.ArrayList;
import java.util.HashMap;

public interface IVehicle {

    public void calculateVehicleSpeed(float roadSpeedLimit);
    public float getVehiclesCurrentSpeed();
    public float getVehiclesDistanceTravelled();
    public HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> calculateNextPosition(long timeStep, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap, HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap);
    public boolean isVehicleLeavingRoad();
    public DefaultWeightedEdge getCurrentRoadEdge();
    public String getVehiclesOriginNode();
    public String getVehiclesDestinationNode();
}
