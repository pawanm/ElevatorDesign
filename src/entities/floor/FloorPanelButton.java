package entities.floor;


import handlers.IFloorPanelButtonHandler;
import enums.ElevatorDirection;

public class FloorPanelButton {

    private IFloorPanelButtonHandler mFloorPanelButtonHandler;
    private ElevatorDirection mButtonDirection;
    private boolean mIsButtonIlluminated = false;

    public FloorPanelButton(ElevatorDirection buttonDirection, IFloorPanelButtonHandler handler) {
        mButtonDirection = buttonDirection;
        mFloorPanelButtonHandler = handler;
    }

    public void setFloorPanelButtonHandler(IFloorPanelButtonHandler handler) {
        mFloorPanelButtonHandler = handler;
    }

    public void press() {
        mFloorPanelButtonHandler.onButtonPressed();
    }

    public void changeIlluminationState(boolean isButtonIlluminated) {
        mIsButtonIlluminated = isButtonIlluminated;
    }

    public boolean getIsIlluminated() {
        return mIsButtonIlluminated;
    }

    public ElevatorDirection getButtonDirection() {
        return mButtonDirection;
    }
}
