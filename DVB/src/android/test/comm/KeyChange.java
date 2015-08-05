package android.test.comm;

import android.view.KeyEvent;

public class KeyChange
{
	static KeyChange keyinstance = null;

	final int konka_ir_keymap[][] = {
//			{ 0x52, KeyEvent.KEYCODE_M },// ,
//			{ 0x17, KeyEvent.KEYCODE_ENTER },//
//			{ 0x18, KeyEvent.KEYCODE_VOLUME_UP },// ,
//			{ 0x19, KeyEvent.KEYCODE_VOLUME_DOWN },//
//			{ 0x5B, KeyEvent.KEYCODE_MUTE },// ,
//			{ 0x77, KeyEvent.KEYCODE_S },//
//			{ 0x7a, KeyEvent.KEYCODE_DPAD_RIGHT },// ,
//			{ 0x79, KeyEvent.KEYCODE_DPAD_LEFT },//
//			{ 0xD8, KeyEvent.KEYCODE_GUIDE },/* EPG */
//			{ 0xD6, KeyEvent.KEYCODE_A },/* AUDIO */
//			{ 0xD5, KeyEvent.KEYCODE_M },/* MTS */
//			{ 0xDA, KeyEvent.KEYCODE_T },/* SUBTITLE */
			{0xFF,KeyEvent.KEYCODE_UNKNOWN},
	};

	public static KeyChange getInstance()
	{
		if (keyinstance == null)
			keyinstance = new KeyChange();
		return keyinstance;
	}

	public int translateIRKey(int key)
	{
		int value = key;
		int keymapsize = konka_ir_keymap.length;
		int i = 0;
		for (i = 0; i < keymapsize; i++)
		{
			if (key == konka_ir_keymap[i][0])
			{
				value = konka_ir_keymap[i][1];
				break;
			}
		}
		return value;
	}
}