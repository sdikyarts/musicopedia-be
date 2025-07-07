package musicopedia.factory;

import musicopedia.builder.ArtistBuilder;
import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.Solo;
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
        assertTrue(result.getSoloIdentities() == null || result.getSoloIdentities().isEmpty());
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
        assertFalse(result.getSoloIdentities().isEmpty());
        assertEquals(soloArtist, result.getSoloIdentities().get(0).getArtist());
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
        assertTrue(result.getSoloIdentities() == null || result.getSoloIdentities().isEmpty());
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

    @Test
    void linkToSoloIdentity_WithNullSolo_ShouldThrowException() {
        Member member = new Member();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            memberFactory.linkToSoloIdentity(member, null);
        });
        assertEquals("Solo identity cannot be null", ex.getMessage());
    }

    @Test
    void linkToSoloIdentity_WhenSoloIdentitiesIsNull_ShouldInitializeAndAdd() {
        Member member = new Member();
        member.setSoloIdentities(null);
        Solo solo = new Solo();
        memberFactory.linkToSoloIdentity(member, solo);
        assertNotNull(member.getSoloIdentities());
        assertTrue(member.getSoloIdentities().contains(solo));
        assertEquals(member, solo.getMember());
    }

    @Test
    void linkToSoloIdentity_WhenSoloAlreadyPresent_ShouldNotAddAgain() {
        Member member = new Member();
        Solo solo = new Solo();
        java.util.List<Solo> solos = new java.util.ArrayList<>();
        solos.add(solo);
        member.setSoloIdentities(solos);
        memberFactory.linkToSoloIdentity(member, solo);
        assertEquals(1, member.getSoloIdentities().size());
        assertEquals(member, solo.getMember());
    }

    @Test
    void linkToSoloIdentity_WhenSoloNotPresent_ShouldAdd() {
        Member member = new Member();
        Solo solo1 = new Solo();
        Solo solo2 = new Solo();
        java.util.List<Solo> solos = new java.util.ArrayList<>();
        solos.add(solo1);
        member.setSoloIdentities(solos);
        memberFactory.linkToSoloIdentity(member, solo2);
        assertTrue(member.getSoloIdentities().contains(solo2));
        assertEquals(member, solo2.getMember());
    }

    @Test
    void unlinkFromSoloIdentity_WhenSoloIdentitiesIsNull_ShouldNotThrow() {
        Member member = new Member();
        member.setSoloIdentities(null);
        Solo solo = new Solo();
        assertDoesNotThrow(() -> memberFactory.unlinkFromSoloIdentity(member, solo));
        assertNull(solo.getMember());
    }

    @Test
    void unlinkFromSoloIdentity_WhenSoloPresent_ShouldRemove() {
        Member member = new Member();
        Solo solo = new Solo();
        java.util.List<Solo> solos = new java.util.ArrayList<>();
        solos.add(solo);
        member.setSoloIdentities(solos);
        memberFactory.unlinkFromSoloIdentity(member, solo);
        assertFalse(member.getSoloIdentities().contains(solo));
        assertNull(solo.getMember());
    }

    @Test
    void unlinkFromSoloIdentity_WhenSoloNotPresent_ShouldNotThrow() {
        Member member = new Member();
        Solo solo1 = new Solo();
        solo1.setArtistId(UUID.randomUUID());
        Solo solo2 = new Solo();
        solo2.setArtistId(UUID.randomUUID()); // ensure different artistId
        java.util.List<Solo> solos = new java.util.ArrayList<>();
        solos.add(solo1);
        member.setSoloIdentities(solos);
        assertDoesNotThrow(() -> memberFactory.unlinkFromSoloIdentity(member, solo2));
        assertTrue(member.getSoloIdentities().contains(solo1));
    }
}
