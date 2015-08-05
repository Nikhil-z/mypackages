
package android.com.konka.dvb.casys.data;

public class CaEmailHeadsInfo {
    public short sEmailHeadsState;

    public short sCount;

    public short sFromIndex;

    public CaEmailHeadInfo[] EmailHeads;

    public CaEmailHeadsInfo() {
        sEmailHeadsState = 0;
        sCount = 0;
        sFromIndex = 0;
        for (int i = 0; i < EmailHeads.length; i++) {
            EmailHeads[i] = new CaEmailHeadInfo();
        }

    }

    public short getsEmailHeadsState() {
        return sEmailHeadsState;
    }

    public void setsEmailHeadsState(short sEmailHeadsState) {
        this.sEmailHeadsState = sEmailHeadsState;
    }

    public short getsCount() {
        return sCount;
    }

    public void setsCount(short sCount) {
        this.sCount = sCount;
    }

    public short getsFromIndex() {
        return sFromIndex;
    }

    public void setsFromIndex(short sFromIndex) {
        this.sFromIndex = sFromIndex;
    }

    public CaEmailHeadInfo[] getEmailHeads() {
        return EmailHeads;
    }

    public void setEmailHeads(CaEmailHeadInfo[] emailHeads) {
        EmailHeads = emailHeads;
    }
}
