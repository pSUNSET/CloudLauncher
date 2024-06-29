package com.psunset.cloudlauncher.util.bundle;

import com.psunset.cloudlauncher.util.Constants;

import java.text.MessageFormat;
import java.util.*;

/**
 * Find Output Message in .properties file
 */
public class OutputHelper {

    // Code in Fabric Installer with JDK-1.8

    /*
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("assets/lang/l", Locale.getDefault(), new ResourceBundle.Control() {
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties").toLowerCase(Locale.ROOT);
            InputStream stream = loader.getResourceAsStream(resourceName);
            try {
                if (stream != null) {
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    try {
                        PropertyResourceBundle propertyResourceBundle = new PropertyResourceBundle(reader);
                        reader.close();
                        if (stream != null)
                            stream.close();
                        return propertyResourceBundle;
                    } catch (Throwable throwable) {
                        try {
                            reader.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                        throw throwable;
                    }
                }
                if (stream != null)
                    stream.close();
            } catch (Throwable throwable) {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                throw throwable;
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    });

     */

    /**
     * Find Output Message in /assets/lang/launcher_??_??.properties file
     *
     * @param key
     * @param locale
     * @param objects
     * @return
     */
    public static String getOutputMessage(String key, Locale locale, Object... objects) {
        ResourceBundle messages = ResourceBundle.getBundle("assets/lang/launcher", locale);
        MessageFormat formatter = new MessageFormat(messages.getString(key));
        return formatter.format(objects);
    }

    /**
     * Find Output Message in /assets/lang/launcher_??_??.properties file with Launcher Language
     *
     * @param key
     * @param objects
     * @return
     */
    public static String getOutputMessage(String key, Object... objects) {
        return getOutputMessage(key, Constants.getLocale(), objects);
    }
}
