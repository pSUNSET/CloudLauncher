module ps.psunset.cloudlauncher {
    requires javafx.controls;

    requires jdk.jsobject;
    requires java.desktop;
    requires com.google.gson;
    requires jdk.jdi;
    requires org.apache.commons.io;
    requires javafx.web;
    requires java.security.jgss;
    requires java.compiler;
    requires jdk.sctp;

    opens ps.psunset.cloudlauncher to javafx.fxml;
    exports ps.psunset.cloudlauncher;
    exports ps.psunset.cloudlauncher.util;
    opens ps.psunset.cloudlauncher.util to javafx.fxml;
    exports ps.psunset.cloudlauncher.js;
    opens ps.psunset.cloudlauncher.js to javafx.fxml;
    exports ps.psunset.cloudlauncher.client;
    opens ps.psunset.cloudlauncher.client to javafx.fxml;
    exports ps.psunset.cloudlauncher.util.bundle;
    opens ps.psunset.cloudlauncher.util.bundle to javafx.fxml;
}