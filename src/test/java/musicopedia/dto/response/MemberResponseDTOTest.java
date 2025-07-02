package musicopedia.dto.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberResponseDTOTest {

    @Test
    void testMemberResponseDTO_AllFields() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        UUID soloArtistId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("John Doe");
        dto.setDescription("Lead vocalist and main rapper");
        dto.setImage("https://example.com/john-doe.jpg");
        dto.setBirthDate(LocalDate.of(1995, 3, 15));
        dto.setSoloArtistId(soloArtistId);
        dto.setSoloArtistName("J.D.");
        dto.setHasOfficialSoloDebut(true);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("John Doe", dto.getFullName());
        assertEquals("Lead vocalist and main rapper", dto.getDescription());
        assertEquals("https://example.com/john-doe.jpg", dto.getImage());
        assertEquals(LocalDate.of(1995, 3, 15), dto.getBirthDate());
        assertEquals(soloArtistId, dto.getSoloArtistId());
        assertEquals("J.D.", dto.getSoloArtistName());
        assertTrue(dto.getHasOfficialSoloDebut());
    }

    @Test
    void testMemberResponseDTO_WithoutSoloCareer() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Jane Smith");
        dto.setDescription("Main dancer");
        dto.setImage("https://example.com/jane-smith.jpg");
        dto.setBirthDate(LocalDate.of(1998, 8, 22));
        dto.setSoloArtistId(null);
        dto.setSoloArtistName(null);
        dto.setHasOfficialSoloDebut(false);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Jane Smith", dto.getFullName());
        assertEquals("Main dancer", dto.getDescription());
        assertEquals("https://example.com/jane-smith.jpg", dto.getImage());
        assertEquals(LocalDate.of(1998, 8, 22), dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
        assertNull(dto.getSoloArtistName());
        assertFalse(dto.getHasOfficialSoloDebut());
    }

    @Test
    void testMemberResponseDTO_MinimalFields() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Bob Wilson");
        dto.setHasOfficialSoloDebut(false);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Bob Wilson", dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
        assertNull(dto.getSoloArtistName());
        assertFalse(dto.getHasOfficialSoloDebut());
    }

    @Test
    void testMemberResponseDTO_NullValues() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        
        // When
        dto.setMemberId(null);
        dto.setFullName(null);
        dto.setDescription(null);
        dto.setImage(null);
        dto.setBirthDate(null);
        dto.setSoloArtistId(null);
        dto.setSoloArtistName(null);
        dto.setHasOfficialSoloDebut(null);
        
        // Then
        assertNull(dto.getMemberId());
        assertNull(dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
        assertNull(dto.getSoloArtistName());
        assertNull(dto.getHasOfficialSoloDebut());
    }

    @Test
    void testMemberResponseDTO_EmptyStrings() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("");
        dto.setDescription("");
        dto.setImage("");
        dto.setSoloArtistName("");
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("", dto.getFullName());
        assertEquals("", dto.getDescription());
        assertEquals("", dto.getImage());
        assertEquals("", dto.getSoloArtistName());
    }

    @Test
    void testMemberResponseDTO_WithSoloCareerButNullSoloArtistId() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Alice Cooper");
        dto.setSoloArtistId(null);
        dto.setSoloArtistName("Alice Solo");
        dto.setHasOfficialSoloDebut(true);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Alice Cooper", dto.getFullName());
        assertNull(dto.getSoloArtistId());
        assertEquals("Alice Solo", dto.getSoloArtistName());
        assertTrue(dto.getHasOfficialSoloDebut());
    }

    @Test
    void testMemberResponseDTO_LongDescription() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        String longDescription = "This member is incredibly talented with expertise in vocals, dance, rap, and songwriting. They have been with the group since debut and have shown tremendous growth. ".repeat(3);
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Multi-talented Member");
        dto.setDescription(longDescription);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Multi-talented Member", dto.getFullName());
        assertEquals(longDescription, dto.getDescription());
    }

    @Test
    void testMemberResponseDTO_FutureBirthDate() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        LocalDate futureDate = LocalDate.now().plusYears(1);
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Future Member");
        dto.setBirthDate(futureDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Future Member", dto.getFullName());
        assertEquals(futureDate, dto.getBirthDate());
    }

    @Test
    void testMemberResponseDTO_VeryOldBirthDate() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Old Member");
        dto.setBirthDate(oldDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Old Member", dto.getFullName());
        assertEquals(oldDate, dto.getBirthDate());
    }

    @Test
    void testMemberResponseDTO_EqualsAndHashCode() {
        // Given
        MemberResponseDTO dto1 = new MemberResponseDTO();
        MemberResponseDTO dto2 = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        UUID soloArtistId = UUID.randomUUID();
        
        dto1.setMemberId(memberId);
        dto1.setFullName("Test Member");
        dto1.setDescription("Test Description");
        dto1.setImage("test-image.jpg");
        dto1.setBirthDate(LocalDate.of(1990, 1, 1));
        dto1.setSoloArtistId(soloArtistId);
        dto1.setSoloArtistName("Test Solo");
        dto1.setHasOfficialSoloDebut(true);
        
        dto2.setMemberId(memberId);
        dto2.setFullName("Test Member");
        dto2.setDescription("Test Description");
        dto2.setImage("test-image.jpg");
        dto2.setBirthDate(LocalDate.of(1990, 1, 1));
        dto2.setSoloArtistId(soloArtistId);
        dto2.setSoloArtistName("Test Solo");
        dto2.setHasOfficialSoloDebut(true);
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testMemberResponseDTO_NotEquals() {
        // Given
        MemberResponseDTO dto1 = new MemberResponseDTO();
        MemberResponseDTO dto2 = new MemberResponseDTO();
        
        dto1.setMemberId(UUID.randomUUID());
        dto1.setFullName("Member 1");
        dto1.setHasOfficialSoloDebut(true);
        
        dto2.setMemberId(UUID.randomUUID());
        dto2.setFullName("Member 2");
        dto2.setHasOfficialSoloDebut(false);
        
        // Then
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testMemberResponseDTO_ToString() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        dto.setMemberId(memberId);
        dto.setFullName("Test Member");
        dto.setDescription("Test Description");
        dto.setHasOfficialSoloDebut(true);
        
        // When
        String result = dto.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("Test Member"));
        assertTrue(result.contains("Test Description"));
        assertTrue(result.contains("true"));
    }

    @Test
    void testMemberResponseDTO_BooleanFlags() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        
        // When - Test true case
        dto.setMemberId(memberId);
        dto.setHasOfficialSoloDebut(true);
        
        // Then
        assertTrue(dto.getHasOfficialSoloDebut());
        
        // When - Test false case
        dto.setHasOfficialSoloDebut(false);
        
        // Then
        assertFalse(dto.getHasOfficialSoloDebut());
    }
}
