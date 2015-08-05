
package android.com.konka.dvb.casys.data;

public class CaStartIPPVBuyDlgInfo{
 
    // public short sStartIppvState;
    public short wyMessageType;

    public short wEcmPid;

    public int dwProductID;

    public short wTvsID;

    public short wySlotID;

    public short wyPriceNum;

    public short wExpiredDate;

    public CaIPPVPrice[] m_Price = new CaIPPVPrice[2];

    public CaStartIPPVBuyDlgInfo() {
        // sStartIppvState = 0;
        dwProductID = 0;
        wySlotID = 0;
        wyPriceNum = 0;
        wExpiredDate = 0;
        for (int i = 0; i < 2; i++) {
            m_Price[i] = new CaIPPVPrice();
        }

    }

    public short getWyMessageType() {
        return wyMessageType;
    }

    public void setWyMessageType(short wyMessageType) {
        this.wyMessageType = wyMessageType;
    }

    public short getwEcmPid() {
        return wEcmPid;
    }

    public void setwEcmPid(short wEcmPid) {
        this.wEcmPid = wEcmPid;
    }

    public int getDwProductID() {
        return dwProductID;
    }

    public void setDwProductID(int dwProductID) {
        this.dwProductID = dwProductID;
    }

    public short getwTvsID() {
        return wTvsID;
    }

    public void setwTvsID(short wTvsID) {
        this.wTvsID = wTvsID;
    }

    public short getWySlotID() {
        return wySlotID;
    }

    public void setWySlotID(short wySlotID) {
        this.wySlotID = wySlotID;
    }

    public short getWyPriceNum() {
        return wyPriceNum;
    }

    public void setWyPriceNum(short wyPriceNum) {
        this.wyPriceNum = wyPriceNum;
    }

    public short getwExpiredDate() {
        return wExpiredDate;
    }

    public void setwExpiredDate(short wExpiredDate) {
        this.wExpiredDate = wExpiredDate;
    }

    public CaIPPVPrice[] getM_Price() {
        return m_Price;
    }

    public void setM_Price(CaIPPVPrice[] m_Price) {
        this.m_Price = m_Price;

    }
}


