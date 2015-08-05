
package android.com.konka.dvb.casys.data;

public class CaEntitleIDs {
    public short sEntitleIDsState;

    public int[] pdwEntitleIds = new int[300];

    public CaEntitleIDs() {
        sEntitleIDsState = 0;
        for (int i = 0; i < 300; i++) {
            pdwEntitleIds[i] = 0;
        }
    }
}


