package musicopedia.dto.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateMemberRequestDTOTest {

    @Test
    void testUpdateMemberRequestDTO_AllFields() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        UUID soloArtistId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("John Doe Updated");
        dto.setDescription("Updated description for the member");
        dto.setImage("https://example.com/john-doe-updated.jpg");
        dto.setBirthDate(LocalDate.of(1995, 3, 15));
        dto.setSoloArtistId(soloArtistId);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("John Doe Updated", dto.getFullName());
        assertEquals("Updated description for the member", dto.getDescription());
        assertEquals("https://example.com/john-doe-updated.jpg", dto.getImage());
        assertEquals(LocalDate.of(1995, 3, 15), dto.getBirthDate());
        assertEquals(soloArtistId, dto.getSoloArtistId());
    }

    @Test
    void testUpdateMemberRequestDTO_MinimalFields() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertNull(dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testUpdateMemberRequestDTO_PartialUpdate() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        
        // When - only updating some fields
        dto.setMemberId(memberId);
        dto.setFullName("Jane Smith Updated");
        dto.setDescription("New description");
        // Not setting image, birthDate, or soloArtistId
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Jane Smith Updated", dto.getFullName());
        assertEquals("New description", dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testUpdateMemberRequestDTO_RemoveSoloArtist() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        
        // When - explicitly setting soloArtistId to null to remove the reference
        dto.setMemberId(memberId);
        dto.setFullName("Bob Wilson");
        dto.setSoloArtistId(null);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Bob Wilson", dto.getFullName());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testUpdateMemberRequestDTO_UpdateSoloArtist() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        UUID newSoloArtistId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setSoloArtistId(newSoloArtistId);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(newSoloArtistId, dto.getSoloArtistId());
    }

    @Test
    void testUpdateMemberRequestDTO_NullValues() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        
        // When
        dto.setMemberId(null);
        dto.setFullName(null);
        dto.setDescription(null);
        dto.setImage(null);
        dto.setBirthDate(null);
        dto.setSoloArtistId(null);
        
        // Then
        assertNull(dto.getMemberId());
        assertNull(dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testUpdateMemberRequestDTO_EmptyStrings() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("");
        dto.setDescription("");
        dto.setImage("");
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("", dto.getFullName());
        assertEquals("", dto.getDescription());
        assertEquals("", dto.getImage());
    }

    @Test
    void testUpdateMemberRequestDTO_UpdateBirthDate() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        LocalDate newBirthDate = LocalDate.of(1992, 7, 20);
        
        // When
        dto.setMemberId(memberId);
        dto.setBirthDate(newBirthDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(newBirthDate, dto.getBirthDate());
    }

    @Test
    void testUpdateMemberRequestDTO_LongDescription() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        String longDescription = "This is a very long updated description that contains extensive information about the member's recent activities and achievements. ".repeat(5);
        
        // When
        dto.setMemberId(memberId);
        dto.setDescription(longDescription);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(longDescription, dto.getDescription());
    }

    @Test
    void testUpdateMemberRequestDTO_EqualsAndHashCode() {
        // Given
        UpdateMemberRequestDTO dto1 = new UpdateMemberRequestDTO();
        UpdateMemberRequestDTO dto2 = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        UUID soloArtistId = UUID.randomUUID();
        
        dto1.setMemberId(memberId);
        dto1.setFullName("Test Member");
        dto1.setDescription("Test Description");
        dto1.setImage("test-image.jpg");
        dto1.setBirthDate(LocalDate.of(1990, 1, 1));
        dto1.setSoloArtistId(soloArtistId);
        
        dto2.setMemberId(memberId);
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
    void testUpdateMemberRequestDTO_NotEquals() {
        // Given
        UpdateMemberRequestDTO dto1 = new UpdateMemberRequestDTO();
        UpdateMemberRequestDTO dto2 = new UpdateMemberRequestDTO();
        
        dto1.setMemberId(UUID.randomUUID());
        dto1.setFullName("Member 1");
        
        dto2.setMemberId(UUID.randomUUID());
        dto2.setFullName("Member 2");
        
        // Then
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testUpdateMemberRequestDTO_ToString() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        dto.setMemberId(memberId);
        dto.setFullName("Test Member");
        dto.setDescription("Test Description");
        
        // When
        String result = dto.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("Test Member"));
        assertTrue(result.contains("Test Description"));
    }

    @Test
    void testUpdateMemberRequestDTO_FutureBirthDate() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        LocalDate futureDate = LocalDate.now().plusYears(1);
        
        // When
        dto.setMemberId(memberId);
        dto.setBirthDate(futureDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(futureDate, dto.getBirthDate());
    }

    @Test
    void testUpdateMemberRequestDTO_VeryOldBirthDate() {
        // Given
        UpdateMemberRequestDTO dto = new UpdateMemberRequestDTO();
        UUID memberId = UUID.randomUUID();
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        
        // When
        dto.setMemberId(memberId);
        dto.setBirthDate(oldDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(oldDate, dto.getBirthDate());
    }
}
