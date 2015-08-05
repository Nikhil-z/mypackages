package android.com.konka.dvb;

import android.os.Parcel;

public abstract interface IDVBListener {

	/**
	 * @param EventID
	 * @param EventContent
	 */
	public void NotifyEvent(int EventID, int From, int To,int param0, int param1, int param2,int param3, int param4);
}
