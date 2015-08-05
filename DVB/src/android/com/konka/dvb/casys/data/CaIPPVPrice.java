
package android.com.konka.dvb.casys.data;

public class CaIPPVPrice{

    public short m_wPrice;

    public short m_byPriceCode;

    public CaIPPVPrice() {
        m_wPrice = 0;
        m_byPriceCode = 0;
    }

    public short getM_wPrice() {
        return m_wPrice;
    }

    public void setM_wPrice(short m_wPrice) {
        this.m_wPrice = m_wPrice;
    }

    public short getM_byPriceCode() {
        return m_byPriceCode;
    }

    public void setM_byPriceCode(short m_byPriceCode) {
        this.m_byPriceCode = m_byPriceCode;
    }
}
