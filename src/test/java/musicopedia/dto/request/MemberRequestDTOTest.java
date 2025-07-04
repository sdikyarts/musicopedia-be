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
        String fullName = "John Doe";
        String description = "Lead vocalist and dancer";
        String image = "http://example.com/john-doe.jpg";
        LocalDate birthDate = LocalDate.of(1995, 3, 15);

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberId(memberId);
        dto.setFullName(fullName);
        dto.setDescription(description);
        dto.setImage(image);
        dto.setBirthDate(birthDate);
        dto.setSoloArtistId(soloArtistId);

        // Then
        assertEquals(memberId, dto.getMemberId());
        assertEquals(fullName, dto.getFullName());
        assertEquals(description, dto.getDescription());
        assertEquals(image, dto.getImage());
        assertEquals(birthDate, dto.getBirthDate());
        assertEquals(soloArtistId, dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_MinimalFields() {
        // Given
        String fullName = "Jane Smith";

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setFullName(fullName);

        // Then
        assertNull(dto.getMemberId());
        assertEquals(fullName, dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_ForCreateOperation() {
        // Given - typical create scenario (no memberId)
        String fullName = "Alice Johnson";
        String description = "Main rapper";
        LocalDate birthDate = LocalDate.of(1998, 7, 22);

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setFullName(fullName);
        dto.setDescription(description);
        dto.setBirthDate(birthDate);
        // memberId should remain null for create operations

        // Then
        assertNull(dto.getMemberId()); // Important: no ID for create
        assertEquals(fullName, dto.getFullName());
        assertEquals(description, dto.getDescription());
        assertEquals(birthDate, dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_ForUpdateOperation() {
        // Given - typical update scenario (with memberId)
        UUID memberId = UUID.randomUUID();
        String fullName = "Bob Wilson";
        String description = "Lead guitarist";
        String image = "http://example.com/bob-wilson.jpg";
        LocalDate birthDate = LocalDate.of(1992, 11, 8);

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setMemberId(memberId); // ID present for update
        dto.setFullName(fullName);
        dto.setDescription(description);
        dto.setImage(image);
        dto.setBirthDate(birthDate);

        // Then
        assertEquals(memberId, dto.getMemberId()); // Important: ID present for update
        assertEquals(fullName, dto.getFullName());
        assertEquals(description, dto.getDescription());
        assertEquals(image, dto.getImage());
        assertEquals(birthDate, dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }

    @Test
    void testMemberRequestDTO_WithSoloArtist() {
        // Given
        UUID soloArtistId = UUID.randomUUID();
        String fullName = "Charlie Brown";

        // When
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setFullName(fullName);
        dto.setSoloArtistId(soloArtistId);

        // Then
        assertEquals(fullName, dto.getFullName());
        assertEquals(soloArtistId, dto.getSoloArtistId());
        assertNull(dto.getMemberId());
    }

    @Test
    void testMemberRequestDTO_EmptyValues() {
        // When
        MemberRequestDTO dto = new MemberRequestDTO();

        // Then
        assertNull(dto.getMemberId());
        assertNull(dto.getFullName());
        assertNull(dto.getDescription());
        assertNull(dto.getImage());
        assertNull(dto.getBirthDate());
        assertNull(dto.getSoloArtistId());
    }
}
