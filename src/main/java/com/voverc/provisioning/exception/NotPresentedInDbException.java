package com.voverc.provisioning.exception;


/**
 * @author Nazar Lelyak.
 */
public class NotPresentedInDbException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String STATION_NOT_FOUND_EXCEPTION = "Provisioning Device, with mac address: %s is NOT FOUND in DB.";

    public NotPresentedInDbException(String macAddress) {
        super(String.format(STATION_NOT_FOUND_EXCEPTION, macAddress));
    }
}
