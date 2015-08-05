package com.konka.upgrade.exception;

public class LoaderException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -6670675307756113954L;

    public LoaderException() {
        super("Loader Init failed");
    }
    
    public LoaderException(String msg) {
        super(msg);
    }
}
