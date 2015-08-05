
package android.com.konka.dvb.casys.exception;

/**
 * <p>
 * Title: CaCommonException
 * </p>
 * <p>
 * Description: This exception is thrown when a call to tvos java api failed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Mstarsemi Inc.
 * </p>
 * 
 * @author Jacky.Lin
 * @version 1.0
 */
public class CaCommonException extends Exception implements CaExceptionConstant {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CaCommonException(String a_sExceptionMsg) {
        super(a_sExceptionMsg);
    }

    public CaCommonException(String a_sExceptionMsg, Exception e) {
        super(a_sExceptionMsg, e);
    }

    public String getMessage() {
        return CaExceptionConstant.EXCEPTION_MSG_COMMON;
    }
}
