package musicopedia.dto.request;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreateArtistRequestDTOTest {

    @Test
    void testCreateArtistRequestDTO_AllFields() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        
        // When
        dto.setArtistName("Test Artist");
        dto.setType(ArtistType.SOLO);
        dto.setSpotifyId("test123");
        dto.setDescription("Test description");
        dto.setImage("test-image.jpg");
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");
        dto.setOriginCountry("US");
        
        // Solo fields
        dto.setBirthDate(LocalDate.of(1990, 5, 15));
        dto.setDeathDate(null);
        dto.setSoloGender(ArtistGender.MALE);
        
        // Then
        assertEquals("Test Artist", dto.getArtistName());
        assertEquals(ArtistType.SOLO, dto.getType());
        assertEquals("test123", dto.getSpotifyId());
        assertEquals("Test description", dto.getDescription());
        assertEquals("test-image.jpg", dto.getImage());
        assertEquals("English", dto.getPrimaryLanguage());
        assertEquals("Pop", dto.getGenre());
        assertEquals("US", dto.getOriginCountry());
        assertEquals(LocalDate.of(1990, 5, 15), dto.getBirthDate());
        assertNull(dto.getDeathDate());
        assertEquals(ArtistGender.MALE, dto.getSoloGender());
    }

    @Test
    void testCreateArtistRequestDTO_GroupType() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        
        // When
        dto.setArtistName("Test Group");
        dto.setType(ArtistType.GROUP);
        dto.setFormationDate(LocalDate.of(2010, 1, 1));
        dto.setDisbandDate(LocalDate.of(2020, 12, 31));
        dto.setGroupGender(ArtistGender.MIXED);
        
        // Then
        assertEquals("Test Group", dto.getArtistName());
        assertEquals(ArtistType.GROUP, dto.getType());
        assertEquals(LocalDate.of(2010, 1, 1), dto.getFormationDate());
        assertEquals(LocalDate.of(2020, 12, 31), dto.getDisbandDate());
        assertEquals(ArtistGender.MIXED, dto.getGroupGender());
    }

    @Test
    void testCreateArtistRequestDTO_MinimalFields() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        
        // When
        dto.setArtistName("Minimal Artist");
        dto.setType(ArtistType.FRANCHISE);
        
        // Then
        assertEquals("Minimal Artist", dto.getArtistName());
        assertEquals(ArtistType.FRANCHISE, dto.getType());
        assertNull(dto.getSpotifyId());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getPrimaryLanguage());
        assertNull(dto.getGenre());
        assertNull(dto.getOriginCountry());
    }

    @Test
    void testCreateArtistRequestDTO_EqualsAndHashCode() {
        // Given
        CreateArtistRequestDTO dto1 = new CreateArtistRequestDTO();
        dto1.setArtistName("Test Artist");
        dto1.setType(ArtistType.SOLO);
        
        CreateArtistRequestDTO dto2 = new CreateArtistRequestDTO();
        dto2.setArtistName("Test Artist");
        dto2.setType(ArtistType.SOLO);
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testCreateArtistRequestDTO_ToString() {
        // Given
        CreateArtistRequestDTO dto = new CreateArtistRequestDTO();
        dto.setArtistName("Test Artist");
        dto.setType(ArtistType.SOLO);
        
        // When
        String toString = dto.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Artist"));
        assertTrue(toString.contains("SOLO"));
    }
}
