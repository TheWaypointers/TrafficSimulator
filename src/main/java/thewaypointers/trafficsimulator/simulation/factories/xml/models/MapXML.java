package thewaypointers.trafficsimulator.simulation.factories.xml.models;


import java.util.ArrayList;
import java.util.List;

public class MapXML {
    private List<RoadXML> roads = new ArrayList<>();
    private List<JunctionXML> junctions = new ArrayList<>();

    public List<RoadXML> getRoads() {
        return roads;
    }

    public void setRoads(List<RoadXML> roads) {
        this.roads = roads;
    }

    public List<JunctionXML> getJunctions() {
        return junctions;
    }

    public void setJunctions(List<JunctionXML> junctions) {
        this.junctions = junctions;
    }
}
