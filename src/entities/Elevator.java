package entities;

import exception.InvalidFloorException;
import handlers.IElevatorButtonHandler;
import handlers.IElevatorHandler;
import handlers.IFloorPanelHandler;
import configuration.Configuration;
import controllers.ElevatorDoorController;
import enums.ElevatorDirection;
import controllers.RequestQueueController;

import java.util.ArrayList;
import java.util.List;

public class Elevator implements IElevator {

    private boolean mIsMoving = false;
    private RequestQueueController mRequestQueueController = new RequestQueueController();
    private int mCurrentFloor;
    private ElevatorDirection mElevatorDirection = ElevatorDirection.None;
    private ElevatorDirection mElevatorNextDirection = ElevatorDirection.None;
    private List<IFloorPanelHandler> mFloorPanelCallbacks = new ArrayList<IFloorPanelHandler>();
    private List<IElevatorHandler> mElevatorCallbacks = new ArrayList<IElevatorHandler>();
    private List<IElevatorButtonHandler> mButtonCallbacks = new ArrayList<IElevatorButtonHandler>();
    private ElevatorDoorController mDoorController;

    public Elevator() {
        mDoorController = new ElevatorDoorController(this);
    }

    @Override
    public void addFloorPanelCallback(IFloorPanelHandler callback) {
        mFloorPanelCallbacks.add(callback);
    }

    @Override
    public void addElevatorCallback(IElevatorHandler callback) {
        mElevatorCallbacks.add(callback);
    }

    @Override
    public void addElevatorButtonCallback(IElevatorButtonHandler callback) {
        mButtonCallbacks.add(callback);
    }

    @Override
    public boolean getIsMoving() {
        return mIsMoving;
    }

    @Override
    public void setIsMoving(boolean value) {
        mIsMoving = value;
    }


    @Override
    public int getCurrentFloor() {
        return this.mCurrentFloor;
    }

    @Override
    public void makeRequest(int floorNumber) throws InvalidFloorException {
        if (floorNumber > Configuration.TOP_FLOOR || floorNumber < Configuration.GROUND_FLOOR) {
            throw new InvalidFloorException();
        }
        ElevatorDirection direction = mCurrentFloor < floorNumber ? ElevatorDirection.Up : ElevatorDirection.Down;
        request(floorNumber, direction);
    }


    @Override
    public void request(int floorNumber, ElevatorDirection direction) {
        if (mCurrentFloor == floorNumber) {
            mDoorController.openDoorForFixedTimeIntervalAndThenClose();
            return;
        }

        mRequestQueueController.addRequest(floorNumber, direction);

        if (!mIsMoving) {
            moveElevator(direction);
        }

        fireRequestRecdHandler(floorNumber, direction);
    }

    @Override
    public void stopElevator() {
        fireButtonStoppedHandler();
        mIsMoving = false;
        mRequestQueueController.clearAllRequests();
    }

    @Override
    public ElevatorDirection getElevatorDirection() {
        return mElevatorDirection;
    }

    @Override
    public ElevatorDirection getElevatorNextDirection() {
        return mElevatorNextDirection;
    }

    private void moveElevator(ElevatorDirection direction) {
        mIsMoving = true;
        fireElevatorStartedHandler();
        if (direction == ElevatorDirection.Up && mCurrentFloor < mRequestQueueController.getFirstUpRequest()) {
            traverseFromUpQueueToDownQueue();
        } else if (direction == ElevatorDirection.Down && mCurrentFloor > mRequestQueueController.getFirstDownRequest()) {
            traverseFromDownQueueToUpQueue();
        }
        traverseIfRequestsExists();
        mIsMoving = false;
    }

    private void traverseIfRequestsExists() {
        if (mRequestQueueController.isUpQueueContainsAnyRequests()) {
            moveDownForUpRequest();
        }
        if (mRequestQueueController.isDownQueueContainsAnyRequests()) {
            moveUpForDownRequest();
        }
    }

    private void traverseFromDownQueueToUpQueue() {
        if (mRequestQueueController.isDownQueueContainsAnyRequests()) {
            moveDown();
        }
        if (mRequestQueueController.isUpQueueContainsAnyRequests()) {
            moveUp();
        }
    }

    private void traverseFromUpQueueToDownQueue() {
        if (mRequestQueueController.isUpQueueContainsAnyRequests()) {
            moveUp();
        }
        if (mRequestQueueController.isDownQueueContainsAnyRequests()) {
            moveDown();
        }
    }


    private void moveUp() {
        mElevatorDirection = ElevatorDirection.Up;
        for (int i = mCurrentFloor + 1; i < mRequestQueueController.upQueueLength(); i++) {
            changeFloorAndFireOnFloorHandler(i);
            if (mRequestQueueController.isRequestExistsForUpQueue(i)) {
                mRequestQueueController.clearRequestForAFloor(i, ElevatorDirection.Up);
                fireOnFloorHandler(i);
            }
            if (!mRequestQueueController.isUpQueueContainsAnyRequests()) {
                mElevatorDirection = ElevatorDirection.None;
                return;
            }
        }
        mElevatorDirection = ElevatorDirection.None;
    }

    private void moveDownForUpRequest() {
        mElevatorDirection = ElevatorDirection.Down;
        for (int i = mCurrentFloor - 1; i >= 0; i--) {
            changeFloorAndFireOnFloorHandler(i);
            if (!mRequestQueueController.isUpQueueContainsRequestsForFloor(i, ElevatorDirection.Down)) {
                mRequestQueueController.clearRequestForAFloor(i, ElevatorDirection.Up);
                fireOnFloorHandler(i);
                moveElevator(ElevatorDirection.Up);
                break;
            }
        }
        mElevatorDirection = ElevatorDirection.None;
    }

    private void moveDown() {
        mElevatorDirection = ElevatorDirection.Down;
        for (int i = mCurrentFloor - 1; i >= 0; i--) {
            changeFloorAndFireOnFloorHandler(i);
            if (mRequestQueueController.isRequestExistsForDownQueue(i)) {
                mRequestQueueController.clearRequestForAFloor(i, ElevatorDirection.Down);
                fireOnFloorHandler(i);

            }
            if (!mRequestQueueController.isDownQueueContainsAnyRequests()) {
                mElevatorDirection = ElevatorDirection.None;
                return;
            }
        }
        mElevatorDirection = ElevatorDirection.None;

    }


    private void moveUpForDownRequest() {
        mElevatorDirection = ElevatorDirection.Up;
        for (int i = mCurrentFloor + 1; i <= Configuration.TOP_FLOOR; i++) {
            changeFloorAndFireOnFloorHandler(i);

            if (!mRequestQueueController.isDownQueueContainsRequestsForFloor(i, ElevatorDirection.Up)) {
                fireOnFloorHandler(i);
                mRequestQueueController.clearRequestForAFloor(i, ElevatorDirection.Down);
                moveElevator(ElevatorDirection.Down);
                break;
            }
        }
        mElevatorDirection = ElevatorDirection.None;
    }

    private void changeFloorAndFireOnFloorHandler(int floorIncrement) {
        if (!mIsMoving) {
            mIsMoving = true;
        }
        mCurrentFloor = floorIncrement;
        for (IFloorPanelHandler callback : mFloorPanelCallbacks) {
            callback.onFloor(mCurrentFloor);
        }

    }

    private void fireOnFloorHandler(int floorNumber) {
        if (mCurrentFloor == floorNumber) {
            mIsMoving = false;
            for (IFloorPanelHandler callback : mFloorPanelCallbacks) {
                callback.onFloor(floorNumber);
            }

        }
    }

    private void fireElevatorStartedHandler() {
        for (IElevatorHandler callback : mElevatorCallbacks) {
            callback.elevatorStarted();
        }
    }

    private void fireButtonStoppedHandler() {
        for (IElevatorButtonHandler callback : mButtonCallbacks) {
            callback.onStopButtonPressed();
        }
    }


    private void fireRequestRecdHandler(int floorNo, ElevatorDirection direction) {
        for (IFloorPanelHandler callback : mFloorPanelCallbacks) {
            callback.requestReceived(floorNo, direction);
        }
    }

}
