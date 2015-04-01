package unit;


import configuration.Configuration;
import entities.Elevator;
import entities.IElevator;
import enums.ElevatorDirection;
import exception.InvalidFloorException;
import handlers.IElevatorHandler;
import handlers.IFloorPanelHandler;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElevatorTests {

    private IElevator mElevator;
    private boolean mElevatorStartedAssertFlag;
    private boolean mRequestReceivedAssertFlag;
    private boolean mOnFloorAssertFlag;

    @Before
    public void setUp() {
        mElevator = new Elevator();
    }

    @Test
    public void testMakeRequestForTopFloor() {
        makeElevatorRequest(Configuration.TOP_FLOOR);
        Assert.assertEquals(mElevator.getCurrentFloor(), Configuration.TOP_FLOOR);
    }

    @Test
    public void testMakeRequestForGroundFloor() {
        makeElevatorRequest(Configuration.GROUND_FLOOR);
        Assert.assertEquals(mElevator.getCurrentFloor(), Configuration.GROUND_FLOOR);
    }

    @Test(expected = InvalidFloorException.class)
    public void testMakeRequestForInvalidFloorShouldThrowException() throws InvalidFloorException {
        mElevator.makeRequest(16);
    }

    @Test
    public void testElevatorIsRaisedStartEvent() {
        mElevator.addElevatorCallback(new IElevatorHandler() {
            @Override
            public void elevatorStarted() {
                mElevatorStartedAssertFlag = true;
            }
        });

        makeElevatorRequest(1);
        Assert.assertTrue(mElevatorStartedAssertFlag);
    }

    @Test
    public void testElevatorIsRequestReceivedAndOnFloorEventRaised() {
        mElevator.addFloorPanelCallback(new IFloorPanelHandler() {
            @Override
            public void reachedToFloor(int currentFloor) {

            }

            @Override
            public void onFloor(int currentFloor) {
                mOnFloorAssertFlag = true;
            }

            @Override
            public void requestReceived(int floorNum, ElevatorDirection direction) {
                mRequestReceivedAssertFlag = true;
            }
        });

        makeElevatorRequest(5);
        Assert.assertTrue(mRequestReceivedAssertFlag);
        Assert.assertTrue(mOnFloorAssertFlag);
        Assert.assertEquals(5, mElevator.getCurrentFloor());
    }


    private void makeElevatorRequest(int floorNo) {
        try {
            mElevator.makeRequest(floorNo);
        } catch (InvalidFloorException ex) {
        }

    }


}
