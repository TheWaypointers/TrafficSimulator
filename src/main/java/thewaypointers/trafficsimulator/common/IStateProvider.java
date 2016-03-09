package thewaypointers.trafficsimulator.common;

public interface IStateProvider {
    WorldStateDTO getNextState(float vehicleMovement);
}
