package musicopedia.model.membership;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class GroupMembershipId implements Serializable {
    private UUID groupId;
    private UUID memberId;
}