package proj.taskmanagementapp.userInterface;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proj.taskmanagementapp.businessLogic.TaskManagement;
import proj.taskmanagementapp.businessLogic.Utility;
import proj.taskmanagementapp.dataModel.Task;

import java.util.ArrayList;
import java.util.List;

public class ManageEmployeesController {
    @FXML
    Button backButton;

    @FXML
    Button addEmpButton;

    @FXML
    TextField empIdTextField;

    @FXML
    TextField empNameTextField;

    @FXML
    Button addTaskButton;

    @FXML
    Label idLabel;

    @FXML
    TextField idTextField;

    @FXML
    Label startHourLabel;

    @FXML
    TextField startHourTextField;

    @FXML
    Label endHourLabel;

    @FXML
    TextField endHourTextField;

    @FXML
    Label simpleLabel;

    @FXML
    TableView simpleTable;

    @FXML
    ChoiceBox<String> choiceBox;

    @FXML
    TableColumn<Task, Integer> idColumn;

    @FXML
    Button assignTaskButton;

    @FXML
    TextField employeeIdTextField;

    @FXML
    TextField taskIdTextField;



    @FXML
    private void initialize() {
        // Allow multiple selection in the simpleTable
        simpleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Set up the table columns with custom cell value factories
        idColumn.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            if (task != null) {
                return new SimpleIntegerProperty(task.getIdTask()).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject(); // Default value for null
            }
        });

        // Load tasks into the table
        loadTasksIntoTable();

        choiceBox.getItems().addAll("Simple", "Complex");

        // Add a listener to handle selection changes
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Simple".equals(newValue)) {
                simpleTable.setDisable(true);
                startHourTextField.setDisable(false);
                endHourTextField.setDisable(false);
            } else if ("Complex".equals(newValue)) {
                simpleTable.setDisable(false);
                startHourTextField.setDisable(true);
                endHourTextField.setDisable(true);
            }
        });
    }


    @FXML
    private void loadTasksIntoTable() {
        List<Task> taskList = Utility.getUnassignedTasks(); // Get tasks from Utility class

        // Filter the task list to only include SimpleTasks
        List<Task> simpleTasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task instanceof Task) { // Only include SimpleTask objects
                simpleTasks.add(task);
            }
        }

        // Handle null or empty list
        if (simpleTasks.isEmpty()) {
            System.out.println("No simple tasks available.");
        }

        ObservableList<Task> observableTasks = FXCollections.observableArrayList(simpleTasks);
        simpleTable.setItems(observableTasks);
    }

    @FXML
    public void addTaskAction(ActionEvent event) {
        String taskIdText = idTextField.getText().trim();
        String taskType = choiceBox.getValue();

        try {
            int taskId = Integer.parseInt(taskIdText);

            if ("Simple".equals(taskType)) {
                addSimpleTask(taskId);
            } else if ("Complex".equals(taskType)) {
                addComplexTask(taskId);
            }

            idTextField.clear();
            startHourTextField.clear();
            endHourTextField.clear();
        } catch (NumberFormatException e) {
            System.out.println("Error: Task ID must be a number!");
        }
    }

    private void addSimpleTask(int taskId) {
        String startHourText = startHourTextField.getText().trim();
        String endHourText = endHourTextField.getText().trim();

        if (startHourText.isEmpty() || endHourText.isEmpty()) {
            System.out.println("Error: Start and End hours must be provided for a Simple task!");
            return;
        }

        try {
            int startHour = Integer.parseInt(startHourText);
            int endHour = Integer.parseInt(endHourText);

            if (startHour >= endHour) {
                System.out.println("Error: Start hour must be less than End hour!");
                return;
            }

            Utility.createSimpleTask(taskId, startHour, endHour);
            System.out.println("Simple Task added: " + taskId);
            loadTasksIntoTable();
        } catch (NumberFormatException e) {
            System.out.println("Error: Start and End hours must be numbers!");
        }
    }

    private void addComplexTask(int taskId) {
        ObservableList<Task> selectedTasks = simpleTable.getSelectionModel().getSelectedItems();

        if (selectedTasks.isEmpty()) {
            System.out.println("Error: Select at least one sub-task for a Complex task!");
            return;
        }

        List<Task> subTasks = List.copyOf(selectedTasks);
        Utility.createComplexTask(taskId, subTasks);
        System.out.println("Complex Task added: " + taskId + " with " + subTasks.size() + " sub-tasks.");
        loadTasksIntoTable();
    }

    @FXML
    private void backAction(ActionEvent event) {
        loadScene("/proj/taskmanagementapp/home.fxml");
    }

    @FXML
    private void addEmpAction(ActionEvent event) {
        String name = empNameTextField.getText().trim();
        String id = empIdTextField.getText().trim();

        if (name.isEmpty() || id.isEmpty()) {
            System.out.println("Error: Both fields must be filled!");
            return;
        }

        try {
            int employeeId = Integer.parseInt(id);
            Utility.createEmployee(employeeId, name);
            System.out.println("Employee added: " + name + " (ID: " + employeeId + ")");
            empIdTextField.clear();
            empNameTextField.clear();
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number!");
        }
    }

    @FXML
    public void assignTaskAction(ActionEvent event) {
        String idEmployeeText = employeeIdTextField.getText().trim();
        String idTaskText = taskIdTextField.getText().trim();

        if (idEmployeeText.isEmpty() || idTaskText.isEmpty()) {
            System.out.println("Error: Both Employee ID and Task ID must be filled!");
            return;
        }

        try {
            int idEmployee = Integer.parseInt(idEmployeeText);
            int idTask = Integer.parseInt(idTaskText);

            // Find the Task object by ID
            Task taskToAssign = Utility.findTaskById(idTask);
            if (taskToAssign == null) {
                System.out.println("Error: Task with ID " + idTask + " not found.");
                return;
            }

            TaskManagement.assignTaskToEmployee(idEmployee, taskToAssign);
            System.out.println("Task " + idTask + " assigned to Employee " + idEmployee);

            employeeIdTextField.clear();
            taskIdTextField.clear();
        } catch (NumberFormatException e) {
            System.out.println("Error: Employee ID and Task ID must be numbers!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
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
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}