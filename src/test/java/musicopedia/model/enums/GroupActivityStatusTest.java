package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GroupActivityStatusTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(3, GroupActivityStatus.values().length);
        assertNotNull(GroupActivityStatus.valueOf("ACTIVE"));
        assertNotNull(GroupActivityStatus.valueOf("INACTIVE"));
        assertNotNull(GroupActivityStatus.valueOf("DISBANDED"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, GroupActivityStatus.ACTIVE.ordinal());
        assertEquals(1, GroupActivityStatus.INACTIVE.ordinal());
        assertEquals(2, GroupActivityStatus.DISBANDED.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("ACTIVE", GroupActivityStatus.ACTIVE.toString());
        assertEquals("INACTIVE", GroupActivityStatus.INACTIVE.toString());
        assertEquals("DISBANDED", GroupActivityStatus.DISBANDED.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(GroupActivityStatus.ACTIVE, GroupActivityStatus.valueOf("ACTIVE"));
        assertSame(GroupActivityStatus.INACTIVE, GroupActivityStatus.valueOf("INACTIVE"));
        assertSame(GroupActivityStatus.DISBANDED, GroupActivityStatus.valueOf("DISBANDED"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> GroupActivityStatus.valueOf("InvalidStatus"));
    }
}
