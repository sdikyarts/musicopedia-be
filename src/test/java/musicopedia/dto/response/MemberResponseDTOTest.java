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
        dto.setMemberName("HAN");
        dto.setRealName("Han Ji-sung");
        dto.setDescription("Main rapper and producer");
        dto.setImage("https://example.com/han.jpg");
        dto.setBirthDate(LocalDate.of(2000, 9, 14));
        dto.setSoloArtistId(soloArtistId);
        dto.setSoloArtistName("HAN");
        dto.setHasOfficialSoloDebut(true);
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("HAN", dto.getMemberName());
        assertEquals("Han Ji-sung", dto.getRealName());
        assertEquals("Main rapper and producer", dto.getDescription());
        assertEquals("https://example.com/han.jpg", dto.getImage());
        assertEquals(LocalDate.of(2000, 9, 14), dto.getBirthDate());
        assertEquals(soloArtistId, dto.getSoloArtistId());
        assertEquals("HAN", dto.getSoloArtistName());
        assertTrue(dto.getHasOfficialSoloDebut());
    }

    @Test
    void testMemberResponseDTO_WithoutSoloCareer() {
        // Given
        MemberResponseDTO dto = new MemberResponseDTO();
        UUID memberId = UUID.randomUUID();
        // When
        dto.setMemberId(memberId);
        dto.setMemberName("KIMCHAEWON");
        dto.setRealName("Kim Chae-won");
        dto.setDescription("Leader and main vocalist");
        dto.setImage("https://example.com/chaewon.jpg");
        dto.setBirthDate(LocalDate.of(2000, 8, 1));
        dto.setSoloArtistId(null);
        dto.setSoloArtistName(null);
        dto.setHasOfficialSoloDebut(false);
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("KIMCHAEWON", dto.getMemberName());
        assertEquals("Kim Chae-won", dto.getRealName());
        assertEquals("Leader and main vocalist", dto.getDescription());
        assertEquals("https://example.com/chaewon.jpg", dto.getImage());
        assertEquals(LocalDate.of(2000, 8, 1), dto.getBirthDate());
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
        dto.setMemberName("Felix");
        dto.setRealName("Felix Yongbok Lee");
        dto.setHasOfficialSoloDebut(false);
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Felix", dto.getMemberName());
        assertEquals("Felix Yongbok Lee", dto.getRealName());
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
        dto.setMemberName(null);
        dto.setDescription(null);
        dto.setImage(null);
        dto.setBirthDate(null);
        dto.setSoloArtistId(null);
        dto.setSoloArtistName(null);
        dto.setHasOfficialSoloDebut(null);
        
        // Then
        assertNull(dto.getMemberId());
        assertNull(dto.getMemberName());
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
        dto.setMemberName("");
        dto.setDescription("");
        dto.setImage("");
        dto.setSoloArtistName("");
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("", dto.getMemberName());
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
        dto.setMemberName("Alice Cooper");
        dto.setSoloArtistId(null);
        dto.setSoloArtistName("Alice Solo");
        dto.setHasOfficialSoloDebut(true);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Alice Cooper", dto.getMemberName());
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
        dto.setMemberName("Multi-talented Member");
        dto.setDescription(longDescription);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Multi-talented Member", dto.getMemberName());
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
        dto.setMemberName("Future Member");
        dto.setBirthDate(futureDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Future Member", dto.getMemberName());
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
        dto.setMemberName("Old Member");
        dto.setBirthDate(oldDate);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Old Member", dto.getMemberName());
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
        dto1.setMemberName("Test Member");
        dto1.setDescription("Test Description");
        dto1.setImage("test-image.jpg");
        dto1.setBirthDate(LocalDate.of(1990, 1, 1));
        dto1.setSoloArtistId(soloArtistId);
        dto1.setSoloArtistName("Test Solo");
        dto1.setHasOfficialSoloDebut(true);
        
        dto2.setMemberId(memberId);
        dto2.setMemberName("Test Member");
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
        dto1.setMemberName("Member 1");
        dto1.setHasOfficialSoloDebut(true);
        
        dto2.setMemberId(UUID.randomUUID());
        dto2.setMemberName("Member 2");
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
        dto.setMemberName("Test Member");
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
