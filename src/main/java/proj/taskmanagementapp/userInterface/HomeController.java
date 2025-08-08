package proj.taskmanagementapp.userInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import proj.taskmanagementapp.businessLogic.TaskManagement;
import proj.taskmanagementapp.dataModel.ComplexTask;
import proj.taskmanagementapp.dataModel.Employee;
import proj.taskmanagementapp.dataModel.Task;

import java.util.List;
import java.util.Map;

public class HomeController {

    @FXML
    private Button manageEmployees;

    @FXML
    private Button modifyTasks;

    @FXML
    private Button viewStatististics;

    @FXML
    private TableView<Pair<Employee, Task>> employeeTaskTable;

    @FXML
    private TableColumn<Pair<Employee, Task>, Integer> employeeColumn;

    @FXML
    private TableColumn<Pair<Employee, Task>, String> taskColumn;

    @FXML
    private TableColumn<Pair<Employee, Task>, Integer> taskIdColumn;

    @FXML
    private TableColumn<Pair<Employee, Task>, String> estimatedDurationColumn;

    @FXML
    private TableColumn<Pair<Employee, Task>, String> typeColumn;


    @FXML
    private void initialize() {
        // Set up table columns
        employeeColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(
                () -> cellData.getValue().getKey().getIdEmployee()
        ));
        taskColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(
                () -> cellData.getValue().getKey().getName()
        ));
        taskIdColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(
                () -> cellData.getValue().getValue().getIdTask()
        ));
        estimatedDurationColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(
                () -> {
                    Task task = cellData.getValue().getValue();
                    int duration = task.estimateDuration();  // Calculate the duration
                    return duration + " hrs";  // Add "hrs" after the duration
                }
        ));

        typeColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(
                () -> {
                    Task task = cellData.getValue().getValue();
                    if (task instanceof ComplexTask) {
                        return "Complex Task"; // Example type for ComplexTask
                    } else {
                        return "Simple Task"; // Example type for other tasks
                    }
                }
        ));

        // Load data into table
        loadEmployeeTasks();
    }

    private void loadEmployeeTasks() {
        Map<Employee, List<Task>> tasksMap = TaskManagement.getEmployeeTasks();

        if (tasksMap == null) {
            System.out.println("Error: TasksManagement.getEmployeeTasks() returned null!");
            return;
        }

        ObservableList<Pair<Employee, Task>> taskEntries = FXCollections.observableArrayList();

        for (Map.Entry<Employee, List<Task>> entry : tasksMap.entrySet()) {
            Employee employee = entry.getKey();
            List<Task> tasks = entry.getValue();

            if (tasks != null) {
                for (Task task : tasks) {
                    taskEntries.add(new Pair<>(employee, task));
                }
            }
        }

        employeeTaskTable.setItems(taskEntries);
        employeeTaskTable.refresh(); // Refresh the table to reflect updated data
    }


    @FXML
    private void ManageEmployeesAction(ActionEvent event) {
        loadScene("/proj/taskmanagementapp/manage_employees.fxml");
    }

    @FXML
    private void ModifyTasksAction(ActionEvent event) {
        loadScene("/proj/taskmanagementapp/view_statistics.fxml");
    }

    @FXML
    private void ViewStatisticsAction(ActionEvent event) {
        loadScene("/proj/taskmanagementapp/view_statistics.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            // Use a relative path to load the FXML files
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Location is not set for " + fxmlFile);
            }
            AnchorPane newScene = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) manageEmployees.getScene().getWindow();
            stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}