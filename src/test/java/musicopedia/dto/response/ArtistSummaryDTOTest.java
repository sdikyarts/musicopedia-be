package musicopedia.dto.response;

import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArtistSummaryDTOTest {

    @Test
    void testArtistSummaryDTO_AllFields() {
        // Given
        ArtistSummaryDTO dto = new ArtistSummaryDTO();
        UUID artistId = UUID.randomUUID();
        
        // When
        dto.setArtistId(artistId);
        dto.setArtistName("Summary Artist");
        dto.setType(ArtistType.SOLO);
        dto.setImage("summary-image.jpg");
        dto.setGenre("Pop");
        dto.setOriginCountry("US");
        
        // Then
        assertEquals(artistId, dto.getArtistId());
        assertEquals("Summary Artist", dto.getArtistName());
        assertEquals(ArtistType.SOLO, dto.getType());
        assertEquals("summary-image.jpg", dto.getImage());
        assertEquals("Pop", dto.getGenre());
        assertEquals("US", dto.getOriginCountry());
    }

    @Test
    void testArtistSummaryDTO_MinimalFields() {
        // Given
        ArtistSummaryDTO dto = new ArtistSummaryDTO();
        UUID artistId = UUID.randomUUID();
        
        // When
        dto.setArtistId(artistId);
        dto.setArtistName("Minimal Summary");
        dto.setType(ArtistType.GROUP);
        
        // Then
        assertEquals(artistId, dto.getArtistId());
        assertEquals("Minimal Summary", dto.getArtistName());
        assertEquals(ArtistType.GROUP, dto.getType());
        assertNull(dto.getImage());
        assertNull(dto.getGenre());
        assertNull(dto.getOriginCountry());
    }

    @Test
    void testArtistSummaryDTO_AllArtistTypes() {
        // Given & When & Then
        for (ArtistType type : ArtistType.values()) {
            ArtistSummaryDTO dto = new ArtistSummaryDTO();
            dto.setArtistId(UUID.randomUUID());
            dto.setArtistName("Test " + type.name());
            dto.setType(type);
            
            assertEquals(type, dto.getType());
            assertTrue(dto.getArtistName().contains(type.name()));
        }
    }

    @Test
    void testArtistSummaryDTO_EqualsAndHashCode() {
        // Given
        UUID artistId = UUID.randomUUID();
        
        ArtistSummaryDTO dto1 = new ArtistSummaryDTO();
        dto1.setArtistId(artistId);
        dto1.setArtistName("Test Summary");
        dto1.setType(ArtistType.FRANCHISE);
        
        ArtistSummaryDTO dto2 = new ArtistSummaryDTO();
        dto2.setArtistId(artistId);
        dto2.setArtistName("Test Summary");
        dto2.setType(ArtistType.FRANCHISE);
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testArtistSummaryDTO_NotEquals() {
        // Given
        ArtistSummaryDTO dto1 = new ArtistSummaryDTO();
        dto1.setArtistId(UUID.randomUUID());
        dto1.setArtistName("Artist 1");
        dto1.setType(ArtistType.SOLO);
        
        ArtistSummaryDTO dto2 = new ArtistSummaryDTO();
        dto2.setArtistId(UUID.randomUUID()); // Different ID
        dto2.setArtistName("Artist 1");
        dto2.setType(ArtistType.SOLO);
        
        // Then
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testArtistSummaryDTO_ToString() {
        // Given
        ArtistSummaryDTO dto = new ArtistSummaryDTO();
        dto.setArtistId(UUID.randomUUID());
        dto.setArtistName("Test Summary");
        dto.setType(ArtistType.VARIOUS);
        dto.setGenre("Electronic");
        
        // When
        String toString = dto.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Summary"));
        assertTrue(toString.contains("VARIOUS"));
        assertTrue(toString.contains("Electronic"));
    }

    @Test
    void testArtistSummaryDTO_NullValues() {
        // Given
        ArtistSummaryDTO dto = new ArtistSummaryDTO();
        
        // Then - All fields should be null by default
        assertNull(dto.getArtistId());
        assertNull(dto.getArtistName());
        assertNull(dto.getType());
        assertNull(dto.getImage());
        assertNull(dto.getGenre());
        assertNull(dto.getOriginCountry());
    }
}
