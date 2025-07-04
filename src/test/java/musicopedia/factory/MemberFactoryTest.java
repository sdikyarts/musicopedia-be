package musicopedia.factory;

import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberFactoryTest {

    @Mock
    private ArtistService artistService;

    private MemberFactory memberFactory;

    private MemberRequestDTO validDto;
    private Artist soloArtist;
    private Artist groupArtist;

    @BeforeEach
    void setUp() {
        memberFactory = new MemberFactory(artistService);
        
        // Setup valid DTO
        validDto = new MemberRequestDTO();
        validDto.setFullName("John Doe");
        validDto.setDescription("A talented member");
        validDto.setImage("http://example.com/image.jpg");
        validDto.setBirthDate(LocalDate.of(1990, 1, 1));
        
        // Setup test artists
        soloArtist = new Artist();
        soloArtist.setArtistId(UUID.randomUUID());
        soloArtist.setArtistName("Solo Artist");
        soloArtist.setType(ArtistType.SOLO);
        
        groupArtist = new Artist();
        groupArtist.setArtistId(UUID.randomUUID());
        groupArtist.setArtistName("Group Artist");
        groupArtist.setType(ArtistType.GROUP);
    }

    @Test
    void createMember_WithValidDataAndNoSoloArtist_ShouldCreateMember() {
        // When
        Member result = memberFactory.createMember(validDto);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("A talented member", result.getDescription());
        assertEquals("http://example.com/image.jpg", result.getImage());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
        assertNull(result.getSoloArtist());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithValidDataAndSoloArtist_ShouldCreateMemberWithSoloArtist() {
        // Given
        UUID soloArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(soloArtistId);
        when(artistService.findById(soloArtistId)).thenReturn(Optional.of(soloArtist));

        // When
        Member result = memberFactory.createMember(validDto);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("A talented member", result.getDescription());
        assertEquals("http://example.com/image.jpg", result.getImage());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
        assertNotNull(result.getSoloArtist());
        assertEquals(soloArtist, result.getSoloArtist());
        verify(artistService).findById(soloArtistId);
    }

    @Test
    void createMember_WithNullFullName_ShouldThrowException() {
        // Given
        validDto.setFullName(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member full name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithEmptyFullName_ShouldThrowException() {
        // Given
        validDto.setFullName("   ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member full name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithBlankFullName_ShouldThrowException() {
        // Given
        validDto.setFullName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member full name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithWhitespaceInFullName_ShouldTrimName() {
        // Given
        validDto.setFullName("  John Doe  ");

        // When
        Member result = memberFactory.createMember(validDto);

        // Then
        assertEquals("John Doe", result.getFullName());
    }

    @Test
    void createMember_WithNonExistentSoloArtist_ShouldThrowException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        validDto.setSoloArtistId(nonExistentId);
        when(artistService.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Solo artist not found with ID: " + nonExistentId, exception.getMessage());
        verify(artistService).findById(nonExistentId);
    }

    @Test
    void createMember_WithGroupArtistReference_ShouldThrowException() {
        // Given
        UUID groupArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(groupArtistId);
        when(artistService.findById(groupArtistId)).thenReturn(Optional.of(groupArtist));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s",
            "John Doe", "Group Artist", ArtistType.GROUP
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findById(groupArtistId);
    }

    @Test
    void createMember_WithFranchiseArtistReference_ShouldThrowException() {
        // Given
        Artist franchiseArtist = new Artist();
        franchiseArtist.setArtistId(UUID.randomUUID());
        franchiseArtist.setArtistName("Franchise Artist");
        franchiseArtist.setType(ArtistType.FRANCHISE);
        
        UUID franchiseArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(franchiseArtistId);
        when(artistService.findById(franchiseArtistId)).thenReturn(Optional.of(franchiseArtist));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s",
            "John Doe", "Franchise Artist", ArtistType.FRANCHISE
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findById(franchiseArtistId);
    }

    @Test
    void createMember_WithVariousArtistReference_ShouldThrowException() {
        // Given
        Artist variousArtist = new Artist();
        variousArtist.setArtistId(UUID.randomUUID());
        variousArtist.setArtistName("Various Artists");
        variousArtist.setType(ArtistType.VARIOUS);
        
        UUID variousArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(variousArtistId);
        when(artistService.findById(variousArtistId)).thenReturn(Optional.of(variousArtist));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s",
            "John Doe", "Various Artists", ArtistType.VARIOUS
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findById(variousArtistId);
    }

    @Test
    void linkToSoloArtist_WithValidSoloArtist_ShouldLinkSuccessfully() {
        // Given
        Member member = new Member();
        member.setFullName("Test Member");

        // When
        memberFactory.linkToSoloArtist(member, soloArtist);

        // Then
        assertEquals(soloArtist, member.getSoloArtist());
    }

    @Test
    void linkToSoloArtist_WithGroupArtist_ShouldThrowException() {
        // Given
        Member member = new Member();
        member.setFullName("Test Member");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.linkToSoloArtist(member, groupArtist);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members",
            "Test Member", "Group Artist"
        );
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(member.getSoloArtist());
    }

    @Test
    void linkToSoloArtist_WithFranchiseArtist_ShouldThrowException() {
        // Given
        Member member = new Member();
        member.setFullName("Test Member");
        
        Artist franchiseArtist = new Artist();
        franchiseArtist.setArtistName("Franchise Artist");
        franchiseArtist.setType(ArtistType.FRANCHISE);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.linkToSoloArtist(member, franchiseArtist);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members",
            "Test Member", "Franchise Artist"
        );
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(member.getSoloArtist());
    }

    @Test
    void linkToSoloArtist_WithVariousArtist_ShouldThrowException() {
        // Given
        Member member = new Member();
        member.setFullName("Test Member");
        
        Artist variousArtist = new Artist();
        variousArtist.setArtistName("Various Artists");
        variousArtist.setType(ArtistType.VARIOUS);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.linkToSoloArtist(member, variousArtist);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members",
            "Test Member", "Various Artists"
        );
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(member.getSoloArtist());
    }

    @Test
    void unlinkFromSoloArtist_WithLinkedMember_ShouldRemoveLink() {
        // Given
        Member member = new Member();
        member.setFullName("Test Member");
        member.setSoloArtist(soloArtist);
        assertNotNull(member.getSoloArtist()); // Verify it's linked initially

        // When
        memberFactory.unlinkFromSoloArtist(member);

        // Then
        assertNull(member.getSoloArtist());
    }

    @Test
    void unlinkFromSoloArtist_WithUnlinkedMember_ShouldDoNothing() {
        // Given
        Member member = new Member();
        member.setFullName("Test Member");
        assertNull(member.getSoloArtist()); // Verify it's not linked initially

        // When
        memberFactory.unlinkFromSoloArtist(member);

        // Then
        assertNull(member.getSoloArtist());
    }

    @Test
    void createMember_WithMinimalValidData_ShouldCreateMember() {
        // Given
        MemberRequestDTO minimalDto = new MemberRequestDTO();
        minimalDto.setFullName("Jane Doe");
        // All other fields are null

        // When
        Member result = memberFactory.createMember(minimalDto);

        // Then
        assertNotNull(result);
        assertEquals("Jane Doe", result.getFullName());
        assertNull(result.getDescription());
        assertNull(result.getImage());
        assertNull(result.getBirthDate());
        assertNull(result.getSoloArtist());
    }
}
