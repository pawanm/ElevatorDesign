package controllers;

import handlers.IFloorPanelHandler;
import configuration.Configuration;
import entities.Elevator;
import entities.ElevatorDoor;
import entities.IElevator;
import handlers.IDoorHandler;
import enums.ElevatorDirection;

public class ElevatorDoorController {

    private ElevatorDoor mElevatorDoor = null;
    private IElevator mElevator = null;
    private IDoorHandler mDoorHandler = new IDoorHandler() {
        @Override
        public void openDoor() {
            //OpenDoor
        }

        @Override
        public void closeDoor() {
            //CloseDoor
        }
    };

    public ElevatorDoorController(Elevator elevator) {
        mElevator = elevator;
        mElevatorDoor = new ElevatorDoor();
        mElevator.addFloorPanelCallback(getPanelFloorHandlerCallback());
    }

    public void openDoorForFixedTimeIntervalAndThenClose() {
        openDoor();
        sleep();
        closeDoor();
    }


    private IFloorPanelHandler getPanelFloorHandlerCallback() {
        return new IFloorPanelHandler() {
            @Override
            public void reachedToFloor(int currentFloor) {
                openDoorForFixedTimeIntervalAndThenClose();
            }

            @Override
            public void onFloor(int currentFloor) {

            }

            @Override
            public void requestReceived(int floorNum, ElevatorDirection direction) {

            }
        };
    }


    public void openDoor() {
        if (!mElevator.getIsMoving()) {
            mElevatorDoor.openDoors();
        }
        mDoorHandler.openDoor();
    }

    public void closeDoor() {
        mElevatorDoor.closeDoors();
        mDoorHandler.closeDoor();
    }

    private void sleep() {
        try {
            Thread.sleep(Configuration.DOOR_OPEN_SEC * 1000);
        } catch (Exception ex) {
        }
    }
}

