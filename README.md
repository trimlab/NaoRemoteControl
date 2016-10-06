# Nao Remote Control
This is a remote control app for Nao built using Android.  Currently requires Android 4.4 or above.

A few notes about working with the source code for this application:
- **DO NOT** update the gradle version and **DO NOT** change the SDK compile version above 23.  This causes build errors with the Nao SDK.
- If adding new functionality, try to keep the delay between starting on the remote and execution on the robot to 1 second or less.  Or, use a loader dialog to show progress.  Locking up the app for running commands is not good.

Any questions can be directed to emvasey@mtu.edu.
