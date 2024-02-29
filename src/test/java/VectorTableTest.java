import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class VectorTableTest {
    private final List<TableEntry> testInitialEntries = List.of(
            new TableEntry("N1", "R1", 1),
            new TableEntry("N3", "R3", 2)
    );

    public VectorTable testUpdateTable_initial(){
        VectorTable table = new VectorTable();
        for (TableEntry entry : testInitialEntries){
            Assertions.assertTrue(table.updateTable(entry));
        }
        return table;
    }

    @Test
    public void testUpdateTable_update(){
        VectorTable table = testUpdateTable_initial();
        Assertions.assertTrue(table.updateTable(new TableEntry("N3", "R3", 1)));
    }

    @Test
    public void testUpdateTable_noUpdate(){
        VectorTable table = testUpdateTable_initial();
        Assertions.assertFalse(table.updateTable(new TableEntry("N1", "R1", 1)));
    }
}