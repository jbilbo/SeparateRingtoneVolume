Separate Ringtone Volume
========================

Since Android 4.0, Google linked by default the ringtone and notifications volume into the same bar. If you have a not default Android with 3rd party software on top (like HTC Sense, Samsung TouchWiz... etc) you are probably not having this problem, but if you have a "Pure Google" Android here is your app.

This app allows you to mute the notifications and control the ringtone volume. It is impossible to do by API, so the *trick* is to register a `BroadcastReceiver` that *listen to* the phone state and just before the Ringing start it turns on the ringtone/notification volume and turn it back to mute after the call. There is no `Service` involved, just the BroadcastReceiver and it only wakes up just before the ringing, so there's no draining battery at all.

[Download it from Google Play](https://play.google.com/store/apps/details?id=com.jbilbo.separate_ringtone_volume)

Credits
-------
Author: Jonathan Hernandez

The code in this project is licensed under the Apache Software License 2.0.
<br />