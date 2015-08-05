

package android.com.konka.dvb.casys.exception;

/**
 * <p>
 * Title: CaExceptionConstant
 * </p>
 * <p>
 * Description: This class provides the relative constants for the Tv Exception.
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
public interface CaExceptionConstant {
    /** common exception type */
    public static final short EXCEPTION_TYPE_NATIVE_COMMON = 0;

    /** native call exception type */
    public static final short EXCEPTION_TYPE_NATIVE_CALL_FAIL = 1;

    /** ipc exception type */
    public static final short EXCEPTION_TYPE_IPC_FAIL = 2;

    /** jni exception type */
    public static final short EXCEPTION_TYPE_JNI_FAIL = 3;

    /** common exception message */
    public static final String EXCEPTION_MSG_COMMON = "Exception happened ";

    /** native call exception message */
    public static final String EXCEPTION_MSG_NATIVE_CALL_FAIL = "Exception happened in native call!! ";

    /** unsupported exception message */
    public static final String EXCEPTION_MSG_UNSUPPORTED = "Exception happened for unsupported!";
    /** ipc exception message */
    public static final String EXCEPTION_MSG_IPC_FAIL = "Exception happened in ipc!! ";

    /** jni exception message */
    public static final String EXCEPTION_MSG_JNI_FAIL = "Exception happened in jni!! ";

    /** out of bound exception message */
    public static final String EXCEPTION_MSG_OUT_OF_BOUND = "Exception happened in bound";
}
