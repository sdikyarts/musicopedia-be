package musicopedia.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubunitTest {
    @Test
    void testSettersAndGetters() {
        Subunit subunit = new Subunit();
        UUID subunitId = UUID.randomUUID();
        Groups mainGroup = new Groups();
        Groups groupSubunit = new Groups();
        subunit.setSubunitId(subunitId);
        subunit.setMainGroup(mainGroup);
        subunit.setSubunitName("Test Subunit");
        subunit.setDescription("desc");
        subunit.setImage("img");
        LocalDate formation = LocalDate.now();
        LocalDate disband = LocalDate.now().plusYears(1);
        subunit.setFormationDate(formation);
        subunit.setDisbandDate(disband);
        subunit.setSubunitGender(null);
        subunit.setActivityStatus(null);
        subunit.setOriginCountry("KR");
        subunit.setGroupSubunit(groupSubunit);

        assertEquals(subunitId, subunit.getSubunitId());
        assertEquals(mainGroup, subunit.getMainGroup());
        assertEquals("Test Subunit", subunit.getSubunitName());
        assertEquals("desc", subunit.getDescription());
        assertEquals("img", subunit.getImage());
        assertEquals(formation, subunit.getFormationDate());
        assertEquals(disband, subunit.getDisbandDate());
        assertNull(subunit.getSubunitGender());
        assertNull(subunit.getActivityStatus());
        assertEquals("KR", subunit.getOriginCountry());
        assertEquals(groupSubunit, subunit.getGroupSubunit());
    }
}
