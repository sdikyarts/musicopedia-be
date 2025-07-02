package musicopedia.dto.response;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberSummaryDTOTest {

    @Test
    void testMemberSummaryDTO_AllFields() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("John Doe");
        dto.setImage("https://example.com/john-doe.jpg");
        dto.setHasOfficialSoloDebut(true);
        dto.setSoloArtistName("J.D.");
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("John Doe", dto.getFullName());
        assertEquals("https://example.com/john-doe.jpg", dto.getImage());
        assertTrue(dto.getHasOfficialSoloDebut());
        assertEquals("J.D.", dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_WithoutSoloCareer() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Jane Smith");
        dto.setImage("https://example.com/jane-smith.jpg");
        dto.setHasOfficialSoloDebut(false);
        dto.setSoloArtistName(null);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Jane Smith", dto.getFullName());
        assertEquals("https://example.com/jane-smith.jpg", dto.getImage());
        assertFalse(dto.getHasOfficialSoloDebut());
        assertNull(dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_MinimalFields() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Bob Wilson");
        dto.setHasOfficialSoloDebut(false);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Bob Wilson", dto.getFullName());
        assertNull(dto.getImage());
        assertFalse(dto.getHasOfficialSoloDebut());
        assertNull(dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_NullValues() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        
        // When
        dto.setMemberId(null);
        dto.setFullName(null);
        dto.setImage(null);
        dto.setHasOfficialSoloDebut(null);
        dto.setSoloArtistName(null);
        
        // Then
        assertNull(dto.getMemberId());
        assertNull(dto.getFullName());
        assertNull(dto.getImage());
        assertNull(dto.getHasOfficialSoloDebut());
        assertNull(dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_EmptyStrings() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("");
        dto.setImage("");
        dto.setSoloArtistName("");
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("", dto.getFullName());
        assertEquals("", dto.getImage());
        assertEquals("", dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_WithSoloCareerButNullSoloArtistName() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Alice Cooper");
        dto.setHasOfficialSoloDebut(true);
        dto.setSoloArtistName(null);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Alice Cooper", dto.getFullName());
        assertTrue(dto.getHasOfficialSoloDebut());
        assertNull(dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_LongFullName() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        String longName = "This Is A Very Long Full Name That Might Be Used In Some Cases";
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName(longName);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(longName, dto.getFullName());
    }

    @Test
    void testMemberSummaryDTO_LongSoloArtistName() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        String longSoloName = "This Is A Very Long Solo Artist Name";
        
        // When
        dto.setMemberId(memberId);
        dto.setFullName("Regular Name");
        dto.setHasOfficialSoloDebut(true);
        dto.setSoloArtistName(longSoloName);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Regular Name", dto.getFullName());
        assertTrue(dto.getHasOfficialSoloDebut());
        assertEquals(longSoloName, dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_EqualsAndHashCode() {
        // Given
        MemberSummaryDTO dto1 = new MemberSummaryDTO();
        MemberSummaryDTO dto2 = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        dto1.setMemberId(memberId);
        dto1.setFullName("Test Member");
        dto1.setImage("test-image.jpg");
        dto1.setHasOfficialSoloDebut(true);
        dto1.setSoloArtistName("Test Solo");
        
        dto2.setMemberId(memberId);
        dto2.setFullName("Test Member");
        dto2.setImage("test-image.jpg");
        dto2.setHasOfficialSoloDebut(true);
        dto2.setSoloArtistName("Test Solo");
        
        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testMemberSummaryDTO_NotEquals() {
        // Given
        MemberSummaryDTO dto1 = new MemberSummaryDTO();
        MemberSummaryDTO dto2 = new MemberSummaryDTO();
        
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
    void testMemberSummaryDTO_ToString() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        dto.setMemberId(memberId);
        dto.setFullName("Test Member");
        dto.setHasOfficialSoloDebut(true);
        dto.setSoloArtistName("Test Solo");
        
        // When
        String result = dto.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("Test Member"));
        assertTrue(result.contains("Test Solo"));
        assertTrue(result.contains("true"));
    }

    @Test
    void testMemberSummaryDTO_BooleanFlags() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
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

    @Test
    void testMemberSummaryDTO_SoloCareerInformation() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When - Member with solo career
        dto.setMemberId(memberId);
        dto.setFullName("Solo Member");
        dto.setHasOfficialSoloDebut(true);
        dto.setSoloArtistName("Solo Artist Name");
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals("Solo Member", dto.getFullName());
        assertTrue(dto.getHasOfficialSoloDebut());
        assertEquals("Solo Artist Name", dto.getSoloArtistName());
        
        // When - Member without solo career
        dto.setHasOfficialSoloDebut(false);
        dto.setSoloArtistName(null);
        
        // Then
        assertFalse(dto.getHasOfficialSoloDebut());
        assertNull(dto.getSoloArtistName());
    }

    @Test
    void testMemberSummaryDTO_ImageUrl() {
        // Given
        MemberSummaryDTO dto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        String imageUrl = "https://cdn.example.com/images/members/profile-picture.jpg";
        
        // When
        dto.setMemberId(memberId);
        dto.setImage(imageUrl);
        
        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(imageUrl, dto.getImage());
    }

    @Test
    void testMemberSummaryDTO_ComparedToResponseDTO() {
        // Given - Summary DTO should have fewer fields than Response DTO
        MemberSummaryDTO summaryDto = new MemberSummaryDTO();
        UUID memberId = UUID.randomUUID();
        
        // When
        summaryDto.setMemberId(memberId);
        summaryDto.setFullName("Test Member");
        summaryDto.setImage("test-image.jpg");
        summaryDto.setHasOfficialSoloDebut(true);
        summaryDto.setSoloArtistName("Test Solo");
        
        // Then - Verify it only has the essential fields for summary view
        assertEquals(memberId, summaryDto.getMemberId());
        assertEquals("Test Member", summaryDto.getFullName());
        assertEquals("test-image.jpg", summaryDto.getImage());
        assertTrue(summaryDto.getHasOfficialSoloDebut());
        assertEquals("Test Solo", summaryDto.getSoloArtistName());
        
        // Note: Summary DTO intentionally lacks description and birthDate fields
        // that are present in MemberResponseDTO
    }
}
