package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MembershipStatusTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(3, MembershipStatus.values().length);
        assertNotNull(MembershipStatus.valueOf("Current"));
        assertNotNull(MembershipStatus.valueOf("Former"));
        assertNotNull(MembershipStatus.valueOf("Inactive"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, MembershipStatus.Current.ordinal());
        assertEquals(1, MembershipStatus.Former.ordinal());
        assertEquals(2, MembershipStatus.Inactive.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("Current", MembershipStatus.Current.toString());
        assertEquals("Former", MembershipStatus.Former.toString());
        assertEquals("Inactive", MembershipStatus.Inactive.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(MembershipStatus.Current, MembershipStatus.valueOf("Current"));
        assertSame(MembershipStatus.Former, MembershipStatus.valueOf("Former"));
        assertSame(MembershipStatus.Inactive, MembershipStatus.valueOf("Inactive"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> MembershipStatus.valueOf("InvalidStatus"));
    }
}
