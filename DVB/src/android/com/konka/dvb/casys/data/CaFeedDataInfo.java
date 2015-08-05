
package android.com.konka.dvb.casys.data;

public class CaFeedDataInfo {
    public short sFeedDataState;

    public short sdataLen;

    public String pbyFeedData; // max size is 255

    public CaFeedDataInfo() {
        sFeedDataState = 0;
        sdataLen = 0;
        pbyFeedData = "";
    }
}


