package musicopedia.dto.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateMemberRequestDTOTest {

    @Test
    void testCreateMemberRequestDTO_AllFields() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        UUID soloArtistId = UUID.randomUUID();
        
        // When
        dto.setFullName("John Doe");
        dto.setDescription("A talented member of the group");
        dto.setImage("https://example.com/john-doe.jpg");
        dto.setBirthDate(LocalDate.of(1995, 3, 15));
        dto.setSoloArtistId(soloArtistId);
        
        // Then
        assertEquals("John Doe", dto.getFullName());
        assertEquals("A talented member of the group", dto.getDescription());
        assertEquals("https://example.com/john-doe.jpg", dto.getImage());
        assertEquals(LocalDate.of(1995, 3, 15), dto.getBirthDate());
        assertEquals(soloArtistId, dto.getSoloArtistId());
    }

    @Test
    void testCreateMemberRequestDTO_MinimalFields() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        
        // When
        dto.setFullName("Jane Smith");
        
        // Then
        assertEquals("Jane Smith", dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testCreateMemberRequestDTO_WithoutSoloArtist() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        
        // When
        dto.setFullName("Bob Wilson");
        dto.setDescription("Lead vocalist");
        dto.setImage("https://example.com/bob-wilson.jpg");
        dto.setBirthDate(LocalDate.of(1990, 12, 25));
        // No solo artist ID set
        
        // Then
        assertEquals("Bob Wilson", dto.getFullName());
        assertEquals("Lead vocalist", dto.getDescription());
        assertEquals("https://example.com/bob-wilson.jpg", dto.getImage());
        assertEquals(LocalDate.of(1990, 12, 25), dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testCreateMemberRequestDTO_NullValues() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        
        // When
        dto.setFullName(null);
        dto.setDescription(null);
        dto.setImage(null);
        dto.setBirthDate(null);
        dto.setSoloArtistId(null);
        
        // Then
        assertNull(dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testCreateMemberRequestDTO_EmptyStrings() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        
        // When
        dto.setFullName("");
        dto.setDescription("");
        dto.setImage("");
        
        // Then
        assertEquals("", dto.getFullName());
        assertEquals("", dto.getDescription());
        assertEquals("", dto.getImage());
    }

    @Test
    void testCreateMemberRequestDTO_LongDescription() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        String longDescription = "This is a very long description that contains a lot of information about the member, including their background, skills, and contributions to the group. ".repeat(10);
        
        // When
        dto.setFullName("Alice Cooper");
        dto.setDescription(longDescription);
        
        // Then
        assertEquals("Alice Cooper", dto.getFullName());
        assertEquals(longDescription, dto.getDescription());
    }

    @Test
    void testCreateMemberRequestDTO_FutureBirthDate() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        LocalDate futureDate = LocalDate.now().plusYears(1);
        
        // When
        dto.setFullName("Future Member");
        dto.setBirthDate(futureDate);
        
        // Then
        assertEquals("Future Member", dto.getFullName());
        assertEquals(futureDate, dto.getBirthDate());
    }

    @Test
    void testCreateMemberRequestDTO_VeryOldBirthDate() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        
        // When
        dto.setFullName("Old Member");
        dto.setBirthDate(oldDate);
        
        // Then
        assertEquals("Old Member", dto.getFullName());
        assertEquals(oldDate, dto.getBirthDate());
    }

    @Test
    void testCreateMemberRequestDTO_EqualsAndHashCode() {
        // Given
        CreateMemberRequestDTO dto1 = new CreateMemberRequestDTO();
        CreateMemberRequestDTO dto2 = new CreateMemberRequestDTO();
        UUID soloArtistId = UUID.randomUUID();
        
        dto1.setFullName("Test Member");
        dto1.setDescription("Test Description");
        dto1.setImage("test-image.jpg");
        dto1.setBirthDate(LocalDate.of(1990, 1, 1));
        dto1.setSoloArtistId(soloArtistId);
        
        dto2.setFullName("Test Member");
        dto2.setDescription("Test Description");
        dto2.setImage("test-image.jpg");
        dto2.setBirthDate(LocalDate.of(1990, 1, 1));
        dto2.setSoloArtistId(soloArtistId);
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testCreateMemberRequestDTO_ToString() {
        // Given
        CreateMemberRequestDTO dto = new CreateMemberRequestDTO();
        dto.setFullName("Test Member");
        dto.setDescription("Test Description");
        
        // When
        String result = dto.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("Test Member"));
        assertTrue(result.contains("Test Description"));
    }
}
