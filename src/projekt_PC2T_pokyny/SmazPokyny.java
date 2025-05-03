package projekt_PC2T_pokyny;

import java.sql.SQLException;

public class SmazPokyny extends StudentAkce {
    public SmazPokyny(int studentId) {
        super(studentId);
    }

    @Override
    public void proved() throws SQLException {
        new VlozeniPokynu().smazaniStudenta(studentId);
    }
}
