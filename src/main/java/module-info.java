module ps.psunset.cloudlauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires jdk.jsobject;
    requires com.gluonhq.attach.util;
    requires java.desktop;
    requires com.google.gson;
    requires jdk.jdi;

    opens ps.psunset.cloudlauncher to javafx.fxml;
    exports ps.psunset.cloudlauncher;
    exports ps.psunset.cloudlauncher.util;
    opens ps.psunset.cloudlauncher.util to javafx.fxml;
    exports ps.psunset.cloudlauncher.js;
    opens ps.psunset.cloudlauncher.js to javafx.fxml;
    exports ps.psunset.cloudlauncher.client;
    opens ps.psunset.cloudlauncher.client to javafx.fxml;
}