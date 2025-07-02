package musicopedia.mapper;

import musicopedia.dto.request.CreateMemberRequestDTO;
import musicopedia.dto.request.UpdateMemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;
import musicopedia.dto.response.MemberSummaryDTO;
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
    private CreateMemberRequestDTO createDto;
    private UpdateMemberRequestDTO updateDto;

    @BeforeEach
    void setUp() {
        memberMapper = new MemberMapper(artistService, memberFactory);

        // Setup test member
        testMember = new Member();
        testMember.setMemberId(UUID.randomUUID());
        testMember.setFullName("Test Member");
        testMember.setDescription("Test description");
        testMember.setImage("test-image.jpg");
        testMember.setBirthDate(LocalDate.of(1990, 1, 1));

        // Setup solo artist
        soloArtist = new Artist();
        soloArtist.setArtistId(UUID.randomUUID());
        soloArtist.setArtistName("Solo Artist");
        soloArtist.setType(ArtistType.SOLO);

        // Setup group artist
        groupArtist = new Artist();
        groupArtist.setArtistId(UUID.randomUUID());
        groupArtist.setArtistName("Group Artist");
        groupArtist.setType(ArtistType.GROUP);

        // Setup DTOs
        createDto = new CreateMemberRequestDTO();
        createDto.setFullName("New Member");
        createDto.setDescription("New description");
        createDto.setImage("new-image.jpg");
        createDto.setBirthDate(LocalDate.of(1995, 5, 15));

        updateDto = new UpdateMemberRequestDTO();
        updateDto.setFullName("Updated Member");
        updateDto.setDescription("Updated description");
        updateDto.setImage("updated-image.jpg");
        updateDto.setBirthDate(LocalDate.of(1992, 3, 10));
    }

    @Test
    void toEntity_ShouldDelegateToMemberFactory() {
        // Given
        Member expectedMember = new Member();
        when(memberFactory.createMember(createDto)).thenReturn(expectedMember);

        // When
        Member result = memberMapper.toEntity(createDto);

        // Then
        assertEquals(expectedMember, result);
        verify(memberFactory).createMember(createDto);
    }

    @Test
    void updateEntityFromDto_WithAllFields_ShouldUpdateAllFields() {
        // Given
        updateDto.setSoloArtistId(soloArtist.getArtistId());
        when(artistService.findById(soloArtist.getArtistId())).thenReturn(Optional.of(soloArtist));

        // When
        memberMapper.updateEntityFromDto(testMember, updateDto);

        // Then
        assertEquals("Updated Member", testMember.getFullName());
        assertEquals("Updated description", testMember.getDescription());
        assertEquals("updated-image.jpg", testMember.getImage());
        assertEquals(LocalDate.of(1992, 3, 10), testMember.getBirthDate());
        assertEquals(soloArtist, testMember.getSoloArtist());
        verify(artistService).findById(soloArtist.getArtistId());
    }

    @Test
    void updateEntityFromDto_WithNullFullName_ShouldNotUpdateFullName() {
        // Given
        String originalFullName = testMember.getFullName();
        updateDto.setFullName(null);

        // When
        memberMapper.updateEntityFromDto(testMember, updateDto);

        // Then
        assertEquals(originalFullName, testMember.getFullName());
    }

    @Test
    void updateEntityFromDto_WithNullDescription_ShouldNotUpdateDescription() {
        // Given
        String originalDescription = testMember.getDescription();
        updateDto.setDescription(null);

        // When
        memberMapper.updateEntityFromDto(testMember, updateDto);

        // Then
        assertEquals(originalDescription, testMember.getDescription());
    }

    @Test
    void updateEntityFromDto_WithNullImage_ShouldNotUpdateImage() {
        // Given
        String originalImage = testMember.getImage();
        updateDto.setImage(null);

        // When
        memberMapper.updateEntityFromDto(testMember, updateDto);

        // Then
        assertEquals(originalImage, testMember.getImage());
    }

    @Test
    void updateEntityFromDto_WithNullBirthDate_ShouldNotUpdateBirthDate() {
        // Given
        LocalDate originalBirthDate = testMember.getBirthDate();
        updateDto.setBirthDate(null);

        // When
        memberMapper.updateEntityFromDto(testMember, updateDto);

        // Then
        assertEquals(originalBirthDate, testMember.getBirthDate());
    }

    @Test
    void updateEntityFromDto_WithNullSoloArtistId_ShouldRemoveSoloArtistReference() {
        // Given
        testMember.setSoloArtist(soloArtist); // Set initial solo artist
        updateDto.setSoloArtistId(null);

        // When
        memberMapper.updateEntityFromDto(testMember, updateDto);

        // Then
        assertNull(testMember.getSoloArtist());
        verifyNoInteractions(artistService);
    }

    @Test
    void updateEntityFromDto_WithNonExistentSoloArtistId_ShouldThrowException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        updateDto.setSoloArtistId(nonExistentId);
        when(artistService.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberMapper.updateEntityFromDto(testMember, updateDto);
        });
        assertEquals("Solo artist not found with ID: " + nonExistentId, exception.getMessage());
        verify(artistService).findById(nonExistentId);
    }

    @Test
    void updateEntityFromDto_WithNonSoloArtist_ShouldThrowException() {
        // Given
        updateDto.setSoloArtistId(groupArtist.getArtistId());
        when(artistService.findById(groupArtist.getArtistId())).thenReturn(Optional.of(groupArtist));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberMapper.updateEntityFromDto(testMember, updateDto);
        });
        assertEquals("Referenced artist must be of type SOLO, but was: " + ArtistType.GROUP, exception.getMessage());
        verify(artistService).findById(groupArtist.getArtistId());
    }

    @Test
    void updateEntityFromDto_WithFranchiseArtist_ShouldThrowException() {
        // Given
        Artist franchiseArtist = new Artist();
        franchiseArtist.setArtistId(UUID.randomUUID());
        franchiseArtist.setType(ArtistType.FRANCHISE);
        
        updateDto.setSoloArtistId(franchiseArtist.getArtistId());
        when(artistService.findById(franchiseArtist.getArtistId())).thenReturn(Optional.of(franchiseArtist));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberMapper.updateEntityFromDto(testMember, updateDto);
        });
        assertEquals("Referenced artist must be of type SOLO, but was: " + ArtistType.FRANCHISE, exception.getMessage());
    }

    @Test
    void updateEntityFromDto_WithVariousArtist_ShouldThrowException() {
        // Given
        Artist variousArtist = new Artist();
        variousArtist.setArtistId(UUID.randomUUID());
        variousArtist.setType(ArtistType.VARIOUS);
        
        updateDto.setSoloArtistId(variousArtist.getArtistId());
        when(artistService.findById(variousArtist.getArtistId())).thenReturn(Optional.of(variousArtist));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberMapper.updateEntityFromDto(testMember, updateDto);
        });
        assertEquals("Referenced artist must be of type SOLO, but was: " + ArtistType.VARIOUS, exception.getMessage());
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
        MemberSummaryDTO result = memberMapper.toSummaryDTO(testMember);

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
        MemberSummaryDTO result = memberMapper.toSummaryDTO(testMember);

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
        List<MemberSummaryDTO> result = memberMapper.toSummaryDTOList(emptyList);

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
        List<MemberSummaryDTO> result = memberMapper.toSummaryDTOList(members);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        MemberSummaryDTO dto1 = result.get(0);
        assertEquals(member1.getMemberId(), dto1.getMemberId());
        assertEquals("Member 1", dto1.getFullName());
        assertEquals("image1.jpg", dto1.getImage());
        assertTrue(dto1.getHasOfficialSoloDebut());
        assertEquals(soloArtist.getArtistName(), dto1.getSoloArtistName());

        MemberSummaryDTO dto2 = result.get(1);
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
        List<MemberSummaryDTO> result = memberMapper.toSummaryDTOList(singleMemberList);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        MemberSummaryDTO dto = result.get(0);
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
}
