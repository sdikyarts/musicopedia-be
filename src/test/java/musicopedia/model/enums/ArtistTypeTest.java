package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArtistTypeTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(4, ArtistType.values().length);
        assertNotNull(ArtistType.valueOf("SOLO"));
        assertNotNull(ArtistType.valueOf("GROUP"));
        assertNotNull(ArtistType.valueOf("FRANCHISE"));
        assertNotNull(ArtistType.valueOf("VARIOUS"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, ArtistType.SOLO.ordinal());
        assertEquals(1, ArtistType.GROUP.ordinal());
        assertEquals(2, ArtistType.FRANCHISE.ordinal());
        assertEquals(3, ArtistType.VARIOUS.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("SOLO", ArtistType.SOLO.toString());
        assertEquals("GROUP", ArtistType.GROUP.toString());
        assertEquals("FRANCHISE", ArtistType.FRANCHISE.toString());
        assertEquals("VARIOUS", ArtistType.VARIOUS.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(ArtistType.SOLO, ArtistType.valueOf("SOLO"));
        assertSame(ArtistType.GROUP, ArtistType.valueOf("GROUP"));
        assertSame(ArtistType.FRANCHISE, ArtistType.valueOf("FRANCHISE"));
        assertSame(ArtistType.VARIOUS, ArtistType.valueOf("VARIOUS"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> ArtistType.valueOf("InvalidType"));
    }
}
