
package android.com.konka.dvb.casys.data;

public class CaOperatorChildStatus {
    public short sOperatorChildState;

    public short sIsChild;

    public short sDelayTime;

    public int wLastFeedTime;

    public String pParentCardSN;// max size is 16

    public boolean bIsCanFeed;

    public CaOperatorChildStatus() {
        sOperatorChildState = 0;
        sIsChild = 0;
        sDelayTime = 0;
        wLastFeedTime = 0;
        pParentCardSN = "";
        bIsCanFeed = false;
    }
}
