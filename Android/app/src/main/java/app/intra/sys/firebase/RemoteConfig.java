/*
Copyright 2019 Jigsaw Operations LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package app.intra.sys.firebase;

import android.os.Build;
import android.os.Build.VERSION;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Utility class for initializing Firebase Remote Config.  Remote Configuration allows us to conduct
 * A/B tests of experimental functionality, and to enable or disable features without having to
 * release a new version of the app.
 */
public class RemoteConfig {
  public static Task<Boolean> update() {
    try {
      FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
      return config.fetchAndActivate();
    } catch (IllegalStateException e) {
      LogWrapper.logException(e);
      return Tasks.forResult(false);
    }
  }

  public static String[] getTlsProbeServers() {
    try {
      return FirebaseRemoteConfig.getInstance()
          .getString("tls_probe_servers").split(",");
    } catch (IllegalStateException e) {
      LogWrapper.logException(e);
      return new String[0];
    }
  }

  public static boolean getUseGoDoh() {
    return true;
    /*
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      // Must be kept in sync with the check in IntraVpnService.makeVpnAdapter.
      // GoVpnAdapter isn't used on versions below M, so Go-DOH shouldn't be used for other purposes
      // (especially probes) on those versions either.
      return false;
    }
    try {
      return FirebaseRemoteConfig.getInstance()
          .getBoolean("use_go_doh");
    } catch (IllegalStateException e) {
      LogWrapper.logException(e);
      return false;
    }
     */
  }

  public static boolean forceGoMode() {
    try {
      return VERSION.SDK_INT >= FirebaseRemoteConfig.getInstance()
          .getLong("go_min_version");
    } catch (IllegalStateException e) {
      LogWrapper.logException(e);
      return false;
    }
  }
}
