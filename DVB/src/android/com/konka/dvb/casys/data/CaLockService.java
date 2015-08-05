
package android.com.konka.dvb.casys.data;


public class CaLockService {
    public int m_dwFrequency;

    public int m_symbol_rate;

    public short m_wPcrPid;

    public short m_Modulation;

    public short m_ComponentNum;

    public CaComponent[] m_CompArr = new CaComponent[5];

    public short m_fec_outer;

    public short m_fec_inner;

    public CaLockService() {
        m_dwFrequency = 0;
        m_symbol_rate = 0;
        m_wPcrPid = 0;
        m_Modulation = 0;
        m_ComponentNum = 0;
        for (int i = 0; i < 5; i++) {
            m_CompArr[i] = new CaComponent();
        }
    }
}
