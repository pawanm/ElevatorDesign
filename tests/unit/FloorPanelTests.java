package unit;


import entities.Elevator;
import entities.IElevator;
import entities.floor.FloorPanel;
import enums.ElevatorDirection;
import handlers.IFloorPanelButtonHandler;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class FloorPanelTests {
    private IElevator mElevator;
    private FloorPanel mFloorPanel;
    private boolean mCaughtEvent = false;


    @Before
    public void setUp() {
        mElevator = new Elevator();
        mFloorPanel = new FloorPanel(mElevator, 3);
    }

    @Test
    public void testFloorPanelInitializedAndShowingAsThree() {
        Assert.assertEquals(mFloorPanel.getFloorPanelFloorNumber(), 3);
    }

    @Test
    public void testElevatorFloorNumberIsDisplayedOnFloorPanelDisplay() {
        Assert.assertEquals(mElevator.getCurrentFloor(), mFloorPanel.getFloorPanelDisplay().getElevatorCurrentFloorNumber());
    }

    @Test
    public void testFloorPanelButtonPressIlluminatesButton() {
        mFloorPanel.getDownDirectionButton().press();
        Assert.assertTrue(mFloorPanel.getDownDirectionButton().getIsIlluminated());
    }

    @Test
    public void testFloorPanelUpDirectionButtonPressRaisesEvent() {
        mFloorPanel.getUpDirectionButton().setFloorPanelButtonHandler(new IFloorPanelButtonHandler() {
            @Override
            public void onButtonPressed() {
                mCaughtEvent = true;
            }
        });

        mFloorPanel.getUpDirectionButton().press();
        Assert.assertTrue(mCaughtEvent);
    }

    @Test
    public void testFloorPanelUpDirectionButtonNotRaisingEventForDownButton() {
        mCaughtEvent = false;
        mFloorPanel.getDownDirectionButton().setFloorPanelButtonHandler(new IFloorPanelButtonHandler() {
            @Override
            public void onButtonPressed() {
                mCaughtEvent = true;
            }
        });

        mFloorPanel.getUpDirectionButton().press();
        Assert.assertFalse(mCaughtEvent);
    }

}
