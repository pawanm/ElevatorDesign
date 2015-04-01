package entities.floor;


import enums.DisplayDirection;

public class FloorPanelDisplay {
    private int mFloorNumber;
    private DisplayDirection mDisplayDirection = DisplayDirection.Blank;

    public int getElevatorCurrentFloorNumber() {
        return mFloorNumber;

    }

    public void SetFloorNumber(int flrNum) {
        mFloorNumber = flrNum;
    }

    public DisplayDirection getElevatorDirectionDisplay() {
        return mDisplayDirection;

    }

    public void setElevatorDirection(DisplayDirection dspDir) {
        mDisplayDirection = dspDir;
    }

}
