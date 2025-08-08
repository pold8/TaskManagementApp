package proj.taskmanagementapp.businessLogic;

import proj.taskmanagementapp.dataModel.Employee;
import proj.taskmanagementapp.dataModel.Task;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskManagement {
    private static final long serialVersionUID = 1L;
    private static Map<Employee, List<Task>> employeeTasks = new HashMap();

    public TaskManagement() {
        employeeTasks = new HashMap();
    }

    public static Map<Employee, List<Task>> getEmployeeTasks() {
        return employeeTasks;
    }

    public static void setEmployeeTasks(Map<Employee, List<Task>> employeeTasks) {
        TaskManagement.employeeTasks = employeeTasks;
}

    public static void assignTaskToEmployee(int idEmployee, Task task) {
        Iterator var2 = employeeTasks.keySet().iterator();

        Employee employee;
        do {
            if (!var2.hasNext()) {
                return;
            }

            employee = (Employee)var2.next();
        } while(employee.getIdEmployee() != idEmployee);

        List<Task> tasks = (List)employeeTasks.computeIfAbsent(employee, (k) -> {
            return new ArrayList();
        });
        if (tasks.stream().noneMatch((t) -> {
            return t.getIdTask() == task.getIdTask();
        })) {
            tasks.add(task);
        } else {
            PrintStream var10000 = System.out;
            int var10001 = task.getIdTask();
            var10000.println("Task with ID " + var10001 + " already assigned to employee " + employee.getName());
        }

    }

    public static int calculateEmployeeWorkDuration(int idEmployee) {
        return employeeTasks.entrySet().stream().filter((entry) -> {
            return ((Employee)entry.getKey()).getIdEmployee() == idEmployee;
        }).flatMap((entry) -> {
            return ((List)entry.getValue()).stream();
        }).filter((task) -> {
            return "Completed".equals(task.getStatusTask());
        }).mapToInt(Task::estimateDuration).sum();
    }

    public static void modifyTaskStatus(int idEmployee, int idTask) {
        Iterator var2 = employeeTasks.keySet().iterator();

        while(true) {
            Employee employee;
            do {
                if (!var2.hasNext()) {
                    return;
                }

                employee = (Employee)var2.next();
            } while(employee.getIdEmployee() != idEmployee);

            Iterator var4 = ((List)employeeTasks.get(employee)).iterator();

            while(var4.hasNext()) {
                Task task = (Task)var4.next();
                if (task.getIdTask() == idTask) {
                    task.setStatusTask(task.getStatusTask().equals("Completed") ? "Uncompleted" : "Completed");
                }
            }
        }
    }

    public static void addEmployee(Employee employee) {
        employeeTasks.putIfAbsent(employee, new ArrayList());
    }
}
