
package android.com.konka.dvb.casys.data;

public class CaEmailContentInfo {
    public short sEmailContentState;

    public String pcEmailContent; // max size is 160

    public CaEmailContentInfo() {
        sEmailContentState = 0;
        pcEmailContent = "";
    }

    public short getsEmailContentState() {
        return sEmailContentState;
    }

    public void setsEmailContentState(short sEmailContentState) {
        this.sEmailContentState = sEmailContentState;
    }

    public String getPcEmailContent() {
        return pcEmailContent;
    }

    public void setPcEmailContent(String pcEmailContent) {
        this.pcEmailContent = pcEmailContent;
    }
}
