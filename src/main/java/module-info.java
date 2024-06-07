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
    requires java.logging;
    requires java.sql;
    requires java.scripting;

    opens ps.psunset.cloudlauncher to javafx.fxml;
    exports ps.psunset.cloudlauncher;
    exports ps.psunset.cloudlauncher.util;
    opens ps.psunset.cloudlauncher.util to javafx.fxml;
    exports ps.psunset.cloudlauncher.js;
    opens ps.psunset.cloudlauncher.js to javafx.fxml;
    exports ps.psunset.cloudlauncher.client;
    opens ps.psunset.cloudlauncher.client to javafx.fxml;
    exports ps.psunset.cloudlauncher.client.helper;
    opens ps.psunset.cloudlauncher.client.helper to javafx.fxml;

    exports mjson;
    exports ps.psunset.cloudlauncher.util.path;
    opens ps.psunset.cloudlauncher.util.path to javafx.fxml;
}