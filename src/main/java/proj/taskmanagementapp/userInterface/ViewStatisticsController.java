package proj.taskmanagementapp.userInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import proj.taskmanagementapp.businessLogic.TaskManagement;
import proj.taskmanagementapp.businessLogic.Utility;
import proj.taskmanagementapp.dataModel.Task;

import java.util.List;
import java.util.Map;

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

    @FXML
    private void initialize() {
        setupFilterEmployeesTable();
        setupStatisticsTable();
    }

    private void setupFilterEmployeesTable() {
        filterEmployeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        filterEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        filterEmployeeHoursColumn.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));

        List<String> employeeNames = Utility.filterAndSortEmployeesByWorkDuration();
        ObservableList<EmployeeData> data = FXCollections.observableArrayList();

        for (String name : employeeNames) {
            int id = TaskManagement.getEmployeeTasks().keySet().stream()
                    .filter(e -> e.getName().equals(name))
                    .map(e -> e.getIdEmployee())
                    .findFirst().orElse(-1);
            int hoursWorked = TaskManagement.calculateEmployeeWorkDuration(id);
            data.add(new EmployeeData(id, name, hoursWorked));
        }

        filterEmployeesTableView.setItems(data);
    }

    private void setupStatisticsTable() {
        statisticsEmployeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statisticsEmployeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        completedTasksColumn.setCellValueFactory(new PropertyValueFactory<>("completedTasks"));
        uncompletedTasksColumn.setCellValueFactory(new PropertyValueFactory<>("uncompletedTasks"));

        Map<String, Map<String, Integer>> taskStats = Utility.calculateCompletedAndUncompletedTasks();
        ObservableList<TaskStatisticsData> data = FXCollections.observableArrayList();

        for (Map.Entry<String, Map<String, Integer>> entry : taskStats.entrySet()) {
            String name = entry.getKey();
            int completed = entry.getValue().getOrDefault("Completed", 0);
            int uncompleted = entry.getValue().getOrDefault("Uncompleted", 0);

            int id = TaskManagement.getEmployeeTasks().keySet().stream()
                    .filter(e -> e.getName().equals(name))
                    .map(e -> e.getIdEmployee())
                    .findFirst().orElse(-1);

            data.add(new TaskStatisticsData(id, name, completed, uncompleted));
        }

        statisticsEmployeeTableView.setItems(data);
    }

    @FXML
    private void backAction(ActionEvent event) {
        loadScene("/proj/taskmanagementapp/home.fxml");
    }

    @FXML
    private void changeStatusAction(ActionEvent event) {
        int employeeId = Integer.parseInt(employeeIdTextField.getText());
        int taskId = Integer.parseInt(taskIdTextField.getText());

        Task task = Utility.findTaskById(taskId);

        TaskManagement.modifyTaskStatus(employeeId, taskId);
        messageLabel.setText("Task status modified to: " + task.getStatusTask());

        // Refresh tables
        setupFilterEmployeesTable();
        setupStatisticsTable();
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

        public int getId() { return id; }
        public String getName() { return name; }
        public int getHoursWorked() { return hoursWorked; }
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

        public int getId() { return id; }
        public String getName() { return name; }
        public int getCompletedTasks() { return completedTasks; }
        public int getUncompletedTasks() { return uncompletedTasks; }
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane newScene = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}