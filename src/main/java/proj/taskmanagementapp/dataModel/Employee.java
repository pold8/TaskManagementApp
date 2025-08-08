package proj.taskmanagementapp.dataModel;

import java.io.Serializable;

public class Employee implements Serializable {
    private int idEmployee;
    private String name;

    public Employee(int idEmployee, String name) {
        this.idEmployee = idEmployee;
        this.name = name;
    }

    public int getIdEmployee() {
        return this.idEmployee;
    }

    public String getName() {
        return this.name;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        return Integer.hashCode(this.idEmployee);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Employee employee = (Employee)obj;
            return this.idEmployee == employee.idEmployee;
        } else {
            return false;
        }
    }
}