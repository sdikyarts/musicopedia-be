package musicopedia.model.membership;

import jakarta.persistence.*;
import lombok.Data;
import musicopedia.model.Member;
import musicopedia.model.Subunit;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "subunit_membership")
@IdClass(SubunitMembership.SubunitMembershipId.class)
public class SubunitMembership {
    @Id
    private UUID subunitId;
    @Id
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subunit_id", referencedColumnName = "subunitId", insertable = false, updatable = false)
    private Subunit subunit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "memberId", insertable = false, updatable = false)
    private Member member;

    private LocalDate joinedDate;
    private LocalDate leftDate;

    @Data
    public static class SubunitMembershipId implements Serializable {
        private UUID subunitId;
        private UUID memberId;
    }
}
