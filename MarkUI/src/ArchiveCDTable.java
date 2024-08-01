import javax.swing.*;
import javax.swing.table.*;

public class ArchiveCDTable {
    private JTable m_Table;

    public ArchiveCDTable(Object[][] data) {
        String[] columnNames = {
                "Title",
                "Author",
                "Section",
                "X",
                "Y",
                "Barcode",
                "Description",
                "On-Loan"
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2:
                        return char.class;
                    case 3:
                        return int.class;
                    case 4:
                        return int.class;
                    case 5:
                        return int.class;
                    case 7:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };

        m_Table = new JTable(model);
    }

    public JTable getTable() {
        return m_Table;
    }
}
