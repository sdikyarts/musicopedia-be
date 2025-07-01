package musicopedia.model.membership;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class GroupMembershipIdTest {

    @Test
    public void testConstructorAndGetters() {
        
        UUID groupId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        
        
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(groupId);
        id.setMemberId(memberId);
        
        
        assertEquals(groupId, id.getGroupId());
        assertEquals(memberId, id.getMemberId());
    }
    
    @Test
    public void testEquality() {
        
        UUID groupId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        
        GroupMembershipId id1 = new GroupMembershipId();
        id1.setGroupId(groupId);
        id1.setMemberId(memberId);
        
        GroupMembershipId id2 = new GroupMembershipId();
        id2.setGroupId(groupId);
        id2.setMemberId(memberId);
        
        GroupMembershipId differentId = new GroupMembershipId();
        differentId.setGroupId(UUID.randomUUID());
        differentId.setMemberId(memberId);
        
        
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, differentId);
        assertNotEquals(id1.hashCode(), differentId.hashCode());
    }
    
    @Test
    public void testToString() {
        
        UUID groupId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        
        GroupMembershipId id = new GroupMembershipId();
        id.setGroupId(groupId);
        id.setMemberId(memberId);
        
        
        String toString = id.toString();
        
        
        assertTrue(toString.contains(groupId.toString()));
        assertTrue(toString.contains(memberId.toString()));
    }
}
