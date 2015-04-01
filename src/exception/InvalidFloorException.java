package exception;


import configuration.Configuration;

public class InvalidFloorException extends Exception {

    public InvalidFloorException() {
        super(Configuration.INVALID_FLOOR_MSG);
    }

}
