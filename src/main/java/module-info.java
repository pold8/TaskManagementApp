module proj.taskmanagementapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens proj.taskmanagementapp to javafx.fxml;
    exports proj.taskmanagementapp;
    exports proj.taskmanagementapp.userInterface;
    opens proj.taskmanagementapp.userInterface to javafx.fxml;
}