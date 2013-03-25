package com.jbilbo.separate_ringtone_volume;

public class Utils {

	// Convert from a scale of 0-100 to a f.e. 0-7.
	public static int calculatePercentVolume(int volume, int maxVolume) {
		return (int) (volume*maxVolume)/100;
	}
	
}
