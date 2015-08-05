
package android.com.konka.dvb.casys.data;

public class CaWorkTimeInfo {
    public short sWorkTimeState;

    public short sStartHour;

    public short syStartMin;

    public short syStartSec;

    public short syEndHour;

    public short syEndMin;

    public short syEndSec;

    public CaWorkTimeInfo() {
        sWorkTimeState = -1;
        sStartHour = 0;
        syStartMin = 0;
        syStartSec = 0;
        syEndHour = 0;
        syEndMin = 0;
        syEndSec = 0;
    }
}


