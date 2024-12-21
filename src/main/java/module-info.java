module com.example.rpg {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires transitive javafx.graphics;

    opens com.example.rpg to javafx.fxml;
    exports com.example.rpg;
}
