package thewaypointers.trafficsimulator.simulation.factories;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.vehicles.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class VehicleFactory {


    public IVehicle buildVehicle(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {

        String originNode = calculateOriginNode();
        String destinationNode = calculateDestinationNode(originNode);
        RoadEdge firstRoad = getTheFirstRoad(originNode, nodeGraphMap);
        Stack<String> decisions = calculatePath(originNode, destinationNode);
        IVehicle vehicle = new Car(VehicleType.CarNormal, firstRoad.getSpeedLimit(), decisions, firstRoad.getRoad(), ((float) roadGraph.getEdgeWeight(firstRoad.getRoad())), originNode, Lane.Left);


        return vehicle;
    }

    private RoadEdge getTheFirstRoad(String originNode, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeName().equals(originNode)){
                return nodeGraphMap.get(node).get(0);
            }
        }
        return null;
    }

    private String calculateDestinationNode(String originNode) {
        //TODO: calculate the destination node of the vehicle
        return "3";
    }

    private String calculateOriginNode() {
        //TODO: calculate the node the vehicle will spawn on
        return "1";
    }

    private Stack<String> calculatePath(String origin, String destination) {
        //TODO: calculate path from graph
        Stack decisions = new Stack<>();
        decisions.push("3");
        decisions.push("2");
        return decisions;
    }

}
