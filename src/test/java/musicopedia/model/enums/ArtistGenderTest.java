package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArtistGenderTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(5, ArtistGender.values().length);
        assertNotNull(ArtistGender.valueOf("MALE"));
        assertNotNull(ArtistGender.valueOf("FEMALE"));
        assertNotNull(ArtistGender.valueOf("MIXED"));
        assertNotNull(ArtistGender.valueOf("NON_BINARY"));
        assertNotNull(ArtistGender.valueOf("UNKNOWN"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, ArtistGender.MALE.ordinal());
        assertEquals(1, ArtistGender.FEMALE.ordinal());
        assertEquals(2, ArtistGender.MIXED.ordinal());
        assertEquals(3, ArtistGender.NON_BINARY.ordinal());
        assertEquals(4, ArtistGender.UNKNOWN.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("MALE", ArtistGender.MALE.toString());
        assertEquals("FEMALE", ArtistGender.FEMALE.toString());
        assertEquals("MIXED", ArtistGender.MIXED.toString());
        assertEquals("NON_BINARY", ArtistGender.NON_BINARY.toString());
        assertEquals("UNKNOWN", ArtistGender.UNKNOWN.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(ArtistGender.MALE, ArtistGender.valueOf("MALE"));
        assertSame(ArtistGender.FEMALE, ArtistGender.valueOf("FEMALE"));
        assertSame(ArtistGender.MIXED, ArtistGender.valueOf("MIXED"));
        assertSame(ArtistGender.NON_BINARY, ArtistGender.valueOf("NON_BINARY"));
        assertSame(ArtistGender.UNKNOWN, ArtistGender.valueOf("UNKNOWN"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> ArtistGender.valueOf("InvalidGender"));
    }
}
