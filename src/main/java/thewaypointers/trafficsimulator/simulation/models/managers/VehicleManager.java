package thewaypointers.trafficsimulator.simulation.models.managers;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.simulation.models.VehicleMap;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleManager {

    static VehicleMap vehicleMap;


    public static void setVehicleMap(VehicleMap vehicleMap) {
        VehicleManager.vehicleMap = vehicleMap;
    }

    public static VehicleMap getVehicleMap() {
        return vehicleMap;
    }
}
