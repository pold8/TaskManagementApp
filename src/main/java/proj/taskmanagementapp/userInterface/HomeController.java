package proj.taskmanagementapp.userInterface;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
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

    public HomeController() {
    }

    @FXML
    private void initialize() {
        this.employeeColumn.setCellValueFactory((cellData) -> {
            return Bindings.createObjectBinding(() -> {
                return ((Employee)((Pair)cellData.getValue()).getKey()).getIdEmployee();
            }, new Observable[0]);
        });
        this.taskColumn.setCellValueFactory((cellData) -> {
            return Bindings.createObjectBinding(() -> {
                return ((Employee)((Pair)cellData.getValue()).getKey()).getName();
            }, new Observable[0]);
        });
        this.taskIdColumn.setCellValueFactory((cellData) -> {
            return Bindings.createObjectBinding(() -> {
                return ((Task)((Pair)cellData.getValue()).getValue()).getIdTask();
            }, new Observable[0]);
        });
        this.estimatedDurationColumn.setCellValueFactory((cellData) -> {
            return Bindings.createObjectBinding(() -> {
                Task task = (Task)((Pair)cellData.getValue()).getValue();
                int duration = task.estimateDuration();
                return "" + duration + " hrs";
            }, new Observable[0]);
        });
        this.typeColumn.setCellValueFactory((cellData) -> {
            return Bindings.createObjectBinding(() -> {
                Task task = (Task)((Pair)cellData.getValue()).getValue();
                return task instanceof ComplexTask ? "Complex Task" : "Simple Task";
            }, new Observable[0]);
        });
        this.loadEmployeeTasks();
    }

    private void loadEmployeeTasks() {
        Map<Employee, List<Task>> tasksMap = TaskManagement.getEmployeeTasks();
        if (tasksMap == null) {
            System.out.println("Error: TasksManagement.getEmployeeTasks() returned null!");
        } else {
            ObservableList<Pair<Employee, Task>> taskEntries = FXCollections.observableArrayList();
            Iterator var3 = tasksMap.entrySet().iterator();

            while(true) {
                Employee employee;
                List tasks;
                do {
                    if (!var3.hasNext()) {
                        this.employeeTaskTable.setItems(taskEntries);
                        this.employeeTaskTable.refresh();
                        return;
                    }

                    Map.Entry<Employee, List<Task>> entry = (Map.Entry)var3.next();
                    employee = (Employee)entry.getKey();
                    tasks = (List)entry.getValue();
                } while(tasks == null);

                Iterator var7 = tasks.iterator();

                while(var7.hasNext()) {
                    Task task = (Task)var7.next();
                    taskEntries.add(new Pair(employee, task));
                }
            }
        }
    }

    @FXML
    private void ManageEmployeesAction(ActionEvent event) {
        this.loadScene("/com/example/assignment_1/manage_employees.fxml");
    }

    @FXML
    private void ModifyTasksAction(ActionEvent event) {
        this.loadScene("/com/example/assignment_1/view_statistics.fxml");
    }

    @FXML
    private void ViewStatisticsAction(ActionEvent event) {
        this.loadScene("/com/example/assignment_1/view_statistics.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlFile));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Location is not set for " + fxmlFile);
            }

            AnchorPane newScene = (AnchorPane)loader.load();
            Stage stage = (Stage)this.manageEmployees.getScene().getWindow();
            stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

    }
}