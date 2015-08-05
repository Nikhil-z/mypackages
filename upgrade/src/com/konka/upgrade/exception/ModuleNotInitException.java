package com.konka.upgrade.exception;

public class ModuleNotInitException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 4383098739636411898L;

    public ModuleNotInitException() {
        super("module has not been initiallized");
    }
    
    public ModuleNotInitException(String msg) {
        super(msg);
    }
}
