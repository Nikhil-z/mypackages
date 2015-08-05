
package android.com.konka.dvb.casys.data;

public class CaStopIPPVBuyDlgInfo {
    public short sIPPVBuyDlgState;

    public boolean bBuyProgram;

    public short sEcmPid;

    public String pbyPinCode;

    public short sPrice;

    public short sPriceCode;

    public CaStopIPPVBuyDlgInfo() {
        sIPPVBuyDlgState = 0;
        bBuyProgram = false;
        sEcmPid = 0;
        pbyPinCode = "";
        sPrice = 0;
        sPriceCode = 0;

    }

    public short getsIPPVBuyDlgState() {
        return sIPPVBuyDlgState;
    }

    public void setsIPPVBuyDlgState(short sIPPVBuyDlgState) {
        this.sIPPVBuyDlgState = sIPPVBuyDlgState;
    }

    public boolean isbBuyProgram() {
        return bBuyProgram;
    }

    public void setbBuyProgram(boolean bBuyProgram) {
        this.bBuyProgram = bBuyProgram;
    }

    public short getsEcmPid() {
        return sEcmPid;
    }

    public void setsEcmPid(short sEcmPid) {
        this.sEcmPid = sEcmPid;
    }

    public String getPbyPinCode() {
        return pbyPinCode;
    }

    public void setPbyPinCode(String pbyPinCode) {
        this.pbyPinCode = pbyPinCode;
    }

    public short getsPrice() {
        return sPrice;
    }

    public void setsPrice(short sPrice) {
        this.sPrice = sPrice;
    }

    public short getsPriceCode() {
        return sPriceCode;
    }

    public void setsPriceCode(short sPriceCode) {
        this.sPriceCode = sPriceCode;
    }
}


