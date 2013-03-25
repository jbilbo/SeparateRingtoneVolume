package com.jbilbo.separate_ringtone_volume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {

	private static final String TAG = "CallReceiver";
	
	private TelephonyManager mTelephonyManager;
	private AudioManager audio;
	private SharedPreferences preferences;
	
	@Override
	public void onReceive(Context context, Intent intent) {
    	preferences = context.getSharedPreferences(Settings.PREF_NAME, Context.MODE_PRIVATE);
    	if (preferences.getBoolean(Settings.PREF_ACTIVATED, Settings.DEFAULT_ACTIVATED)) {
			mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
			audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	    	
    	}
}
	
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	        super.onCallStateChanged(state, incomingNumber);
	        
	        switch (state) {
	        case TelephonyManager.CALL_STATE_RINGING:
        		setCustomVolume();
	        	break;
	        case TelephonyManager.CALL_STATE_OFFHOOK:
	        	break;
	        case TelephonyManager.CALL_STATE_IDLE:
        		setPreviousVolume();
        		mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
	        	break;
	        }
	    }
	};
	
	private void setCustomVolume() {
    	int volumeCall = preferences.getInt(Settings.PREF_VOLUME_CALL, Settings.DEFAULT_VOLUME_CALL);
    	
    	int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
    	int calculatedVolume = Utils.calculatePercentVolume(volumeCall, maxVolume);
    	audio.setStreamVolume(AudioManager.STREAM_RING, calculatedVolume, 0);
	}
	
	private void setPreviousVolume() {
		if (preferences.getBoolean(Settings.PREF_VIBRATE, Settings.DEFAULT_VIBRATE)) {
			audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);			
		} else {
			audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
	}

}
