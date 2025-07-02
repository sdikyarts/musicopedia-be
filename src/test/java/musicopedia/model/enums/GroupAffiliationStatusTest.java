package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GroupAffiliationStatusTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(3, GroupAffiliationStatus.values().length);
        assertNotNull(GroupAffiliationStatus.valueOf("NEVER_IN_A_GROUP"));
        assertNotNull(GroupAffiliationStatus.valueOf("IN_A_GROUP"));
        assertNotNull(GroupAffiliationStatus.valueOf("WAS_IN_A_GROUP"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, GroupAffiliationStatus.NEVER_IN_A_GROUP.ordinal());
        assertEquals(1, GroupAffiliationStatus.IN_A_GROUP.ordinal());
        assertEquals(2, GroupAffiliationStatus.WAS_IN_A_GROUP.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("NEVER_IN_A_GROUP", GroupAffiliationStatus.NEVER_IN_A_GROUP.toString());
        assertEquals("IN_A_GROUP", GroupAffiliationStatus.IN_A_GROUP.toString());
        assertEquals("WAS_IN_A_GROUP", GroupAffiliationStatus.WAS_IN_A_GROUP.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(GroupAffiliationStatus.NEVER_IN_A_GROUP, GroupAffiliationStatus.valueOf("NEVER_IN_A_GROUP"));
        assertSame(GroupAffiliationStatus.IN_A_GROUP, GroupAffiliationStatus.valueOf("IN_A_GROUP"));
        assertSame(GroupAffiliationStatus.WAS_IN_A_GROUP, GroupAffiliationStatus.valueOf("WAS_IN_A_GROUP"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> GroupAffiliationStatus.valueOf("InvalidStatus"));
    }
}
