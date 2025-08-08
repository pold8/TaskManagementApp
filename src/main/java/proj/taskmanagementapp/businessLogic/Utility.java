package proj.taskmanagementapp.businessLogic;

import proj.taskmanagementapp.dataModel.ComplexTask;
import proj.taskmanagementapp.dataModel.Employee;
import proj.taskmanagementapp.dataModel.SimpleTask;
import proj.taskmanagementapp.dataModel.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Utility implements Serializable {
    private static List<Task> unassignedTasks = new ArrayList<>();

    public static void setUnassignedTasks(List<Task> unassignedTasks) {
        Utility.unassignedTasks = unassignedTasks;
    }

    public Utility() {
        unassignedTasks = new ArrayList<>();
    }

    public static List<Task> getUnassignedTasks() {
        return unassignedTasks;
    }

    public static List<String> filterAndSortEmployeesByWorkDuration() {
        return TaskManagement.getEmployeeTasks().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().getName(), TaskManagement.calculateEmployeeWorkDuration(entry.getKey().getIdEmployee())))
                .filter(entry -> entry.getValue() > 40)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) // Sorting in descending order
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Map<String, Map<String, Integer>> calculateCompletedAndUncompletedTasks() {
        return TaskManagement.getEmployeeTasks().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getName(),
                        entry -> {
                            Map<String, Integer> taskCounts = new HashMap<>();
                            taskCounts.put("Completed", 0);
                            taskCounts.put("Uncompleted", 0);
                            for (Task task : entry.getValue()) {
                                taskCounts.put(task.getStatusTask(), taskCounts.get(task.getStatusTask()) + 1);
                            }
                            return taskCounts;
                        }
                ));
    }

    private static boolean taskExists(int idTask) {
        return unassignedTasks.stream().anyMatch(task -> task.getIdTask() == idTask);
    }

    public static void createSimpleTask(int idTask, int startHour, int endHour) {
        if (taskExists(idTask)) {
            System.out.println("Task with ID " + idTask + " already exists! Task not created.");
        } else {
            Task task = new SimpleTask(startHour, endHour, idTask);
            unassignedTasks.add(task);
            System.out.println("Simple Task created with ID: " + idTask);
        }
    }

    public static void createComplexTask(int idTask, List<Task> tasks) {
        if (taskExists(idTask)) {
            System.out.println("Task with ID " + idTask + " already exists! Task not created.");
        } else {
            ComplexTask complexTask = new ComplexTask(idTask);
            for (Task simpleTask : tasks) {
                complexTask.addTask(simpleTask);  // Add simple tasks correctly
            }
            unassignedTasks.add(complexTask);
            System.out.println("Complex Task created with ID: " + idTask);
        }
    }

    public static void createEmployee(int idEmployee, String name) {
        Map<Employee, List<Task>> employeeTasks = TaskManagement.getEmployeeTasks();

        Employee employee = new Employee(idEmployee, name);

        for(Employee employee1 : employeeTasks.keySet()) {
            if(employee1.getIdEmployee() == idEmployee) {
                throw new IllegalArgumentException("Employee with ID " + idEmployee + " already exists! Employee not created.");
            }
        }

        TaskManagement.addEmployee(employee);
    }

    public static Task findTaskById(int taskId){
        for(Task task : unassignedTasks) {
            if(task.getIdTask() == taskId) {
                return task;
            }
        }
        return null;
    }
}