package musicopedia.mapper;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import musicopedia.factory.SubunitFactory;
import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Subunit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubunitMapperTest {
    @Mock
    private SubunitFactory subunitFactory;
    private SubunitMapper subunitMapper;
    private SubunitRequestDTO dto;
    private Groups mainGroup;
    private Groups groupSubunit;
    private Subunit subunit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subunitMapper = new SubunitMapper(subunitFactory);
        dto = new SubunitRequestDTO();
        dto.setSubunitName("Test Subunit");
        dto.setDescription("desc");
        dto.setImage("img");
        dto.setFormationDate(LocalDate.now());
        dto.setDisbandDate(LocalDate.now().plusYears(1));
        dto.setSubunitGender("FEMALE");
        dto.setActivityStatus("ACTIVE");
        dto.setOriginCountry("KR");
        mainGroup = new Groups();
        groupSubunit = new Groups();
        subunit = new Subunit();
        subunit.setSubunitId(UUID.randomUUID());
        subunit.setMainGroup(mainGroup);
        subunit.setSubunitName("Test Subunit");
        subunit.setDescription("desc");
        subunit.setImage("img");
        subunit.setFormationDate(dto.getFormationDate());
        subunit.setDisbandDate(dto.getDisbandDate());
        subunit.setSubunitGender(null);
        subunit.setActivityStatus(null);
        subunit.setOriginCountry("KR");
        subunit.setGroupSubunit(groupSubunit);
    }

    @Test
    void toEntity_shouldDelegateToFactory() throws Exception {
        when(subunitFactory.createSubunit(dto, mainGroup, groupSubunit)).thenReturn(CompletableFuture.completedFuture(subunit));
        CompletableFuture<Subunit> result = subunitMapper.toEntity(dto, mainGroup, groupSubunit);
        assertEquals(subunit, result.get());
        verify(subunitFactory).createSubunit(dto, mainGroup, groupSubunit);
    }

    @Test
    void toResponseDTO_shouldMapFields() throws Exception {
        // Set up mainGroup and groupSubunit with non-null Artist
        Artist mainArtist = new Artist();
        mainArtist.setArtistName("Main Group Name");
        mainGroup.setArtist(mainArtist);
        Artist subunitArtist = new Artist();
        subunitArtist.setArtistName("Subunit Group Name");
        groupSubunit.setArtist(subunitArtist);
        subunit.setMainGroup(mainGroup);
        subunit.setGroupSubunit(groupSubunit);
        subunit.setSubunitGender(null);
        subunit.setActivityStatus(null);
        SubunitResponseDTO dtoResult = subunitMapper.toResponseDTO(subunit).get();
        assertEquals(subunit.getSubunitId(), dtoResult.getSubunitId());
        assertEquals(subunit.getSubunitName(), dtoResult.getSubunitName());
        assertEquals(subunit.getDescription(), dtoResult.getDescription());
        assertEquals(subunit.getImage(), dtoResult.getImage());
        assertEquals(subunit.getFormationDate(), dtoResult.getFormationDate());
        assertEquals(subunit.getDisbandDate(), dtoResult.getDisbandDate());
        assertEquals(subunit.getOriginCountry(), dtoResult.getOriginCountry());
        assertEquals("Main Group Name", dtoResult.getMainGroupName());
        assertEquals("Subunit Group Name", dtoResult.getGroupSubunitName());
    }

    @Test
    void toResponseDTO_shouldMapGenderAndActivityStatus() throws Exception {
        subunit.setSubunitGender(musicopedia.model.enums.ArtistGender.FEMALE);
        subunit.setActivityStatus(musicopedia.model.enums.GroupActivityStatus.ACTIVE);
        // Set up mainGroup and groupSubunit with non-null Artist
        Artist mainArtist = new Artist();
        mainArtist.setArtistName("Main Group Name");
        mainGroup.setArtist(mainArtist);
        Artist subunitArtist = new Artist();
        subunitArtist.setArtistName("Subunit Group Name");
        groupSubunit.setArtist(subunitArtist);
        subunit.setMainGroup(mainGroup);
        subunit.setGroupSubunit(groupSubunit);
        SubunitResponseDTO dtoResult = subunitMapper.toResponseDTO(subunit).get();
        assertEquals("FEMALE", dtoResult.getSubunitGender());
        assertEquals("ACTIVE", dtoResult.getActivityStatus());
    }

    @Test
    void toResponseDTO_shouldHandleNullGroupSubunit() throws Exception {
        subunit.setGroupSubunit(null);
        // Set up mainGroup with non-null Artist
        Artist mainArtist = new Artist();
        mainArtist.setArtistName("Main Group Name");
        mainGroup.setArtist(mainArtist);
        subunit.setMainGroup(mainGroup);
        SubunitResponseDTO dtoResult = subunitMapper.toResponseDTO(subunit).get();
        assertNull(dtoResult.getGroupSubunitId());
        assertNull(dtoResult.getGroupSubunitName());
    }

    @Test
    void toResponseDTO_shouldHandleNullMainGroup() throws Exception {
        subunit.setMainGroup(null);
        // Set up groupSubunit with non-null Artist
        Artist subunitArtist = new Artist();
        subunitArtist.setArtistName("Subunit Group Name");
        groupSubunit.setArtist(subunitArtist);
        subunit.setGroupSubunit(groupSubunit);
        SubunitResponseDTO dtoResult = subunitMapper.toResponseDTO(subunit).get();
        assertNull(dtoResult.getMainGroupName());
    }

    @Test
    void toResponseDTO_shouldHandleNullArtists() throws Exception {
        // mainGroup and groupSubunit exist but their artists are null
        mainGroup.setArtist(null);
        groupSubunit.setArtist(null);
        subunit.setMainGroup(mainGroup);
        subunit.setGroupSubunit(groupSubunit);
        SubunitResponseDTO dtoResult = subunitMapper.toResponseDTO(subunit).get();
        assertNull(dtoResult.getMainGroupName());
        assertNull(dtoResult.getGroupSubunitName());
    }
}
