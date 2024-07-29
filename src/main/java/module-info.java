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

    opens com.psunset.cloudlauncher to javafx.fxml;
    exports com.psunset.cloudlauncher;
    exports com.psunset.cloudlauncher.util;
    opens com.psunset.cloudlauncher.util to javafx.fxml;
    exports com.psunset.cloudlauncher.frontend;
    opens com.psunset.cloudlauncher.frontend to javafx.fxml;
    exports com.psunset.cloudlauncher.client;
    opens com.psunset.cloudlauncher.client to javafx.fxml;
    exports com.psunset.cloudlauncher.client.helper;
    opens com.psunset.cloudlauncher.client.helper to javafx.fxml;

    exports mjson;
    exports com.psunset.cloudlauncher.util.path;
    opens com.psunset.cloudlauncher.util.path to javafx.fxml;
    exports com.psunset.cloudlauncher.database;
    opens com.psunset.cloudlauncher.database to javafx.fxml;
    exports com.psunset.cloudlauncher.util.bundle;
    opens com.psunset.cloudlauncher.util.bundle to javafx.fxml;
}