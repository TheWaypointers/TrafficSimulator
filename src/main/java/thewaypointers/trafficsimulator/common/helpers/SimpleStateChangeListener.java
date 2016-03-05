package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

public class SimpleStateChangeListener implements IStateChangeListener{

    @Override
    public void NewStateReceived(WorldStateDTO state) {
        ILocation location = state.getVehicleList().getAll().get(0).getLocation();
        if (location.getClass().equals(JunctionLocationDTO.class)){
            System.out.format("Vehicle 0: at junction");
        }else if(location.getClass().equals(RoadLocationDTO.class)){
            RoadLocationDTO loc = (RoadLocationDTO) location;
            System.out.format("Vehicle 0 position: %f en route from %s to %s",
                              loc.getDistanceTravelled(),
                              loc.getRoad().getFrom().getLabel(),
                              loc.getRoad().getTo().getLabel());
        }
        System.out.println();
    }
}
