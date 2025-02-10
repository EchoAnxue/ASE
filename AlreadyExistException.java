// This is the custom exception class for handling the case when a requested item is already exist.
// @author <Suntanqing FU> <sf4009@hw.ac.uk>
// @version 0.01
// @since 2025-02-09
public class AlreadyExistException extends Exception {

    public AlreadyExistException (String message) {
        super(message+" has already exist. Please check.");
    }
}
