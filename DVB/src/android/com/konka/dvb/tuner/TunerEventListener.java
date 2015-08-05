package android.com.konka.dvb.tuner;

public interface TunerEventListener {

	boolean onTunerLockStatus(int freq, int flag);
}
