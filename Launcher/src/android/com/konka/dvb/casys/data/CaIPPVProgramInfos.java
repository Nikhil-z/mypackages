
package android.com.konka.dvb.casys.data;

public class CaIPPVProgramInfos {
    public short sIPPVInfoState;

    public short sNumber;

    public CaIPPVProgramInfo[] IPPVProgramInfo = new CaIPPVProgramInfo[300];

    public CaIPPVProgramInfos() {
        sIPPVInfoState = 0;
        sNumber = 0;
        for (int i = 0; i < 300; i++) {
            IPPVProgramInfo[i] = new CaIPPVProgramInfo();
        }
    }
}


