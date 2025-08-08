package proj.taskmanagementapp.businessLogic;

import proj.taskmanagementapp.dataModel.Employee;
import proj.taskmanagementapp.dataModel.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Map<Employee, List<Task>> employeeTasks = new HashMap<>();

    public TaskManagement() {
        employeeTasks = new HashMap<>();
    }

    public static Map<Employee, List<Task>> getEmployeeTasks() {
        return employeeTasks;
    }

    public static void setEmployeeTasks(Map<Employee, List<Task>> employeeTasks) {
        TaskManagement.employeeTasks = employeeTasks;
    }

    public static void assignTaskToEmployee(int idEmployee, Task task) {
        for (Employee employee : employeeTasks.keySet()) {
            if (employee.getIdEmployee() == idEmployee) {
                List<Task> tasks = employeeTasks.computeIfAbsent(employee, k -> new ArrayList<>());
                if (tasks.stream().noneMatch(t -> t.getIdTask() == task.getIdTask())) {
                    tasks.add(task);
                } else {
                    System.out.println("Task with ID " + task.getIdTask() + " already assigned to employee " + employee.getName());
                }
                return;
            }
        }
    }

    public static int calculateEmployeeWorkDuration(int idEmployee) {
        return employeeTasks.entrySet().stream()
                .filter(entry -> entry.getKey().getIdEmployee() == idEmployee)
                .flatMap(entry -> entry.getValue().stream())
                .filter(task -> "Completed".equals(task.getStatusTask()))
                .mapToInt(Task::estimateDuration)
                .sum();
    }


    public static void modifyTaskStatus(int idEmployee, int idTask) {
        for (Employee employee : employeeTasks.keySet()) {
            if (employee.getIdEmployee() == idEmployee) {
                for (Task task : employeeTasks.get(employee)) {
                    if (task.getIdTask() == idTask) {
                        task.setStatusTask(task.getStatusTask().equals("Completed") ? "Uncompleted" : "Completed");
                    }
                }
            }
        }
    }

    public static void addEmployee(Employee employee) {
        employeeTasks.putIfAbsent(employee, new ArrayList<>());
    }
}