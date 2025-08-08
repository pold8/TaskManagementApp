package proj.taskmanagementapp;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import proj.taskmanagementapp.dataAccess.TaskSerialization;

public class HelloApplication extends Application {
    public HelloApplication() {
    }

    public void start(Stage stage) throws IOException {
        TaskSerialization.deserialize();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/assignment_1/home.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load(), 1200.0, 700.0);
        stage.setTitle("Task Manager");
        stage.setScene(scene);
        stage.setOnCloseRequest(this::handleExit);
        stage.show();
    }

    private void handleExit(WindowEvent event) {
        System.out.println("Saving data before exiting...");
        TaskSerialization.serialize();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}