package proj.taskmanagementapp.dataModel;

import java.io.Serializable;

public non-sealed class SimpleTask extends Task implements Serializable {
    private int startHour;
    private int endHour;

    public SimpleTask(int startHour, int endHour, int idTask) {
        super(idTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    @Override
    public int estimateDuration(){
        return endHour - startHour;
    }
}
