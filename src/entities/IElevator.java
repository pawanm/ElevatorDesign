package entities;

import exception.InvalidFloorException;
import handlers.IElevatorButtonHandler;
import handlers.IElevatorHandler;
import handlers.IFloorPanelHandler;
import enums.ElevatorDirection;

public interface IElevator {
    public void makeRequest(int FloorNumber) throws InvalidFloorException;
    public void request(int floorNumber, ElevatorDirection direction);
    public int getCurrentFloor();
    public boolean getIsMoving();
    public void setIsMoving(boolean value);
    public void stopElevator();
    public void addFloorPanelCallback(IFloorPanelHandler callback);
    public void addElevatorCallback(IElevatorHandler callback);
    public void addElevatorButtonCallback(IElevatorButtonHandler callback);
    public ElevatorDirection getElevatorDirection();
    public ElevatorDirection getElevatorNextDirection();
}
