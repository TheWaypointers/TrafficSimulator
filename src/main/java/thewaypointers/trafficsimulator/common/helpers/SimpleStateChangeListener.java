package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.IStateChangeListener;
import thewaypointers.trafficsimulator.common.LocationDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;

public class SimpleStateChangeListener implements IStateChangeListener{

    @Override
    public void NewStateReceived(WorldStateDTO state) {
        LocationDTO loc = state.getVehicleList().getAll().get(0).getLocation();
        System.out.format("Vehicle 0 position: %f en route from %s to %s",
                          loc.getDistanceTravelled(),
                          loc.getRoad().getFrom().getLabel(),
                          loc.getRoad().getTo().getLabel());
        System.out.println();
    }
}
