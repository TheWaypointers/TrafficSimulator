package thewaypointers.trafficsimulator.simulation.factories;

import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.vehicles.Car;

import java.util.*;

public class VehicleFactory {

    List<Node> exitNodes;

    public VehicleFactory(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        exitNodes = new ArrayList<>();

        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeType() == NodeType.ExitNode){
                exitNodes.add(node);
            }
        }
    }

    public IVehicle buildVehicle(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph, HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {

        String originNode = calculateOriginNode();
        String destinationNode = calculateDestinationNode(originNode);
        RoadEdge firstRoad = getTheFirstRoad(originNode, nodeGraphMap);
        Stack<String> decisions = calculatePath(originNode, destinationNode, roadGraph);
        return new Car(VehicleType.CarNormal, firstRoad.getSpeedLimit(), decisions, firstRoad, ((float) roadGraph.getEdgeWeight(firstRoad.getRoad())), originNode, Lane.Left);
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
        List<String> exitNodesAvailable = new ArrayList<>();

        for(Node node : exitNodes){
            if(!node.getNodeName().equals(originNode)){
                exitNodesAvailable.add(node.getNodeName());
            }
        }

        int numberOfExitNodes = exitNodesAvailable.size();

        int number;
        number = (int)(Math.random() * numberOfExitNodes);

        return exitNodesAvailable.get(number);
    }

    private String calculateOriginNode() {
        int numberOfExitNodes = exitNodes.size();

        int number;
        number = (int)(Math.random() * numberOfExitNodes);
        Node node = exitNodes.get(number);

        return node.getNodeName();
    }

    private Stack<String> calculatePath(String origin, String destination, SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph) {
        Stack decisions = new Stack<>();
        List<DefaultWeightedEdge> list =  BellmanFordShortestPath.findPathBetween(roadGraph, origin, destination);

        for(int i = list.size() - 1; i >= 0; i--){
            decisions.push(roadGraph.getEdgeTarget(list.get(i)));
        }

        return decisions;
    }

}
