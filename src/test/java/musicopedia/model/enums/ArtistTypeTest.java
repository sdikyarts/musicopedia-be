package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArtistTypeTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(4, ArtistType.values().length);
        assertNotNull(ArtistType.valueOf("Solo"));
        assertNotNull(ArtistType.valueOf("Group"));
        assertNotNull(ArtistType.valueOf("Franchise"));
        assertNotNull(ArtistType.valueOf("Various"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, ArtistType.Solo.ordinal());
        assertEquals(1, ArtistType.Group.ordinal());
        assertEquals(2, ArtistType.Franchise.ordinal());
        assertEquals(3, ArtistType.Various.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("Solo", ArtistType.Solo.toString());
        assertEquals("Group", ArtistType.Group.toString());
        assertEquals("Franchise", ArtistType.Franchise.toString());
        assertEquals("Various", ArtistType.Various.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(ArtistType.Solo, ArtistType.valueOf("Solo"));
        assertSame(ArtistType.Group, ArtistType.valueOf("Group"));
        assertSame(ArtistType.Franchise, ArtistType.valueOf("Franchise"));
        assertSame(ArtistType.Various, ArtistType.valueOf("Various"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> ArtistType.valueOf("InvalidType"));
    }
}
