package musicopedia.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArtistGenderTest {

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        assertEquals(5, ArtistGender.values().length);
        assertNotNull(ArtistGender.valueOf("Male"));
        assertNotNull(ArtistGender.valueOf("Female"));
        assertNotNull(ArtistGender.valueOf("Mixed"));
        assertNotNull(ArtistGender.valueOf("Non_Binary"));
        assertNotNull(ArtistGender.valueOf("Unknown"));
    }

    @Test
    public void testEnumOrdinals() {
        // Test the ordinal values
        assertEquals(0, ArtistGender.Male.ordinal());
        assertEquals(1, ArtistGender.Female.ordinal());
        assertEquals(2, ArtistGender.Mixed.ordinal());
        assertEquals(3, ArtistGender.Non_Binary.ordinal());
        assertEquals(4, ArtistGender.Unknown.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test string representation
        assertEquals("Male", ArtistGender.Male.toString());
        assertEquals("Female", ArtistGender.Female.toString());
        assertEquals("Mixed", ArtistGender.Mixed.toString());
        assertEquals("Non_Binary", ArtistGender.Non_Binary.toString());
        assertEquals("Unknown", ArtistGender.Unknown.toString());
    }

    @Test
    public void testValueOf() {
        // Test valueOf() method
        assertSame(ArtistGender.Male, ArtistGender.valueOf("Male"));
        assertSame(ArtistGender.Female, ArtistGender.valueOf("Female"));
        assertSame(ArtistGender.Mixed, ArtistGender.valueOf("Mixed"));
        assertSame(ArtistGender.Non_Binary, ArtistGender.valueOf("Non_Binary"));
        assertSame(ArtistGender.Unknown, ArtistGender.valueOf("Unknown"));
    }

    @Test
    public void testInvalidValueOf() {
        // Test that an invalid enum value throws an exception
        assertThrows(IllegalArgumentException.class, () -> ArtistGender.valueOf("InvalidGender"));
    }
}
