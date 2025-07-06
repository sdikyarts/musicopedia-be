package musicopedia.factory;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.model.Groups;
import musicopedia.model.Subunit;
import musicopedia.model.enums.ArtistGender;
import musicopedia.model.enums.GroupActivityStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class SubunitFactoryTest {
    private SubunitFactory subunitFactory;
    private SubunitRequestDTO dto;
    private Groups mainGroup;
    private Groups groupSubunit;

    @BeforeEach
    void setUp() {
        subunitFactory = new SubunitFactory();
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
        mainGroup.setArtistId(UUID.randomUUID());
        groupSubunit = new Groups();
        groupSubunit.setArtistId(UUID.randomUUID());
    }

    @Test
    void createSubunit_shouldMapFields() throws Exception {
        CompletableFuture<Subunit> future = subunitFactory.createSubunit(dto, mainGroup, groupSubunit);
        Subunit subunit = future.get();
        assertEquals(mainGroup, subunit.getMainGroup());
        assertEquals("Test Subunit", subunit.getSubunitName());
        assertEquals("desc", subunit.getDescription());
        assertEquals("img", subunit.getImage());
        assertEquals(dto.getFormationDate(), subunit.getFormationDate());
        assertEquals(dto.getDisbandDate(), subunit.getDisbandDate());
        assertEquals(ArtistGender.FEMALE, subunit.getSubunitGender());
        assertEquals(GroupActivityStatus.ACTIVE, subunit.getActivityStatus());
        assertEquals("KR", subunit.getOriginCountry());
        assertEquals(groupSubunit, subunit.getGroupSubunit());
    }

    @Test
    void createSubunit_shouldThrowIfMainGroupNull() {
        assertThrows(IllegalArgumentException.class, () -> subunitFactory.createSubunit(dto, null, groupSubunit).get());
    }

    @Test
    void createSubunit_shouldHandleNullSubunitGender() throws Exception {
        dto.setSubunitGender(null);
        CompletableFuture<Subunit> future = subunitFactory.createSubunit(dto, mainGroup, groupSubunit);
        Subunit subunit = future.get();
        assertNull(subunit.getSubunitGender());
    }

    @Test
    void createSubunit_shouldHandleNullActivityStatus() throws Exception {
        dto.setActivityStatus(null);
        CompletableFuture<Subunit> future = subunitFactory.createSubunit(dto, mainGroup, groupSubunit);
        Subunit subunit = future.get();
        assertNull(subunit.getActivityStatus());
    }
}
