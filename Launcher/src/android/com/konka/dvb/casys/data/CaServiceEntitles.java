
package android.com.konka.dvb.casys.data;

public class CaServiceEntitles {
    public short sEntitlesState;

    public short sProductCount;

    public CaServiceEntitle[] cEntitles = new CaServiceEntitle[300];

    public CaServiceEntitles() {
        sEntitlesState = 0;
        sProductCount = 0;
        for (int i = 0; i < 300; i++) {
            cEntitles[i] = new CaServiceEntitle();
        }
    }
}


