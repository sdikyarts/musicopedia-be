package musicopedia.factory;

import musicopedia.builder.ArtistBuilder;
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
import java.util.concurrent.CompletableFuture;

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
        soloArtist = new ArtistBuilder()
            .setArtistName("Solo Artist")
            .setType(ArtistType.SOLO)
            .build();
        soloArtist.setArtistId(UUID.randomUUID());
        
        groupArtist = new ArtistBuilder()
            .setArtistName("Group Artist")
            .setType(ArtistType.GROUP)
            .build();
        groupArtist.setArtistId(UUID.randomUUID());
    }

    @Test
    void createMember_WithValidDataAndNoSoloArtist_ShouldCreateMember() {
        // When
        CompletableFuture<Member> resultFuture = memberFactory.createMember(validDto);
        Member result = resultFuture.join();

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
        when(artistService.findByIdAsync(soloArtistId)).thenReturn(CompletableFuture.completedFuture(Optional.of(soloArtist)));

        // When
        CompletableFuture<Member> resultFuture = memberFactory.createMember(validDto);
        Member result = resultFuture.join();

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("A talented member", result.getDescription());
        assertEquals("http://example.com/image.jpg", result.getImage());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
        assertNotNull(result.getSoloArtist());
        assertEquals(soloArtist, result.getSoloArtist());
        verify(artistService).findByIdAsync(soloArtistId);
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
        CompletableFuture<Member> resultFuture = memberFactory.createMember(validDto);
        Member result = resultFuture.join();

        // Then
        assertEquals("John Doe", result.getFullName());
    }

    @Test
    void createMember_WithNonExistentSoloArtist_ShouldThrowException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        validDto.setSoloArtistId(nonExistentId);
        when(artistService.findByIdAsync(nonExistentId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // When & Then
        CompletableFuture<Member> future = memberFactory.createMember(validDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            try {
                future.join();
            } catch (RuntimeException e) {
                throw e.getCause() instanceof IllegalArgumentException ? e.getCause() : e;
            }
        });
        
        assertTrue(exception instanceof IllegalArgumentException);
        assertEquals("Solo artist not found with ID: " + nonExistentId, exception.getMessage());
        verify(artistService).findByIdAsync(nonExistentId);
    }

    @Test
    void createMember_WithGroupArtistReference_ShouldThrowException() {
        // Given
        UUID groupArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(groupArtistId);
        when(artistService.findByIdAsync(groupArtistId)).thenReturn(CompletableFuture.completedFuture(Optional.of(groupArtist)));

        // When & Then
        CompletableFuture<Member> future = memberFactory.createMember(validDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            try {
                future.join();
            } catch (RuntimeException e) {
                throw e.getCause() instanceof IllegalArgumentException ? e.getCause() : e;
            }
        });
        
        assertTrue(exception instanceof IllegalArgumentException);
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s",
            "John Doe", "Group Artist", ArtistType.GROUP
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findByIdAsync(groupArtistId);
    }

    @Test
    void createMember_WithFranchiseArtistReference_ShouldThrowException() {
        // Given
        Artist franchiseArtist = new ArtistBuilder()
            .setArtistName("Franchise Artist")
            .setType(ArtistType.FRANCHISE)
            .build();
        
        UUID franchiseArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(franchiseArtistId);
        when(artistService.findByIdAsync(franchiseArtistId)).thenReturn(CompletableFuture.completedFuture(Optional.of(franchiseArtist)));

        // When & Then
        CompletableFuture<Member> future = memberFactory.createMember(validDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            try {
                future.join();
            } catch (RuntimeException e) {
                throw e.getCause() instanceof IllegalArgumentException ? e.getCause() : e;
            }
        });
        
        assertTrue(exception instanceof IllegalArgumentException);
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s",
            "John Doe", "Franchise Artist", ArtistType.FRANCHISE
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findByIdAsync(franchiseArtistId);
    }

    @Test
    void createMember_WithVariousArtistReference_ShouldThrowException() {
        // Given
        Artist variousArtist = new ArtistBuilder()
            .setArtistName("Various Artists")
            .setType(ArtistType.VARIOUS)
            .build();
        
        UUID variousArtistId = UUID.randomUUID();
        validDto.setSoloArtistId(variousArtistId);
        when(artistService.findByIdAsync(variousArtistId)).thenReturn(CompletableFuture.completedFuture(Optional.of(variousArtist)));

        // When & Then
        CompletableFuture<Member> future = memberFactory.createMember(validDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            try {
                future.join();
            } catch (RuntimeException e) {
                throw e.getCause() instanceof IllegalArgumentException ? e.getCause() : e;
            }
        });
        
        assertTrue(exception instanceof IllegalArgumentException);
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s",
            "John Doe", "Various Artists", ArtistType.VARIOUS
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findByIdAsync(variousArtistId);
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
        
        Artist franchiseArtist = new ArtistBuilder()
            .setArtistName("Franchise Artist")
            .setType(ArtistType.FRANCHISE)
            .build();

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
        
        Artist variousArtist = new ArtistBuilder()
            .setArtistName("Various Artists")
            .setType(ArtistType.VARIOUS)
            .build();

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
        CompletableFuture<Member> resultFuture = memberFactory.createMember(minimalDto);
        Member result = resultFuture.join();

        // Then
        assertNotNull(result);
        assertEquals("Jane Doe", result.getFullName());
        assertNull(result.getDescription());
        assertNull(result.getImage());
        assertNull(result.getBirthDate());
        assertNull(result.getSoloArtist());
    }
}
