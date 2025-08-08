package proj.taskmanagementapp.userInterface;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proj.taskmanagementapp.businessLogic.TaskManagement;
import proj.taskmanagementapp.businessLogic.Utility;
import proj.taskmanagementapp.dataModel.Task;

import java.util.ArrayList;
import java.util.Iterator;
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

    public ManageEmployeesController() {
    }

    @FXML
    private void initialize() {
        this.simpleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.idColumn.setCellValueFactory((cellData) -> {
            Task task = (Task)cellData.getValue();
            return task != null ? (new SimpleIntegerProperty(task.getIdTask())).asObject() : (new SimpleIntegerProperty(0)).asObject();
        });
        this.loadTasksIntoTable();
        this.choiceBox.getItems().addAll(new String[]{"Simple", "Complex"});
        this.choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Simple".equals(newValue)) {
                this.simpleTable.setDisable(true);
                this.startHourTextField.setDisable(false);
                this.endHourTextField.setDisable(false);
            } else if ("Complex".equals(newValue)) {
                this.simpleTable.setDisable(false);
                this.startHourTextField.setDisable(true);
                this.endHourTextField.setDisable(true);
            }

        });
    }

    @FXML
    private void loadTasksIntoTable() {
        List<Task> taskList = Utility.getUnassignedTasks();
        List<Task> simpleTasks = new ArrayList();
        Iterator var3 = taskList.iterator();

        while(var3.hasNext()) {
            Task task = (Task)var3.next();
            if (task instanceof Task) {
                simpleTasks.add(task);
            }
        }

        if (simpleTasks.isEmpty()) {
            System.out.println("No simple tasks available.");
        }

        ObservableList<Task> observableTasks = FXCollections.observableArrayList(simpleTasks);
        this.simpleTable.setItems(observableTasks);
    }

    @FXML
    public void addTaskAction(ActionEvent event) {
        String taskIdText = this.idTextField.getText().trim();
        String taskType = (String)this.choiceBox.getValue();

        try {
            int taskId = Integer.parseInt(taskIdText);
            if ("Simple".equals(taskType)) {
                this.addSimpleTask(taskId);
            } else if ("Complex".equals(taskType)) {
                this.addComplexTask(taskId);
            }

            this.idTextField.clear();
            this.startHourTextField.clear();
            this.endHourTextField.clear();
        } catch (NumberFormatException var5) {
            System.out.println("Error: Task ID must be a number!");
        }

    }

    private void addSimpleTask(int taskId) {
        String startHourText = this.startHourTextField.getText().trim();
        String endHourText = this.endHourTextField.getText().trim();
        if (!startHourText.isEmpty() && !endHourText.isEmpty()) {
            try {
                int startHour = Integer.parseInt(startHourText);
                int endHour = Integer.parseInt(endHourText);
                if (startHour >= endHour) {
                    System.out.println("Error: Start hour must be less than End hour!");
                    return;
                }

                Utility.createSimpleTask(taskId, startHour, endHour);
                System.out.println("Simple Task added: " + taskId);
                this.loadTasksIntoTable();
            } catch (NumberFormatException var6) {
                System.out.println("Error: Start and End hours must be numbers!");
            }

        } else {
            System.out.println("Error: Start and End hours must be provided for a Simple task!");
        }
    }

    private void addComplexTask(int taskId) {
        ObservableList<Task> selectedTasks = this.simpleTable.getSelectionModel().getSelectedItems();
        if (selectedTasks.isEmpty()) {
            System.out.println("Error: Select at least one sub-task for a Complex task!");
        } else {
            List<Task> subTasks = List.copyOf(selectedTasks);
            Utility.createComplexTask(taskId, subTasks);
            System.out.println("Complex Task added: " + taskId + " with " + subTasks.size() + " sub-tasks.");
            this.loadTasksIntoTable();
        }
    }

    @FXML
    private void backAction(ActionEvent event) {
        this.loadScene("/com/example/assignment_1/home.fxml");
    }

    @FXML
    private void addEmpAction(ActionEvent event) {
        String name = this.empNameTextField.getText().trim();
        String id = this.empIdTextField.getText().trim();
        if (!name.isEmpty() && !id.isEmpty()) {
            try {
                int employeeId = Integer.parseInt(id);
                Utility.createEmployee(employeeId, name);
                System.out.println("Employee added: " + name + " (ID: " + employeeId + ")");
                this.empIdTextField.clear();
                this.empNameTextField.clear();
            } catch (NumberFormatException var5) {
                System.out.println("Error: ID must be a number!");
            }

        } else {
            System.out.println("Error: Both fields must be filled!");
        }
    }

    @FXML
    public void assignTaskAction(ActionEvent event) {
        String idEmployeeText = this.employeeIdTextField.getText().trim();
        String idTaskText = this.taskIdTextField.getText().trim();
        if (!idEmployeeText.isEmpty() && !idTaskText.isEmpty()) {
            try {
                int idEmployee = Integer.parseInt(idEmployeeText);
                int idTask = Integer.parseInt(idTaskText);
                Task taskToAssign = Utility.findTaskById(idTask);
                if (taskToAssign == null) {
                    System.out.println("Error: Task with ID " + idTask + " not found.");
                    return;
                }

                TaskManagement.assignTaskToEmployee(idEmployee, taskToAssign);
                System.out.println("Task " + idTask + " assigned to Employee " + idEmployee);
                this.employeeIdTextField.clear();
                this.taskIdTextField.clear();
            } catch (NumberFormatException var7) {
                System.out.println("Error: Employee ID and Task ID must be numbers!");
            } catch (IllegalArgumentException var8) {
                IllegalArgumentException e = var8;
                System.out.println("Error: " + e.getMessage());
            }

        } else {
            System.out.println("Error: Both Employee ID and Task ID must be filled!");
        }
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlFile));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Location is not set for " + fxmlFile);
            }

            AnchorPane newScene = (AnchorPane)loader.load();
            Stage stage = (Stage)this.backButton.getScene().getWindow();
            stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

    }
}