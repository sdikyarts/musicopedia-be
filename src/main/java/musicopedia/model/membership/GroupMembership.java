package musicopedia.model.membership;

import jakarta.persistence.*;
import lombok.Data;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.MembershipStatus;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "group_memberships")
public class GroupMembership {

    @EmbeddedId
    private GroupMembershipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Artist group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;

    private LocalDate joinDate;
    private LocalDate leaveDate;

    public void syncStatusWithMember() {
        if (this.member != null && this.member.isDeceased()) {
            this.status = MembershipStatus.FORMER;
            this.leaveDate = this.member.getDeathDate();
        }
    }
}