package thewaypointers.trafficsimulator.simulation.factories.xml.models;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class HandlerXML extends DefaultHandler {
    String rootname;
    Attributes atr;
    private boolean flag = false;
    public MapXML map;

    private RoadXML road;
    private JunctionXML junction;

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes) {
        rootname = qName;
        atr = attributes;

        if (rootname.equalsIgnoreCase("simulation")) {
            map = new MapXML();
            map.setJunctions(new ArrayList<>());
            map.setRoads(new ArrayList<>());
            flag = true;
        }


    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (flag) {
            String value = new String(ch, start, length);
            if (rootname.equalsIgnoreCase("road")) {
                road = new RoadXML();
            }
            if (rootname.equalsIgnoreCase("origin")) {
                if(!value.trim().equals("")) {
                    road.setOrigin(value);
                }
            }
            if (rootname.equalsIgnoreCase("length")) {
                if(!value.trim().equals("")) {
                    road.setLength(value);
                }
            }
            if (rootname.equalsIgnoreCase("direction")) {
                if(!value.trim().equals("")) {
                    road.setDirection(value);
                }
            }
            if (rootname.equalsIgnoreCase("destination")) {
                if(!value.trim().equals("")) {
                    road.setDestination(value);
                }
            }
            if (rootname.equalsIgnoreCase("junction")) {
                junction = new JunctionXML();
            }
            if (rootname.equalsIgnoreCase("name")) {
                if(!value.trim().equals("")) {
                    junction.setName(value);
                }
            }
            if (rootname.equalsIgnoreCase("type")) {
                if(!value.trim().equals("")) {
                    junction.setType(value);
                }
            }

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        rootname = qName;
        if (rootname.equalsIgnoreCase("road") && !road.getOrigin().equals("")) {
            map.getRoads().add(road);
        }
        if (rootname.equalsIgnoreCase("junction")) {
            map.getJunctions().add(junction);
        }
        if (rootname.equalsIgnoreCase("simulation")) {
            flag = false;
        }
    }
}