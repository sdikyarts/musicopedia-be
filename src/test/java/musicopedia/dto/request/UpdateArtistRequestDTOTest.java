package musicopedia.dto.request;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UpdateArtistRequestDTOTest {

    @Test
    void testUpdateArtistRequestDTO_AllFields() {
        // Given
        UpdateArtistRequestDTO dto = new UpdateArtistRequestDTO();
        
        // When
        dto.setArtistName("Updated Artist");
        dto.setType(ArtistType.GROUP);
        dto.setSpotifyId("updated123");
        dto.setDescription("Updated description");
        dto.setImage("updated-image.jpg");
        dto.setPrimaryLanguage("Spanish");
        dto.setGenre("Rock");
        dto.setOriginCountry("ES");
        
        // Group fields
        dto.setFormationDate(LocalDate.of(2005, 3, 10));
        dto.setDisbandDate(LocalDate.of(2015, 8, 20));
        dto.setGroupGender(ArtistGender.FEMALE);
        
        // Then
        assertEquals("Updated Artist", dto.getArtistName());
        assertEquals(ArtistType.GROUP, dto.getType());
        assertEquals("updated123", dto.getSpotifyId());
        assertEquals("Updated description", dto.getDescription());
        assertEquals("updated-image.jpg", dto.getImage());
        assertEquals("Spanish", dto.getPrimaryLanguage());
        assertEquals("Rock", dto.getGenre());
        assertEquals("ES", dto.getOriginCountry());
        assertEquals(LocalDate.of(2005, 3, 10), dto.getFormationDate());
        assertEquals(LocalDate.of(2015, 8, 20), dto.getDisbandDate());
        assertEquals(ArtistGender.FEMALE, dto.getGroupGender());
    }

    @Test
    void testUpdateArtistRequestDTO_SoloType() {
        // Given
        UpdateArtistRequestDTO dto = new UpdateArtistRequestDTO();
        
        // When
        dto.setArtistName("Updated Solo Artist");
        dto.setType(ArtistType.SOLO);
        dto.setBirthDate(LocalDate.of(1985, 12, 25));
        dto.setDeathDate(LocalDate.of(2020, 6, 10));
        dto.setSoloGender(ArtistGender.FEMALE);
        
        // Then
        assertEquals("Updated Solo Artist", dto.getArtistName());
        assertEquals(ArtistType.SOLO, dto.getType());
        assertEquals(LocalDate.of(1985, 12, 25), dto.getBirthDate());
        assertEquals(LocalDate.of(2020, 6, 10), dto.getDeathDate());
        assertEquals(ArtistGender.FEMALE, dto.getSoloGender());
    }

    @Test
    void testUpdateArtistRequestDTO_PartialUpdate() {
        // Given
        UpdateArtistRequestDTO dto = new UpdateArtistRequestDTO();
        
        // When - Only updating some fields
        dto.setArtistName("Partially Updated");
        dto.setGenre("Jazz");
        
        // Then
        assertEquals("Partially Updated", dto.getArtistName());
        assertEquals("Jazz", dto.getGenre());
        assertNull(dto.getType());
        assertNull(dto.getSpotifyId());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getPrimaryLanguage());
        assertNull(dto.getOriginCountry());
    }

    @Test
    void testUpdateArtistRequestDTO_EmptyDTO() {
        // Given
        UpdateArtistRequestDTO dto = new UpdateArtistRequestDTO();
        
        // Then - All fields should be null by default
        assertNull(dto.getArtistName());
        assertNull(dto.getType());
        assertNull(dto.getSpotifyId());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getPrimaryLanguage());
        assertNull(dto.getGenre());
        assertNull(dto.getOriginCountry());
        assertNull(dto.getBirthDate());
        assertNull(dto.getDeathDate());
        assertNull(dto.getSoloGender());
        assertNull(dto.getFormationDate());
        assertNull(dto.getDisbandDate());
        assertNull(dto.getGroupGender());
    }

    @Test
    void testUpdateArtistRequestDTO_EqualsAndHashCode() {
        // Given
        UpdateArtistRequestDTO dto1 = new UpdateArtistRequestDTO();
        dto1.setArtistName("Test Artist");
        dto1.setType(ArtistType.VARIOUS);
        
        UpdateArtistRequestDTO dto2 = new UpdateArtistRequestDTO();
        dto2.setArtistName("Test Artist");
        dto2.setType(ArtistType.VARIOUS);
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testUpdateArtistRequestDTO_ToString() {
        // Given
        UpdateArtistRequestDTO dto = new UpdateArtistRequestDTO();
        dto.setArtistName("Test Update");
        dto.setType(ArtistType.FRANCHISE);
        
        // When
        String toString = dto.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Update"));
        assertTrue(toString.contains("FRANCHISE"));
    }
}
