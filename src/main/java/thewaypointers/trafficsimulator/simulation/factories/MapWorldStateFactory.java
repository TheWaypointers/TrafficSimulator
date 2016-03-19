package thewaypointers.trafficsimulator.simulation.factories;

import thewaypointers.trafficsimulator.common.MapDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.factories.xml.models.HandlerXML;
import thewaypointers.trafficsimulator.simulation.factories.xml.models.JunctionXML;
import thewaypointers.trafficsimulator.simulation.factories.xml.models.MapXML;
import thewaypointers.trafficsimulator.simulation.factories.xml.models.RoadXML;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;

public class MapWorldStateFactory {

    WorldStateDTO worldState;

    public MapWorldStateFactory(String pathToXML) {

        worldState = new WorldStateDTO();

        MapXML map = parseXML(pathToXML);
        buildWorldState(map);
    }

    private void buildWorldState(MapXML map) {

        MapDTO roadMap = worldState.getRoadMap();

        for (RoadXML road : map.getRoads()) {
            roadMap.addRoad(road.getOrigin(), road.getDestination(), road.getDirection(), road.getLength());
        }

        for(JunctionXML junction : map.getJunctions()){
            if(junction.getType().equals(NodeType.JunctionTrafficLights.toString())){
                worldState.getTrafficLightSystem().addJunction(roadMap.getJunction(junction.getName()));
            }
        }

    }

    private MapXML parseXML(String pathToXML) {
        try {
            File inputFile = new File(pathToXML);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            HandlerXML userhandler = new HandlerXML();
            saxParser.parse(inputFile, userhandler);

            return userhandler.map;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public WorldStateDTO getWorldState(){
        return worldState;
    }
}
