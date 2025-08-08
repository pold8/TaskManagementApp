package proj.taskmanagementapp.userInterface;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proj.taskmanagementapp.businessLogic.TaskManagement;
import proj.taskmanagementapp.businessLogic.Utility;
import proj.taskmanagementapp.dataModel.Task;

public class ViewStatisticsController {
    @FXML
    private Button backButton;
    @FXML
    private Button changeStatusButton;
    @FXML
    private TextField taskIdTextField;
    @FXML
    private TextField employeeIdTextField;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<EmployeeData> filterEmployeesTableView;
    @FXML
    private TableColumn<EmployeeData, Integer> filterEmployeeIdColumn;
    @FXML
    private TableColumn<EmployeeData, String> filterEmployeeNameColumn;
    @FXML
    private TableColumn<EmployeeData, Integer> filterEmployeeHoursColumn;
    @FXML
    private TableView<TaskStatisticsData> statisticsEmployeeTableView;
    @FXML
    private TableColumn<TaskStatisticsData, Integer> statisticsEmployeeIdColumn;
    @FXML
    private TableColumn<TaskStatisticsData, String> statisticsEmployeeNameColumn;
    @FXML
    private TableColumn<TaskStatisticsData, Integer> completedTasksColumn;
    @FXML
    private TableColumn<TaskStatisticsData, Integer> uncompletedTasksColumn;

    public ViewStatisticsController() {
    }

    @FXML
    private void initialize() {
        this.setupFilterEmployeesTable();
        this.setupStatisticsTable();
    }

    private void setupFilterEmployeesTable() {
        this.filterEmployeeIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.filterEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.filterEmployeeHoursColumn.setCellValueFactory(new PropertyValueFactory("hoursWorked"));
        List<String> employeeNames = Utility.filterAndSortEmployeesByWorkDuration();
        ObservableList<EmployeeData> data = FXCollections.observableArrayList();
        Iterator var3 = employeeNames.iterator();

        while(var3.hasNext()) {
            String name = (String)var3.next();
            int id = (Integer) TaskManagement.getEmployeeTasks().keySet().stream().filter((e) -> {
                return e.getName().equals(name);
            }).map((e) -> {
                return e.getIdEmployee();
            }).findFirst().orElse(-1);
            int hoursWorked = TaskManagement.calculateEmployeeWorkDuration(id);
            data.add(new EmployeeData(id, name, hoursWorked));
        }

        this.filterEmployeesTableView.setItems(data);
    }

    private void setupStatisticsTable() {
        this.statisticsEmployeeIdColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.statisticsEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.completedTasksColumn.setCellValueFactory(new PropertyValueFactory("completedTasks"));
        this.uncompletedTasksColumn.setCellValueFactory(new PropertyValueFactory("uncompletedTasks"));
        Map<String, Map<String, Integer>> taskStats = Utility.calculateCompletedAndUncompletedTasks();
        ObservableList<TaskStatisticsData> data = FXCollections.observableArrayList();
        Iterator var3 = taskStats.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Map<String, Integer>> entry = (Map.Entry)var3.next();
            String name = (String)entry.getKey();
            int completed = (Integer)((Map)entry.getValue()).getOrDefault("Completed", 0);
            int uncompleted = (Integer)((Map)entry.getValue()).getOrDefault("Uncompleted", 0);
            int id = (Integer)TaskManagement.getEmployeeTasks().keySet().stream().filter((e) -> {
                return e.getName().equals(name);
            }).map((e) -> {
                return e.getIdEmployee();
            }).findFirst().orElse(-1);
            data.add(new TaskStatisticsData(id, name, completed, uncompleted));
        }

        this.statisticsEmployeeTableView.setItems(data);
    }

    @FXML
    private void backAction(ActionEvent event) {
        this.loadScene("/com/example/assignment_1/home.fxml");
    }

    @FXML
    private void changeStatusAction(ActionEvent event) {
        int employeeId = Integer.parseInt(this.employeeIdTextField.getText());
        int taskId = Integer.parseInt(this.taskIdTextField.getText());
        Task task = Utility.findTaskById(taskId);
        TaskManagement.modifyTaskStatus(employeeId, taskId);
        this.messageLabel.setText("Task status modified to: " + task.getStatusTask());
        this.setupFilterEmployeesTable();
        this.setupStatisticsTable();
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlFile));
            AnchorPane newScene = (AnchorPane)loader.load();
            Stage stage = (Stage)this.backButton.getScene().getWindow();
            stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

    }

    public static class EmployeeData {
        private final int id;
        private final String name;
        private final int hoursWorked;

        public EmployeeData(int id, String name, int hoursWorked) {
            this.id = id;
            this.name = name;
            this.hoursWorked = hoursWorked;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public int getHoursWorked() {
            return this.hoursWorked;
        }
    }

    public static class TaskStatisticsData {
        private final int id;
        private final String name;
        private final int completedTasks;
        private final int uncompletedTasks;

        public TaskStatisticsData(int id, String name, int completedTasks, int uncompletedTasks) {
            this.id = id;
            this.name = name;
            this.completedTasks = completedTasks;
            this.uncompletedTasks = uncompletedTasks;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public int getCompletedTasks() {
            return this.completedTasks;
        }

        public int getUncompletedTasks() {
            return this.uncompletedTasks;
        }
    }
}