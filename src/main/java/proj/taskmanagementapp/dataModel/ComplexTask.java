package proj.taskmanagementapp.dataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComplexTask extends Task implements Serializable {
    private List<Task> tasks = new ArrayList();

    public ComplexTask(int idTask) {
        super(idTask);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int estimateDuration() {
        int duration = 0;
        Iterator var2 = this.tasks.iterator();

        while(var2.hasNext()) {
            Task task = (Task)var2.next();
            duration += task.estimateDuration();
            System.out.println("Adding task duration: " + task.estimateDuration());
        }

        System.out.println("Total duration for complex task: " + duration);
        return duration;
    }

    public void addTask(Task task) {
        if (this.tasks.stream().noneMatch((t) -> {
            return t.getIdTask() == task.getIdTask();
        })) {
            this.tasks.add(task);
        } else {
            System.out.println("Task already exists");
        }

    }

    public void deleteTask(Task task) {
        if (this.tasks.stream().noneMatch((t) -> {
            return t.getIdTask() == task.getIdTask();
        })) {
            System.out.println("Task does not exist");
        } else {
            this.tasks.remove(task);
        }

    }
}
