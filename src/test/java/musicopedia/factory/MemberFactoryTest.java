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
        validDto.setMemberName("Felix");
        validDto.setRealName("Felix Yongbok Lee");
        validDto.setDescription("Lead dancer and rapper of Stray Kids");
        validDto.setImage("http://example.com/felix.jpg");
        validDto.setBirthDate(LocalDate.of(2000, 9, 15));
        // Setup test artists
        soloArtist = new ArtistBuilder()
            .setArtistName("Jung Kook")
            .setType(ArtistType.SOLO)
            .build();
        soloArtist.setArtistId(UUID.randomUUID());
        // Use real-life group: Stray Kids for Felix
        groupArtist = new ArtistBuilder()
            .setArtistName("Stray Kids")
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
        assertEquals("Felix", result.getMemberName());
        assertEquals("Felix Yongbok Lee", result.getRealName());
        assertEquals("Lead dancer and rapper of Stray Kids", result.getDescription());
        assertEquals("http://example.com/felix.jpg", result.getImage());
        assertEquals(LocalDate.of(2000, 9, 15), result.getBirthDate());
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
        assertEquals("Felix", result.getMemberName());
        assertEquals("Felix Yongbok Lee", result.getRealName());
        assertEquals("Lead dancer and rapper of Stray Kids", result.getDescription());
        assertEquals("http://example.com/felix.jpg", result.getImage());
        assertEquals(LocalDate.of(2000, 9, 15), result.getBirthDate());
        assertNotNull(result.getSoloArtist());
        assertEquals(soloArtist, result.getSoloArtist());
        verify(artistService).findByIdAsync(soloArtistId);
    }

    @Test
    void createMember_WithNullMemberName_ShouldThrowException() {
        // Given
        validDto.setMemberName(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithEmptyMemberName_ShouldThrowException() {
        // Given
        validDto.setMemberName("   ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithBlankMemberName_ShouldThrowException() {
        // Given
        validDto.setMemberName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithWhitespaceInMemberName_ShouldTrimName() {
        // Given
        validDto.setMemberName("  HAN  ");
        validDto.setRealName("Han Ji-sung");
        // When
        CompletableFuture<Member> resultFuture = memberFactory.createMember(validDto);
        Member result = resultFuture.join();
        // Then
        assertEquals("HAN", result.getMemberName());
        assertEquals("Han Ji-sung", result.getRealName());
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
            "Felix", "Stray Kids", ArtistType.GROUP
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
            "Felix", "Franchise Artist", ArtistType.FRANCHISE
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
            "Felix", "Various Artists", ArtistType.VARIOUS
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(artistService).findByIdAsync(variousArtistId);
    }

    @Test
    void linkToSoloArtist_WithValidSoloArtist_ShouldLinkSuccessfully() {
        // Given
        Member member = new Member();
        member.setMemberName("Test Member");

        // When
        memberFactory.linkToSoloArtist(member, soloArtist);

        // Then
        assertEquals(soloArtist, member.getSoloArtist());
    }

    @Test
    void linkToSoloArtist_WithGroupArtist_ShouldThrowException() {
        // Given
        Member member = new Member();
        member.setMemberName("Test Member");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.linkToSoloArtist(member, groupArtist);
        });
        String expectedMessage = String.format(
            "Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members",
            "Test Member", "Stray Kids"
        );
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(member.getSoloArtist());
    }

    @Test
    void linkToSoloArtist_WithFranchiseArtist_ShouldThrowException() {
        // Given
        Member member = new Member();
        member.setMemberName("Test Member");
        
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
        member.setMemberName("Test Member");
        
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
        member.setMemberName("Test Member");
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
        member.setMemberName("Test Member");
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
        minimalDto.setMemberName("Jane Doe");
        minimalDto.setRealName("Kim Jisoo");
        // All other fields are null

        // When
        CompletableFuture<Member> resultFuture = memberFactory.createMember(minimalDto);
        Member result = resultFuture.join();

        // Then
        assertNotNull(result);
        assertEquals("Jane Doe", result.getMemberName());
        assertEquals("Kim Jisoo", result.getRealName());
        assertNull(result.getDescription());
        assertNull(result.getImage());
        assertNull(result.getBirthDate());
        assertNull(result.getSoloArtist());
    }

    @Test
    void createMember_WithNullRealName_ShouldThrowException() {
        // Given
        validDto.setRealName(null);
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member real name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithEmptyRealName_ShouldThrowException() {
        // Given
        validDto.setRealName("");
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member real name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }

    @Test
    void createMember_WithBlankRealName_ShouldThrowException() {
        // Given
        validDto.setRealName("   ");
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.createMember(validDto);
        });
        assertEquals("Member real name is required", exception.getMessage());
        verifyNoInteractions(artistService);
    }
}
