package entities;

import exception.InvalidFloorException;

public class ElevatorPanel {
    private IElevator mElevator;

    public ElevatorPanel(IElevator elevator) {
        mElevator = elevator;
    }

    public void stopElevator() {
        mElevator.stopElevator();
    }

    public void requestForFloor(int floorNumber) throws InvalidFloorException{
        if (floorNumber > 8 || floorNumber < 0) return;
        mElevator.makeRequest(floorNumber);
    }
}
