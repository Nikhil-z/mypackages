
package android.com.konka.dvb.casys.data;

public class CaDetitleChkNums {
    public short sDetitleChkNumsState;

    public boolean bReadFlag;

    public int[] pdwEntitleIds = new int[5];

    public CaDetitleChkNums() {
        sDetitleChkNumsState = 0;
        bReadFlag = false;
        for (int i = 0; i < 5; i++) {
            pdwEntitleIds[i] = 0;
        }
    }
}
