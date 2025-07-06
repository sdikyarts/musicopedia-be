package musicopedia.dto.common;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BaseSubunitDTOTest {
    static class TestDTO extends BaseSubunitDTO {}

    @Test
    void testSettersAndGetters() {
        TestDTO dto = new TestDTO();
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

    @Test
    void testEqualsAndHashCode() {
        TestDTO dto1 = new TestDTO();
        TestDTO dto2 = new TestDTO();
        UUID mainGroupId = UUID.randomUUID();
        dto1.setMainGroupId(mainGroupId);
        dto2.setMainGroupId(mainGroupId);
        dto1.setSubunitName("Test");
        dto2.setSubunitName("Test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TestDTO dto = new TestDTO();
        dto.setSubunitName("Test");
        String toString = dto.toString();
        assertTrue(toString.contains("Test"));
    }
}
