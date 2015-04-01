package handlers;


import enums.ElevatorDirection;

public interface IFloorPanelHandler {
    public void reachedToFloor(int currentFloor);
    public void onFloor(int currentFloor);
    public void requestReceived(int floorNum, ElevatorDirection direction);
}
