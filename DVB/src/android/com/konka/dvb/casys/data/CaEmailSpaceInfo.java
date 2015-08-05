
package android.com.konka.dvb.casys.data;

public class CaEmailSpaceInfo {
    public short saEmailSpaceState;

    public short sEmailNum;

    public short sEmptyNum;

    public CaEmailSpaceInfo() {
        saEmailSpaceState = 0;
        sEmailNum = 0;
        sEmptyNum = 0;
    }

    public short getSaEmailSpaceState() {
        return saEmailSpaceState;
    }

    public void setSaEmailSpaceState(short saEmailSpaceState) {
        this.saEmailSpaceState = saEmailSpaceState;
    }

    public short getsEmailNum() {
        return sEmailNum;
    }

    public void setsEmailNum(short sEmailNum) {
        this.sEmailNum = sEmailNum;
    }

    public short getsEmptyNum() {
        return sEmptyNum;
    }

    public void setsEmptyNum(short sEmptyNum) {
        this.sEmptyNum = sEmptyNum;
    }
}
