package ps.psunset.cloudlauncher.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class VersionHelper {
    /**
     * Find Version in /assets/cli-version.properties file
     * @param gameVersion
     * @return
     */
    public static String getClientVersion(String gameVersion) {
        ResourceBundle messages = ResourceBundle.getBundle("assets/cli-version", Locale.getDefault());
        MessageFormat formatter = new MessageFormat(messages.getString(gameVersion));
        return formatter.toPattern();
    }
}
