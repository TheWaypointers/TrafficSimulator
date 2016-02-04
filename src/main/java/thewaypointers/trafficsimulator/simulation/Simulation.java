package thewaypointers.trafficsimulator.simulation;

import thewaypointers.trafficsimulator.common.ISimulationInputListener;
import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import org.jgrapht.graph.*;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.DirectionFromNode;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;

import java.util.ArrayList;
import java.util.HashMap;


public class Simulation implements ISimulationInputListener {

    boolean run;
    IStateChangeListener stateChangeListener;
    SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  roadGraph;
    HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap = new HashMap<>();

    public Simulation(IStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }


    public Simulation(){
        setGraph();
    }

    public void SimulationParameterChanged(String parameterName, String value) {
        // set new value for the parameter in the simulation
    }

    public void NextSimulationStep(){

        // modify simulation state...

        WorldStateDTO state = new WorldStateDTO();

        // put the new simulation state into the DTO...

        stateChangeListener.NewStateReceived(state);
    }

    public void runSimulation(long timeStep){
        while(run){

            NextSimulationStep();

            try{
                Thread.sleep(timeStep);
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }

    private void setGraph() {
        roadGraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        roadGraph.addVertex("1");
        roadGraph.addVertex("2");

        DefaultWeightedEdge e1 = roadGraph.addEdge("1", "2");
        roadGraph.setEdgeWeight(e1, 200);

        Node node1 = new Node("1", NodeType.ExitNode);
        RoadEdge re1 = new RoadEdge(e1, DirectionFromNode.Right);
        Node node2 = new Node("2", NodeType.ExitNode);
        RoadEdge re2 = new RoadEdge(e1, DirectionFromNode.Left);
        nodeGraphMap.put(node1, new ArrayList<>());
        nodeGraphMap.get(node1).add(re1);

        nodeGraphMap.put(node2, new ArrayList<>());
        nodeGraphMap.get(node2).add(re2);

    }

}
