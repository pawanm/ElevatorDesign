package entities;

public class ElevatorDoor {

    private boolean mIsOpen;

    public void openDoors() {
        mIsOpen = true;
    }

    public void closeDoors() {
        mIsOpen = false;
    }

    public boolean areDoorsOpen() {
        return mIsOpen;
    }
}
