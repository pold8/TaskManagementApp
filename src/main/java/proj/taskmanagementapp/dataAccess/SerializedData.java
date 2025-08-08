package proj.taskmanagementapp.dataAccess;

import proj.taskmanagementapp.businessLogic.TaskManagement;
import proj.taskmanagementapp.businessLogic.Utility;
import proj.taskmanagementapp.dataModel.Employee;
import proj.taskmanagementapp.dataModel.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializedData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Employee, List<Task>> employeeTasks;
    private final List<Task> unassignedTasks;

    public SerializedData() {
        this.employeeTasks = TaskManagement.getEmployeeTasks();
        this.unassignedTasks = Utility.getUnassignedTasks();
    }

    public void restore() {
        TaskManagement.setEmployeeTasks(employeeTasks);
        Utility.setUnassignedTasks(unassignedTasks);
    }
}
