package proj.taskmanagementapp.businessLogic;

import proj.taskmanagementapp.dataModel.ComplexTask;
import proj.taskmanagementapp.dataModel.Employee;
import proj.taskmanagementapp.dataModel.SimpleTask;
import proj.taskmanagementapp.dataModel.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utility implements Serializable {
    private static List<Task> unassignedTasks = new ArrayList();

    public static void setUnassignedTasks(List<Task> unassignedTasks) {
        Utility.unassignedTasks = unassignedTasks;
    }

    public Utility() {
        unassignedTasks = new ArrayList();
    }

    public static List<Task> getUnassignedTasks() {
        return unassignedTasks;
    }

    public static List<String> filterAndSortEmployeesByWorkDuration() {
        return (List)TaskManagement.getEmployeeTasks().entrySet().stream().map((entry) -> {
            return Map.entry(((Employee)entry.getKey()).getName(), TaskManagement.calculateEmployeeWorkDuration(((Employee)entry.getKey()).getIdEmployee()));
        }).filter((entry) -> {
            return (Integer)entry.getValue() > 40;
        }).sorted(Entry.comparingByValue().reversed()).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static Map<String, Map<String, Integer>> calculateCompletedAndUncompletedTasks() {
        return (Map)TaskManagement.getEmployeeTasks().entrySet().stream().collect(Collectors.toMap((entry) -> {
            return ((Employee)entry.getKey()).getName();
        }, (entry) -> {
            Map<String, Integer> taskCounts = new HashMap();
            taskCounts.put("Completed", 0);
            taskCounts.put("Uncompleted", 0);
            Iterator var2 = ((List)entry.getValue()).iterator();

            while(var2.hasNext()) {
                Task task = (Task)var2.next();
                taskCounts.put(task.getStatusTask(), (Integer)taskCounts.get(task.getStatusTask()) + 1);
            }

            return taskCounts;
        }));
    }

    private static boolean taskExists(int idTask) {
        return unassignedTasks.stream().anyMatch((task) -> {
            return task.getIdTask() == idTask;
        });
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
            Iterator var3 = tasks.iterator();

            while(var3.hasNext()) {
                Task simpleTask = (Task)var3.next();
                complexTask.addTask(simpleTask);
            }

            unassignedTasks.add(complexTask);
            System.out.println("Complex Task created with ID: " + idTask);
        }

    }

    public static void createEmployee(int idEmployee, String name) {
        Map<Employee, List<Task>> employeeTasks = TaskManagement.getEmployeeTasks();
        Employee employee = new Employee(idEmployee, name);
        Iterator var4 = employeeTasks.keySet().iterator();

        Employee employee1;
        do {
            if (!var4.hasNext()) {
                TaskManagement.addEmployee(employee);
                return;
            }

            employee1 = (Employee)var4.next();
        } while(employee1.getIdEmployee() != idEmployee);

        throw new IllegalArgumentException("Employee with ID " + idEmployee + " already exists! Employee not created.");
    }

    public static Task findTaskById(int taskId) {
        Iterator var1 = unassignedTasks.iterator();

        Task task;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            task = (Task)var1.next();
        } while(task.getIdTask() != taskId);

        return task;
    }
}