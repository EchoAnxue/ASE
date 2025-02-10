// This is the custom exception class for handling the case when a requested item is not found.
// @author <Suntanqing FU> <sf4009@hw.ac.uk>
// @version 0.01
// @since 2025-02-09
public class NotFoundException extends Exception {
    
    public NotFoundException (String message) {
        super(message+" is not available. Please check.");
    }
    
}
