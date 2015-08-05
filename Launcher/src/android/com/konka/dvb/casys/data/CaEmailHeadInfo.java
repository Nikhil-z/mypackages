
package android.com.konka.dvb.casys.data;

public class CaEmailHeadInfo {
    public short sEmailHeadState;

    public int wActionID;

    public int wCreateTime;

    public short wImportance;

    public String pcEmailHead;

    public short m_bNewEmail;

    public CaEmailHeadInfo() {
        sEmailHeadState = 0;
        wActionID = 0;
        wCreateTime = 0;
        wImportance = 0;
        pcEmailHead = "";
        m_bNewEmail = 0;
    }

    public short getsEmailHeadState() {
        return sEmailHeadState;
    }

    public void setsEmailHeadState(short sEmailHeadState) {
        this.sEmailHeadState = sEmailHeadState;
    }

    public int getwActionID() {
        return wActionID;
    }

    public void setwActionID(int wActionID) {
        this.wActionID = wActionID;
    }

    public int getwCreateTime() {
        return wCreateTime;
    }

    public void setwCreateTime(int wCreateTime) {
        this.wCreateTime = wCreateTime;
    }

    public short getwImportance() {
        return wImportance;
    }

    public void setwImportance(short wImportance) {
        this.wImportance = wImportance;
    }

    public String getPcEmailHead() {
        return pcEmailHead;
    }

    public void setPcEmailHead(String pcEmailHead) {
        this.pcEmailHead = pcEmailHead;
    }

    public short getM_bNewEmail() {
        return m_bNewEmail;
    }

    public void setM_bNewEmail(short m_bNewEmail) {
        this.m_bNewEmail = m_bNewEmail;
    }
}
