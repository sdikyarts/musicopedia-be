package musicopedia.mapper;

import musicopedia.builder.ArtistBuilder;
import musicopedia.builder.MemberBuilder;
import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;

import musicopedia.factory.MemberFactory;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberMapperTest {

    @Mock
    private ArtistService artistService;

    @Mock
    private MemberFactory memberFactory;

    private MemberMapper memberMapper;

    private Member testMember;
    private Artist soloArtist;
    private Artist groupArtist;
    private MemberRequestDTO createDto;
    private MemberRequestDTO updateDto;

    @BeforeEach
    void setUp() {
        memberMapper = new MemberMapper(artistService, memberFactory);

        // Setup test member
        testMember = new MemberBuilder()
            .setFullName("Test Member")
            .setDescription("Test description")
            .setImage("test-image.jpg")
            .setBirthDate(LocalDate.of(1990, 1, 1))
            .build();
        testMember.setMemberId(UUID.randomUUID());

        // Setup solo artist
        soloArtist = new ArtistBuilder()
            .setArtistName("Solo Artist")
            .setType(ArtistType.SOLO)
            .build();
        soloArtist.setArtistId(UUID.randomUUID());
        // Setup group artist
        groupArtist = new ArtistBuilder()
            .setArtistName("Group Artist")
            .setType(ArtistType.GROUP)
            .build();
        groupArtist.setArtistId(UUID.randomUUID());

        // Setup DTOs
        createDto = new MemberRequestDTO();
        createDto.setFullName("New Member");
        createDto.setDescription("New description");
        createDto.setImage("new-image.jpg");
        createDto.setBirthDate(LocalDate.of(1995, 5, 15));

        updateDto = new MemberRequestDTO();
        updateDto.setFullName("Updated Member");
        updateDto.setDescription("Updated description");
        updateDto.setImage("updated-image.jpg");
        updateDto.setBirthDate(LocalDate.of(1992, 3, 10));
    }

    @Test
    void toEntity_ShouldDelegateToMemberFactory() {
        // Given
        Member expectedMember = new MemberBuilder()
            .setFullName("Expected Member")
            .build();
        when(memberFactory.createMember(createDto)).thenReturn(CompletableFuture.completedFuture(expectedMember));

        // When
        CompletableFuture<Member> resultFuture = memberMapper.toEntity(createDto);
        Member result = resultFuture.join();

        // Then
        assertEquals(expectedMember, result);
        verify(memberFactory).createMember(createDto);
    }

    @Test
    void updateEntityFromDto_WithAllFields_ShouldUpdateAllFields() {
        // Given
        updateDto.setSoloArtistId(soloArtist.getArtistId());
        when(artistService.findByIdAsync(soloArtist.getArtistId())).thenReturn(CompletableFuture.completedFuture(Optional.of(soloArtist)));

        // When
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        future.join(); // Wait for the async operation to complete

        // Then
        assertEquals("Updated Member", testMember.getFullName());
        assertEquals("Updated description", testMember.getDescription());
        assertEquals("updated-image.jpg", testMember.getImage());
        assertEquals(LocalDate.of(1992, 3, 10), testMember.getBirthDate());
        assertEquals(soloArtist, testMember.getSoloArtist());
        verify(artistService).findByIdAsync(soloArtist.getArtistId());
    }

    @Test
    void updateEntityFromDto_WithNullFullName_ShouldNotUpdateFullName() {
        // Given
        String originalFullName = testMember.getFullName();
        updateDto.setFullName(null);

        // When
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        future.join(); // Wait for the async operation to complete

        // Then
        assertEquals(originalFullName, testMember.getFullName());
    }

    @Test
    void updateEntityFromDto_WithNullDescription_ShouldNotUpdateDescription() {
        // Given
        String originalDescription = testMember.getDescription();
        updateDto.setDescription(null);

        // When
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        future.join(); // Wait for the async operation to complete

        // Then
        assertEquals(originalDescription, testMember.getDescription());
    }

    @Test
    void updateEntityFromDto_WithNullImage_ShouldNotUpdateImage() {
        // Given
        String originalImage = testMember.getImage();
        updateDto.setImage(null);

        // When
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        future.join(); // Wait for the async operation to complete

        // Then
        assertEquals(originalImage, testMember.getImage());
    }

    @Test
    void updateEntityFromDto_WithNullBirthDate_ShouldNotUpdateBirthDate() {
        // Given
        LocalDate originalBirthDate = testMember.getBirthDate();
        updateDto.setBirthDate(null);

        // When
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        future.join(); // Wait for the async operation to complete

        // Then
        assertEquals(originalBirthDate, testMember.getBirthDate());
    }

    @Test
    void updateEntityFromDto_WithNullSoloArtistId_ShouldRemoveSoloArtistReference() {
        // Given
        testMember.setSoloArtist(soloArtist); // Set initial solo artist
        updateDto.setSoloArtistId(null);

        // When
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        future.join(); // Wait for the async operation to complete

        // Then
        assertNull(testMember.getSoloArtist());
        verifyNoInteractions(artistService);
    }

    @Test
    void updateEntityFromDto_WithNonExistentSoloArtistId_ShouldThrowException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        updateDto.setSoloArtistId(nonExistentId);
        when(artistService.findByIdAsync(nonExistentId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // When & Then
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            future.join(); // This will unwrap the CompletableFuture's exception
        });
        
        // The real exception is wrapped as the cause of the CompletionException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Solo artist not found with ID: " + nonExistentId, exception.getCause().getMessage());
        verify(artistService).findByIdAsync(nonExistentId);
    }

    @Test
    void updateEntityFromDto_WithNonSoloArtist_ShouldThrowException() {
        // Given
        updateDto.setSoloArtistId(groupArtist.getArtistId());
        when(artistService.findByIdAsync(groupArtist.getArtistId())).thenReturn(CompletableFuture.completedFuture(Optional.of(groupArtist)));

        // When & Then
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            future.join(); // This will unwrap the CompletableFuture's exception
        });
        
        // The real exception is wrapped as the cause of the CompletionException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Referenced artist must be of type SOLO, but was: " + ArtistType.GROUP, exception.getCause().getMessage());
        verify(artistService).findByIdAsync(groupArtist.getArtistId());
    }

    @Test
    void updateEntityFromDto_WithFranchiseArtist_ShouldThrowException() {
        // Given
        Artist franchiseArtist = new Artist();
        franchiseArtist.setArtistId(UUID.randomUUID());
        franchiseArtist.setType(ArtistType.FRANCHISE);
        
        updateDto.setSoloArtistId(franchiseArtist.getArtistId());
        when(artistService.findByIdAsync(franchiseArtist.getArtistId())).thenReturn(CompletableFuture.completedFuture(Optional.of(franchiseArtist)));

        // When & Then
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            future.join(); // This will unwrap the CompletableFuture's exception
        });
        
        // The real exception is wrapped as the cause of the CompletionException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Referenced artist must be of type SOLO, but was: " + ArtistType.FRANCHISE, exception.getCause().getMessage());
        verify(artistService).findByIdAsync(franchiseArtist.getArtistId());
    }

    @Test
    void updateEntityFromDto_WithVariousArtist_ShouldThrowException() {
        // Given
        Artist variousArtist = new Artist();
        variousArtist.setArtistId(UUID.randomUUID());
        variousArtist.setType(ArtistType.VARIOUS);
        
        updateDto.setSoloArtistId(variousArtist.getArtistId());
        when(artistService.findByIdAsync(variousArtist.getArtistId())).thenReturn(CompletableFuture.completedFuture(Optional.of(variousArtist)));

        // When & Then
        CompletableFuture<Void> future = memberMapper.updateEntityFromDto(testMember, updateDto);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            future.join(); // This will unwrap the CompletableFuture's exception
        });
        
        // The real exception is wrapped as the cause of the CompletionException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Referenced artist must be of type SOLO, but was: " + ArtistType.VARIOUS, exception.getCause().getMessage());
        verify(artistService).findByIdAsync(variousArtist.getArtistId());
    }

    @Test
    void toResponseDTO_WithoutSoloArtist_ShouldMapCorrectly() {
        // When
        MemberResponseDTO result = memberMapper.toResponseDTO(testMember);

        // Then
        assertNotNull(result);
        assertEquals(testMember.getMemberId(), result.getMemberId());
        assertEquals(testMember.getFullName(), result.getFullName());
        assertEquals(testMember.getDescription(), result.getDescription());
        assertEquals(testMember.getImage(), result.getImage());
        assertEquals(testMember.getBirthDate(), result.getBirthDate());
        assertNull(result.getSoloArtistId());
        assertNull(result.getSoloArtistName());
        assertFalse(result.getHasOfficialSoloDebut());
    }

    @Test
    void toResponseDTO_WithSoloArtist_ShouldMapCorrectly() {
        // Given
        testMember.setSoloArtist(soloArtist);

        // When
        MemberResponseDTO result = memberMapper.toResponseDTO(testMember);

        // Then
        assertNotNull(result);
        assertEquals(testMember.getMemberId(), result.getMemberId());
        assertEquals(testMember.getFullName(), result.getFullName());
        assertEquals(testMember.getDescription(), result.getDescription());
        assertEquals(testMember.getImage(), result.getImage());
        assertEquals(testMember.getBirthDate(), result.getBirthDate());
        assertEquals(soloArtist.getArtistId(), result.getSoloArtistId());
        assertEquals(soloArtist.getArtistName(), result.getSoloArtistName());
        assertTrue(result.getHasOfficialSoloDebut());
    }

    @Test
    void toSummaryDTO_WithoutSoloArtist_ShouldMapCorrectly() {
        // When
        MemberResponseDTO result = memberMapper.toSummaryDTO(testMember);

        // Then
        assertNotNull(result);
        assertEquals(testMember.getMemberId(), result.getMemberId());
        assertEquals(testMember.getFullName(), result.getFullName());
        assertEquals(testMember.getImage(), result.getImage());
        assertFalse(result.getHasOfficialSoloDebut());
        assertNull(result.getSoloArtistName());
    }

    @Test
    void toSummaryDTO_WithSoloArtist_ShouldMapCorrectly() {
        // Given
        testMember.setSoloArtist(soloArtist);

        // When
        MemberResponseDTO result = memberMapper.toSummaryDTO(testMember);

        // Then
        assertNotNull(result);
        assertEquals(testMember.getMemberId(), result.getMemberId());
        assertEquals(testMember.getFullName(), result.getFullName());
        assertEquals(testMember.getImage(), result.getImage());
        assertTrue(result.getHasOfficialSoloDebut());
        assertEquals(soloArtist.getArtistName(), result.getSoloArtistName());
    }

    @Test
    void toResponseDTOList_WithEmptyList_ShouldReturnEmptyList() {
        // Given
        List<Member> emptyList = List.of();

        // When
        List<MemberResponseDTO> result = memberMapper.toResponseDTOList(emptyList);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseDTOList_WithMultipleMembers_ShouldMapAll() {
        // Given
        Member member1 = new Member();
        member1.setMemberId(UUID.randomUUID());
        member1.setFullName("Member 1");
        member1.setSoloArtist(soloArtist);

        Member member2 = new Member();
        member2.setMemberId(UUID.randomUUID());
        member2.setFullName("Member 2");
        // No solo artist

        List<Member> members = Arrays.asList(member1, member2);

        // When
        List<MemberResponseDTO> result = memberMapper.toResponseDTOList(members);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        MemberResponseDTO dto1 = result.get(0);
        assertEquals(member1.getMemberId(), dto1.getMemberId());
        assertEquals("Member 1", dto1.getFullName());
        assertTrue(dto1.getHasOfficialSoloDebut());
        assertEquals(soloArtist.getArtistName(), dto1.getSoloArtistName());

        MemberResponseDTO dto2 = result.get(1);
        assertEquals(member2.getMemberId(), dto2.getMemberId());
        assertEquals("Member 2", dto2.getFullName());
        assertFalse(dto2.getHasOfficialSoloDebut());
        assertNull(dto2.getSoloArtistName());
    }

    @Test
    void toSummaryDTOList_WithEmptyList_ShouldReturnEmptyList() {
        // Given
        List<Member> emptyList = List.of();

        // When
        List<MemberResponseDTO> result = memberMapper.toSummaryDTOList(emptyList);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toSummaryDTOList_WithMultipleMembers_ShouldMapAll() {
        // Given
        Member member1 = new Member();
        member1.setMemberId(UUID.randomUUID());
        member1.setFullName("Member 1");
        member1.setImage("image1.jpg");
        member1.setSoloArtist(soloArtist);

        Member member2 = new Member();
        member2.setMemberId(UUID.randomUUID());
        member2.setFullName("Member 2");
        member2.setImage("image2.jpg");
        // No solo artist

        List<Member> members = Arrays.asList(member1, member2);

        // When
        List<MemberResponseDTO> result = memberMapper.toSummaryDTOList(members);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        MemberResponseDTO dto1 = result.get(0);
        assertEquals(member1.getMemberId(), dto1.getMemberId());
        assertEquals("Member 1", dto1.getFullName());
        assertEquals("image1.jpg", dto1.getImage());
        assertTrue(dto1.getHasOfficialSoloDebut());
        assertEquals(soloArtist.getArtistName(), dto1.getSoloArtistName());

        MemberResponseDTO dto2 = result.get(1);
        assertEquals(member2.getMemberId(), dto2.getMemberId());
        assertEquals("Member 2", dto2.getFullName());
        assertEquals("image2.jpg", dto2.getImage());
        assertFalse(dto2.getHasOfficialSoloDebut());
        assertNull(dto2.getSoloArtistName());
    }

    @Test
    void toSummaryDTOList_WithSingleMember_ShouldMapCorrectly() {
        // Given
        List<Member> singleMemberList = List.of(testMember);

        // When
        List<MemberResponseDTO> result = memberMapper.toSummaryDTOList(singleMemberList);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        MemberResponseDTO dto = result.get(0);
        assertEquals(testMember.getMemberId(), dto.getMemberId());
        assertEquals(testMember.getFullName(), dto.getFullName());
        assertEquals(testMember.getImage(), dto.getImage());
        assertFalse(dto.getHasOfficialSoloDebut());
    }

    @Test
    void toResponseDTOList_WithSingleMember_ShouldMapCorrectly() {
        // Given
        List<Member> singleMemberList = List.of(testMember);

        // When
        List<MemberResponseDTO> result = memberMapper.toResponseDTOList(singleMemberList);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        MemberResponseDTO dto = result.get(0);
        assertEquals(testMember.getMemberId(), dto.getMemberId());
        assertEquals(testMember.getFullName(), dto.getFullName());
        assertEquals(testMember.getDescription(), dto.getDescription());
        assertEquals(testMember.getImage(), dto.getImage());
        assertEquals(testMember.getBirthDate(), dto.getBirthDate());
        assertFalse(dto.getHasOfficialSoloDebut());
    }

    @Test
    void createMemberFromDto_ShouldMapAllFieldsCorrectly() {
        // Given
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setFullName("Jane Doe");
        dto.setDescription("A test member");
        dto.setImage("jane.jpg");
        dto.setBirthDate(LocalDate.of(1992, 2, 2));
        Artist soloArtist = new ArtistBuilder()
            .setArtistName("Solo Jane")
            .setType(ArtistType.SOLO)
            .build();
        soloArtist.setArtistId(UUID.randomUUID());

        // When
        Member member = memberMapper.createMemberFromDto(dto, soloArtist);

        // Then
        assertNotNull(member);
        assertEquals("Jane Doe", member.getFullName());
        assertEquals("A test member", member.getDescription());
        assertEquals("jane.jpg", member.getImage());
        assertEquals(LocalDate.of(1992, 2, 2), member.getBirthDate());
        assertEquals(soloArtist, member.getSoloArtist());
    }
}
