package entities.floor;

import handlers.IElevatorButtonHandler;
import handlers.IElevatorHandler;
import handlers.IFloorPanelButtonHandler;
import handlers.IFloorPanelHandler;
import entities.IElevator;
import enums.DisplayDirection;
import enums.ElevatorDirection;

public class FloorPanel {

    private IElevator mElevator;
    private int mFloorNumber;
    private FloorPanelButton mUpDirectionButton;
    private FloorPanelButton mDownDirectionButton;
    private FloorPanelDisplay mFloorPanelDisplay;

    public FloorPanel(IElevator elevator, int floorNumber) {
        mElevator = elevator;
        mFloorNumber = floorNumber;
        mFloorPanelDisplay = new FloorPanelDisplay();

        mUpDirectionButton = new FloorPanelButton(ElevatorDirection.Up, getFloorPanelButtonCallback(ElevatorDirection.Up));
        mDownDirectionButton = new FloorPanelButton(ElevatorDirection.Down, getFloorPanelButtonCallback(ElevatorDirection.Down));
        mElevator.addFloorPanelCallback(getFloorPanelCallback());
        mElevator.addElevatorButtonCallback(getButtonCallback());
        mElevator.addElevatorCallback(getElevatorCallback());
    }

    public int getFloorPanelFloorNumber() {
        return mFloorNumber;
    }

    public FloorPanelButton getUpDirectionButton() {
        return mUpDirectionButton;
    }

    public FloorPanelButton getDownDirectionButton() {
        return mDownDirectionButton;
    }

    public FloorPanelDisplay getFloorPanelDisplay() {
        return mFloorPanelDisplay;
    }

    private IFloorPanelButtonHandler getFloorPanelButtonCallback(final ElevatorDirection direction) {
        return new IFloorPanelButtonHandler() {
            @Override
            public void onButtonPressed() {
                mElevator.request (mFloorNumber,direction);
            }
        };
    }

    private IElevatorHandler getElevatorCallback() {
        return new IElevatorHandler() {
            @Override
            public void elevatorStarted() {
                mElevator.setIsMoving(true);
            }
        };
    }

    private IElevatorButtonHandler getButtonCallback() {
        return new IElevatorButtonHandler() {
            @Override
            public void onStopButtonPressed() {
                mFloorPanelDisplay.SetFloorNumber(mElevator.getCurrentFloor());
                mFloorPanelDisplay.setElevatorDirection(DisplayDirection.Blank);
            }
        };
    }

    private IFloorPanelHandler getFloorPanelCallback() {
        return new IFloorPanelHandler() {
            @Override
            public void reachedToFloor(int currentFloor) {
                if (mElevator.getCurrentFloor() == mFloorNumber) {
                    if (mElevator.getElevatorNextDirection().equals(ElevatorDirection.Up)) {
                        mFloorPanelDisplay.setElevatorDirection(DisplayDirection.UpAndBlinking);
                        mUpDirectionButton.changeIlluminationState(false);
                    } else if (mElevator.getElevatorNextDirection().equals(ElevatorDirection.Down)) {
                        mFloorPanelDisplay.setElevatorDirection(DisplayDirection.DownAndBlinking);
                        mDownDirectionButton.changeIlluminationState(false);
                    } else {
                        mFloorPanelDisplay.setElevatorDirection(DisplayDirection.Blank);
                        mUpDirectionButton.changeIlluminationState(false);
                        mDownDirectionButton.changeIlluminationState(false);
                    }
                }
            }

            @Override
            public void onFloor(int currentFloor) {
                mFloorPanelDisplay.SetFloorNumber(mElevator.getCurrentFloor());

                if (mElevator.getElevatorDirection().equals(ElevatorDirection.Down))
                    mFloorPanelDisplay.setElevatorDirection(DisplayDirection.Down);
                else if (mElevator.getElevatorDirection().equals(ElevatorDirection.Up))
                    mFloorPanelDisplay.setElevatorDirection(DisplayDirection.Up);
                else
                    mFloorPanelDisplay.setElevatorDirection(DisplayDirection.Blank);
            }

            @Override
            public void requestReceived(int floorNumber, ElevatorDirection direction) {
                if (mFloorNumber == floorNumber) {
                    if (direction == ElevatorDirection.Down) {
                        mDownDirectionButton.changeIlluminationState(true);
                    }

                    if (direction == ElevatorDirection.Up) {
                        mUpDirectionButton.changeIlluminationState(true);
                    }
                }
            }
        };
    }
}
