package com.jbilbo.separate_ringtone_volume;

import android.media.AudioManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;

public class Settings extends Activity {

	private static final String TAG = "Settings";
	
	public static final String PREF_NAME="Settings";
	public static final String PREF_ACTIVATED = "activated";
	public static final String PREF_VOLUME_CALL = "volume";
	public static final String PREF_VIBRATE = "vibrate";
	public static final boolean DEFAULT_ACTIVATED = false;
	public static final int DEFAULT_VOLUME_CALL = 90;
	public static final boolean DEFAULT_VIBRATE = true;
	
	private CheckBox checkEnabled;
	private SeekBar volumeCallBar;
	private CheckBox checkVibrate;
	
	private boolean mActivated;
	private int mVolumeCall;
	private boolean mVibrate;
	
	private SharedPreferences preferences;
	private Editor editor;
	private AudioManager audio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		editor = preferences.edit();
		audio = (AudioManager) getSystemService(AUDIO_SERVICE);
		
		checkEnabled = (CheckBox)findViewById(R.id.checkBox1);
		volumeCallBar = (SeekBar)findViewById(R.id.seekBar1);
		checkVibrate = (CheckBox)findViewById(R.id.checkBox2);
		
		checkEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mActivated = isChecked;
		    	if (mActivated) {
		    		if (mVibrate)
		    			audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		    		else
		    			audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);	
		    	} else {
		    		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		    	}
				editor.putBoolean(PREF_ACTIVATED, mActivated);
				editor.commit();
				setEnabledCallReceiver();
			}
		});
		
		volumeCallBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mVolumeCall = progress;
				editor.putInt(PREF_VOLUME_CALL, mVolumeCall);
				editor.commit();
			}
		});
		
		checkVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mVibrate = isChecked;
		    	if (mVibrate) {
		        	audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		    	} else {
		    		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		    	}
				editor.putBoolean(PREF_VIBRATE, mVibrate);
				editor.commit();
			}
		});
		
	}
	
	private void setEnabledControls() {
		volumeCallBar.setEnabled(mActivated);
		checkVibrate.setEnabled(mActivated);
	}
	
	private void setEnabledCallReceiver() {
		int flag = (mActivated ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
		ComponentName component = new ComponentName(Settings.this, CallReceiver.class);
		setEnabledControls();
		getPackageManager().setComponentEnabledSetting(component, flag, PackageManager.DONT_KILL_APP);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mActivated = preferences.getBoolean(PREF_ACTIVATED, DEFAULT_ACTIVATED);
		mVolumeCall = preferences.getInt(PREF_VOLUME_CALL, DEFAULT_VOLUME_CALL);
		mVibrate = preferences.getBoolean(PREF_VIBRATE, DEFAULT_VIBRATE);
		checkEnabled.setChecked(mActivated);
		volumeCallBar.setProgress(mVolumeCall);
		checkVibrate.setChecked(mVibrate);
		
		if (mActivated) {
			setEnabledCallReceiver();
		}
		setEnabledControls();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		editor.putBoolean(PREF_ACTIVATED, mActivated);
		editor.putInt(PREF_VOLUME_CALL, mVolumeCall);
		editor.commit();
		
	}

}
