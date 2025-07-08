package musicopedia.model.membership;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import musicopedia.model.Member;
import musicopedia.model.Subunit;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "subunit_membership")
public class SubunitMembership {
    @EmbeddedId
    private SubunitMembershipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subunitId")
    @JoinColumn(name = "subunit_id", referencedColumnName = "subunitId")
    private Subunit subunit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", referencedColumnName = "memberId")
    private Member member;

    private LocalDate joinedDate;
    private LocalDate leftDate;

    @Data
    @EqualsAndHashCode
    @Embeddable
    public static class SubunitMembershipId implements Serializable {
        private UUID subunitId;
        private UUID memberId;
    }
}
