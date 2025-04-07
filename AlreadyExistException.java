// This is the custom exception class for handling the case when a requested item already exists.
// Author: Suntanqing FU <sf4009@hw.ac.uk> , Bilawal Hassan <bh3006@hw.ac.uk>
// Version: 0.01
// Since: 2025-02-09

public class AlreadyExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public AlreadyExistException(String message) {
        super(message + " already exists. Please check.");
    }
}
