package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MembershipStatusTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(3, MembershipStatus.values().length);
        assertNotNull(MembershipStatus.valueOf("CURRENT"));
        assertNotNull(MembershipStatus.valueOf("FORMER"));
        assertNotNull(MembershipStatus.valueOf("INACTIVE"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, MembershipStatus.CURRENT.ordinal());
        assertEquals(1, MembershipStatus.FORMER.ordinal());
        assertEquals(2, MembershipStatus.INACTIVE.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("CURRENT", MembershipStatus.CURRENT.toString());
        assertEquals("FORMER", MembershipStatus.FORMER.toString());
        assertEquals("INACTIVE", MembershipStatus.INACTIVE.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(MembershipStatus.CURRENT, MembershipStatus.valueOf("CURRENT"));
        assertSame(MembershipStatus.FORMER, MembershipStatus.valueOf("FORMER"));
        assertSame(MembershipStatus.INACTIVE, MembershipStatus.valueOf("INACTIVE"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> MembershipStatus.valueOf("InvalidStatus"));
    }
}
