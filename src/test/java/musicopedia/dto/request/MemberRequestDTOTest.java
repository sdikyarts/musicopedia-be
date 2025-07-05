package musicopedia.dto.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberRequestDTOTest {

    @Test
    void testMemberRequestDTO_AllFields() {
        // Given
        UUID memberId = UUID.randomUUID();
        UUID soloArtistId = UUID.randomUUID();
        String memberName = "Hyunjin";
        String realName = "Hwang Hyun-jin";
        String description = "Main dancer and visual of Stray Kids";
        String image = "http://example.com/hyunjin.jpg";
        LocalDate birthDate = LocalDate.of(2000, 3, 20);

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberId(memberId);
        dto.setMemberName(memberName);
        dto.setRealName(realName);
        dto.setDescription(description);
        dto.setImage(image);
        dto.setBirthDate(birthDate);
        dto.setSoloArtistId(soloArtistId);

        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(memberName, dto.getMemberName());
        assertEquals(realName, dto.getRealName());
        assertEquals(description, dto.getDescription());
        assertEquals(image, dto.getImage());
        assertEquals(birthDate, dto.getBirthDate());
        assertEquals(soloArtistId, dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_MinimalFields() {
        // Given
        String memberName2 = "KIMCHAEWON";
        String realName2 = "Kim Chae-won";

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberName(memberName2);
        dto.setRealName(realName2);

        // Then
        assertNull(dto.getMemberId());
        assertEquals(memberName2, dto.getMemberName());
        assertEquals(realName2, dto.getRealName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_ForCreateOperation() {
        // Given - typical create scenario (no memberId)
        String memberName = "Alice Johnson";
        String description = "Main rapper";
        LocalDate birthDate = LocalDate.of(1998, 7, 22);

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberName(memberName);
        dto.setDescription(description);
        dto.setBirthDate(birthDate);
        // memberId should remain null for create operations

        // Then
        assertNull(dto.getMemberId()); // Important: no ID for create
        assertEquals(memberName, dto.getMemberName());
        assertEquals(description, dto.getDescription());
        assertEquals(birthDate, dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_ForUpdateOperation() {
        // Given - typical update scenario (with memberId)
        UUID memberId = UUID.randomUUID();
        String memberName = "Bob Wilson";
        String description = "Lead guitarist";
        String image = "http://example.com/bob-wilson.jpg";
        LocalDate birthDate = LocalDate.of(1992, 11, 8);

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberId(memberId); // ID present for update
        dto.setMemberName(memberName);
        dto.setDescription(description);
        dto.setImage(image);
        dto.setBirthDate(birthDate);

        // Then
        assertEquals(memberId, dto.getMemberId()); // Important: ID present for update
        assertEquals(memberName, dto.getMemberName());
        assertEquals(description, dto.getDescription());
        assertEquals(image, dto.getImage());
        assertEquals(birthDate, dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_WithSoloArtist() {
        // Given
        UUID soloArtistId = UUID.randomUUID();
        String memberName = "Charlie Brown";

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberName(memberName);
        dto.setSoloArtistId(soloArtistId);

        // Then
        assertEquals(memberName, dto.getMemberName());
        assertEquals(soloArtistId, dto.getSoloArtistId());
        assertNull(dto.getMemberId());
    }

    @Test
    void testMemberRequestDTO_EmptyValues() {
        // When
        MemberRequestDTO dto = new MemberRequestDTO();

        // Then
        assertNull(dto.getMemberId());
        assertNull(dto.getMemberName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }
}
