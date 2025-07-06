package musicopedia.dto.request;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubunitRequestDTOTest {
    @Test
    void testSettersAndGetters() {
        SubunitRequestDTO dto = new SubunitRequestDTO();
        UUID mainGroupId = UUID.randomUUID();
        UUID groupSubunitId = UUID.randomUUID();
        dto.setMainGroupId(mainGroupId);
        dto.setSubunitName("Test");
        dto.setDescription("desc");
        dto.setImage("img");
        LocalDate formation = LocalDate.now();
        LocalDate disband = LocalDate.now().plusYears(1);
        dto.setFormationDate(formation);
        dto.setDisbandDate(disband);
        dto.setSubunitGender("FEMALE");
        dto.setActivityStatus("ACTIVE");
        dto.setOriginCountry("KR");
        dto.setGroupSubunitId(groupSubunitId);

        assertEquals(mainGroupId, dto.getMainGroupId());
        assertEquals("Test", dto.getSubunitName());
        assertEquals("desc", dto.getDescription());
        assertEquals("img", dto.getImage());
        assertEquals(formation, dto.getFormationDate());
        assertEquals(disband, dto.getDisbandDate());
        assertEquals("FEMALE", dto.getSubunitGender());
        assertEquals("ACTIVE", dto.getActivityStatus());
        assertEquals("KR", dto.getOriginCountry());
        assertEquals(groupSubunitId, dto.getGroupSubunitId());
    }
}
