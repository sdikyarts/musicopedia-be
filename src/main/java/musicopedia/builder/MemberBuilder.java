package musicopedia.builder;

import musicopedia.model.Member;
import musicopedia.model.Artist;
import java.time.LocalDate;

public class MemberBuilder {
    private String memberName;
    private String realName;
    private String description;
    private String image;
    private LocalDate birthDate;
    private Artist soloArtist;
    private String nationality;

    public MemberBuilder setMemberName(String memberName) {
        this.memberName = memberName;
        return this;
    }

    public MemberBuilder setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public MemberBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public MemberBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public MemberBuilder setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public MemberBuilder setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public Member build() {
        Member member = new Member();
        member.setMemberName(this.memberName);
        member.setRealName(this.realName);
        member.setDescription(this.description);
        member.setImage(this.image);
        member.setBirthDate(this.birthDate);
        // member.setSoloArtist(this.soloArtist); // removed, no longer valid
        // If you want to add a solo identity, use:
        // if (this.soloArtist != null) {
        //     Solo solo = new Solo();
        //     solo.setArtist(this.soloArtist);
        //     member.getSoloIdentities().add(solo);
        // }
        member.setNationality(this.nationality);
        return member;
    }
}
