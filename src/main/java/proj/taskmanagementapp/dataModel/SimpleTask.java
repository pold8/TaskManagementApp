package proj.taskmanagementapp.dataModel;

import java.io.Serializable;

public class SimpleTask extends Task implements Serializable {
    private int startHour;
    private int endHour;

    public SimpleTask(int startHour, int endHour, int idTask) {
        super(idTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int getStartHour() {
        return this.startHour;
    }

    public int getEndHour() {
        return this.endHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int estimateDuration() {
        return this.endHour - this.startHour;
    }
}
