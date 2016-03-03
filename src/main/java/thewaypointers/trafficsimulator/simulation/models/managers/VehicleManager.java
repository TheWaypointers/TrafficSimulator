package thewaypointers.trafficsimulator.simulation.models.managers;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleManager {

    static HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap;


    public static void setVehicleMap(HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap) {
        VehicleManager.vehicleMap = vehicleMap;
    }

    public static HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> getVehicleMap() {
        return vehicleMap;
    }

    public static void removeVehicle(DefaultWeightedEdge currentRoad, IVehicle vehicle) {
        vehicleMap.get(currentRoad).remove(vehicle);
    }

    public static int getVehicleCount() {
        int counter = 0;

        for (DefaultWeightedEdge road : vehicleMap.keySet()) {
            for (IVehicle vehicle : vehicleMap.get(road)) {
                counter++;
            }
        }
        return counter;
    }
}
