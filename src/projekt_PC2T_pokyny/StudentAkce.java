package projekt_PC2T_pokyny;

import java.sql.SQLException;

public abstract class StudentAkce {
    protected int studentId;

    public StudentAkce(int studentId) {
        this.studentId = studentId;
    }

    public abstract void proved() throws SQLException;
}	