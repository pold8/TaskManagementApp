package proj.taskmanagementapp.dataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public non-sealed class ComplexTask extends Task implements Serializable {

    private List<Task> tasks;

    public ComplexTask(int idTask) {
        super(idTask);
        this.tasks = new ArrayList<Task>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int estimateDuration(){
        int duration = 0;

        for(Task task : tasks){
            duration += task.estimateDuration();
            System.out.println("Adding task duration: " + task.estimateDuration());  // Log each task's duration
        }

        System.out.println("Total duration for complex task: " + duration);  // Log total duration
        return duration;
    }


    public void addTask(Task task) {
        // Add the task only if it doesn't already exist
        if (tasks.stream().noneMatch(t -> t.getIdTask() == task.getIdTask())) {
            tasks.add(task);
        } else {
            System.out.println("Task already exists");
        }
    }

    public void deleteTask(Task task) {
        // Add the task only if it doesn't already exist
        if (tasks.stream().noneMatch(t -> t.getIdTask() == task.getIdTask())) {
            System.out.println("Task does not exist");
        } else {
            tasks.remove(task);
        }
    }
}
