package musicopedia.dto.request;

import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.ArtistType;
import musicopedia.model.enums.GroupActivityStatus;
import musicopedia.model.enums.GroupAffiliationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ArtistRequestDTOTest {

    @Test
    void testArtistRequestDTO_SoloArtist() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        
        // When
        dto.setArtistName("Test Solo Artist");
        dto.setType(ArtistType.SOLO);
        dto.setSpotifyId("solo123");
        dto.setDescription("Solo artist description");
        dto.setImage("solo-image.jpg");
        dto.setPrimaryLanguage("English");
        dto.setGenre("Pop");
        dto.setOriginCountry("US");
        
        // Solo fields
        dto.setBirthDate(LocalDate.of(1990, 5, 15));
        dto.setDeathDate(null);
        dto.setSoloGender(ArtistGender.MALE);
        dto.setGroupAffiliationStatus(GroupAffiliationStatus.NEVER_IN_A_GROUP);
        
        // Then
        assertEquals("Test Solo Artist", dto.getArtistName());
        assertEquals(ArtistType.SOLO, dto.getType());
        assertEquals("solo123", dto.getSpotifyId());
        assertEquals("Solo artist description", dto.getDescription());
        assertEquals("solo-image.jpg", dto.getImage());
        assertEquals("English", dto.getPrimaryLanguage());
        assertEquals("Pop", dto.getGenre());
        assertEquals("US", dto.getOriginCountry());
        assertEquals(LocalDate.of(1990, 5, 15), dto.getBirthDate());
        assertNull(dto.getDeathDate());
        assertEquals(ArtistGender.MALE, dto.getSoloGender());
        assertEquals(GroupAffiliationStatus.NEVER_IN_A_GROUP, dto.getGroupAffiliationStatus());
    }

    @Test
    void testArtistRequestDTO_GroupArtist() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        
        // When
        dto.setArtistName("Test Group");
        dto.setType(ArtistType.GROUP);
        dto.setSpotifyId("group123");
        dto.setDescription("Group description");
        dto.setImage("group-image.jpg");
        dto.setPrimaryLanguage("Spanish");
        dto.setGenre("Rock");
        dto.setOriginCountry("ES");
        
        // Group fields
        dto.setFormationDate(LocalDate.of(2005, 3, 10));
        dto.setDisbandDate(LocalDate.of(2015, 8, 20));
        dto.setGroupGender(ArtistGender.FEMALE);
        dto.setActivityStatus(GroupActivityStatus.DISBANDED);
        
        // Then
        assertEquals("Test Group", dto.getArtistName());
        assertEquals(ArtistType.GROUP, dto.getType());
        assertEquals("group123", dto.getSpotifyId());
        assertEquals("Group description", dto.getDescription());
        assertEquals("group-image.jpg", dto.getImage());
        assertEquals("Spanish", dto.getPrimaryLanguage());
        assertEquals("Rock", dto.getGenre());
        assertEquals("ES", dto.getOriginCountry());
        assertEquals(LocalDate.of(2005, 3, 10), dto.getFormationDate());
        assertEquals(LocalDate.of(2015, 8, 20), dto.getDisbandDate());
        assertEquals(ArtistGender.FEMALE, dto.getGroupGender());
        assertEquals(GroupActivityStatus.DISBANDED, dto.getActivityStatus());
    }

    @Test
    void testArtistRequestDTO_CommonFields() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        
        // When
        dto.setArtistName("Common Artist");
        dto.setType(ArtistType.VARIOUS);
        dto.setSpotifyId("common123");
        dto.setDescription("Common description");
        dto.setImage("common-image.jpg");
        dto.setPrimaryLanguage("French");
        dto.setGenre("Jazz");
        dto.setOriginCountry("FR");
        
        // Then
        assertEquals("Common Artist", dto.getArtistName());
        assertEquals(ArtistType.VARIOUS, dto.getType());
        assertEquals("common123", dto.getSpotifyId());
        assertEquals("Common description", dto.getDescription());
        assertEquals("common-image.jpg", dto.getImage());
        assertEquals("French", dto.getPrimaryLanguage());
        assertEquals("Jazz", dto.getGenre());
        assertEquals("FR", dto.getOriginCountry());
    }

    @Test
    void testArtistRequestDTO_AllFieldsNull() {
        // Given
        ArtistRequestDTO dto = new ArtistRequestDTO();
        
        // Then - all fields should default to null
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
        assertNull(dto.getGroupAffiliationStatus());
        assertNull(dto.getFormationDate());
        assertNull(dto.getDisbandDate());
        assertNull(dto.getGroupGender());
        assertNull(dto.getActivityStatus());
    }

    @Test
    void testArtistRequestDTO_LombokFunctionality() {
        // Given
        ArtistRequestDTO dto1 = new ArtistRequestDTO();
        ArtistRequestDTO dto2 = new ArtistRequestDTO();
        
        // When
        dto1.setArtistName("Same Artist");
        dto2.setArtistName("Same Artist");
        
        // Then - test equals and hashCode (provided by Lombok @Data)
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        
        // Test toString (provided by Lombok @Data)
        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Same Artist"));
    }
}
