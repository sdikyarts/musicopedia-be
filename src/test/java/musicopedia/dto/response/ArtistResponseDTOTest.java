package musicopedia.dto.response;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArtistResponseDTOTest {

    @Test
    void testArtistResponseDTO_AllFields() {
        // Given
        ArtistResponseDTO dto = new ArtistResponseDTO();
        UUID artistId = UUID.randomUUID();
        
        // When
        dto.setArtistId(artistId);
        dto.setSpotifyId("spotify123");
        dto.setArtistName("Response Artist");
        dto.setDescription("Response description");
        dto.setImage("response-image.jpg");
        dto.setType(ArtistType.SOLO);
        dto.setPrimaryLanguage("French");
        dto.setGenre("Classical");
        dto.setOriginCountry("FR");
        
        // Solo fields
        dto.setBirthDate(LocalDate.of(1980, 4, 20));
        dto.setDeathDate(LocalDate.of(2010, 11, 15));
        dto.setSoloGender(ArtistGender.MALE);
        
        // Then
        assertEquals(artistId, dto.getArtistId());
        assertEquals("spotify123", dto.getSpotifyId());
        assertEquals("Response Artist", dto.getArtistName());
        assertEquals("Response description", dto.getDescription());
        assertEquals("response-image.jpg", dto.getImage());
        assertEquals(ArtistType.SOLO, dto.getType());
        assertEquals("French", dto.getPrimaryLanguage());
        assertEquals("Classical", dto.getGenre());
        assertEquals("FR", dto.getOriginCountry());
        assertEquals(LocalDate.of(1980, 4, 20), dto.getBirthDate());
        assertEquals(LocalDate.of(2010, 11, 15), dto.getDeathDate());
        assertEquals(ArtistGender.MALE, dto.getSoloGender());
    }

    @Test
    void testArtistResponseDTO_GroupFields() {
        // Given
        ArtistResponseDTO dto = new ArtistResponseDTO();
        UUID artistId = UUID.randomUUID();
        
        // When
        dto.setArtistId(artistId);
        dto.setArtistName("Response Group");
        dto.setType(ArtistType.GROUP);
        
        // Group fields
        dto.setFormationDate(LocalDate.of(2000, 1, 1));
        dto.setDisbandDate(LocalDate.of(2010, 12, 31));
        dto.setGroupGender(ArtistGender.MIXED);
        
        // Then
        assertEquals(artistId, dto.getArtistId());
        assertEquals("Response Group", dto.getArtistName());
        assertEquals(ArtistType.GROUP, dto.getType());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getFormationDate());
        assertEquals(LocalDate.of(2010, 12, 31), dto.getDisbandDate());
        assertEquals(ArtistGender.MIXED, dto.getGroupGender());
    }

    @Test
    void testArtistResponseDTO_MinimalFields() {
        // Given
        ArtistResponseDTO dto = new ArtistResponseDTO();
        UUID artistId = UUID.randomUUID();
        
        // When
        dto.setArtistId(artistId);
        dto.setArtistName("Minimal Response");
        dto.setType(ArtistType.VARIOUS);
        
        // Then
        assertEquals(artistId, dto.getArtistId());
        assertEquals("Minimal Response", dto.getArtistName());
        assertEquals(ArtistType.VARIOUS, dto.getType());
        assertNull(dto.getSpotifyId());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getPrimaryLanguage());
        assertNull(dto.getGenre());
        assertNull(dto.getOriginCountry());
    }

    @Test
    void testArtistResponseDTO_EqualsAndHashCode() {
        // Given
        UUID artistId = UUID.randomUUID();
        
        ArtistResponseDTO dto1 = new ArtistResponseDTO();
        dto1.setArtistId(artistId);
        dto1.setArtistName("Test Artist");
        dto1.setType(ArtistType.SOLO);
        
        ArtistResponseDTO dto2 = new ArtistResponseDTO();
        dto2.setArtistId(artistId);
        dto2.setArtistName("Test Artist");
        dto2.setType(ArtistType.SOLO);
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testArtistResponseDTO_ToString() {
        // Given
        ArtistResponseDTO dto = new ArtistResponseDTO();
        dto.setArtistId(UUID.randomUUID());
        dto.setArtistName("Test Response");
        dto.setType(ArtistType.GROUP);
        
        // When
        String toString = dto.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Response"));
        assertTrue(toString.contains("GROUP"));
    }
}
