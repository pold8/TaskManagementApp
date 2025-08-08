package proj.taskmanagementapp.dataModel;

import java.io.Serializable;

public abstract sealed class Task implements Serializable permits ComplexTask, SimpleTask {
    private int idTask;
    private String statusTask;

    public Task(int idTask) {
        this.idTask = idTask;
        this.statusTask = "Uncompleted";
    }

    public String getStatusTask() {
        return this.statusTask;
    }

    public int getIdTask() {
        return this.idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    public abstract int estimateDuration();
}
