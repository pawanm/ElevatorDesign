package controllers;

import configuration.Configuration;
import enums.ElevatorDirection;

public class RequestQueueController {

    private boolean[] mUpRequest;
    private boolean[] mDownRequest;

    public RequestQueueController() {
        mUpRequest = new boolean[Configuration.TOP_FLOOR + 1];
        mDownRequest = new boolean[Configuration.TOP_FLOOR + 1];
    }

    public void addRequest(int floorNumber, ElevatorDirection direction) {
        if (direction == ElevatorDirection.Up) {
            mUpRequest[floorNumber] = true;
        } else {
            mDownRequest[floorNumber] = true;
        }
    }

    public void clearAllRequests() {
        for (int i = 0; i <= Configuration.TOP_FLOOR; i++) {
            clearRequestForAFloor(i, ElevatorDirection.Down);
            clearRequestForAFloor(i, ElevatorDirection.Up);
        }
    }

    public void clearRequestForAFloor(int floorNumber, ElevatorDirection direction) {
        if (direction == ElevatorDirection.Down) {
            mDownRequest[floorNumber] = false;
        } else {
            mUpRequest[floorNumber] = false;
        }
    }

    public int upQueueLength() {
        return mUpRequest.length;
    }

    public boolean isRequestExistsForUpQueue(int i) {
        return mUpRequest[i];
    }

    public boolean isRequestExistsForDownQueue(int i) {
        return mDownRequest[i];
    }

    public boolean isUpQueueContainsAnyRequests() {
        return traverseQueue(mUpRequest);
    }

    public boolean isDownQueueContainsAnyRequests() {
        return traverseQueue(mDownRequest);
    }

    public boolean isDownQueueContainsRequestsForFloor(int floorNumber, ElevatorDirection elevatorDirection) {
        if (elevatorDirection == ElevatorDirection.Up && floorNumber <= Configuration.TOP_FLOOR) {
            floorNumber += 1;
            for (int i = floorNumber; i <= Configuration.TOP_FLOOR; i++) {
                if (mDownRequest[i] == true)
                    return true;
            }
        }

        return false;


    }

    public boolean isUpQueueContainsRequestsForFloor(int floorNumber, ElevatorDirection elevatorDirection) {
        if (elevatorDirection == ElevatorDirection.Down && floorNumber >= Configuration.GROUND_FLOOR) {
            floorNumber -= 1;
            for (int i = floorNumber; i >= Configuration.GROUND_FLOOR; i--) {
                if (mUpRequest[i] == true)
                    return true;
            }
        }
        return false;

    }

    public int getFirstDownRequest() {
        return getFirstRequest(mDownRequest);
    }

    public int getFirstUpRequest() {
        return getFirstRequest(mUpRequest);
    }

    private boolean traverseQueue(boolean[] queue) {
        for (int i = 0; i < queue.length; i++) {
            if (queue[i] == true)
                return true;
        }
        return false;
    }

    private int getFirstRequest(boolean[] queue) {
        int firstRequestedFloor = -1;
        for (int i = 0; i < queue.length; i++) {
            if (queue[i] == true)
                firstRequestedFloor = i;
        }
        return firstRequestedFloor;
    }


}

