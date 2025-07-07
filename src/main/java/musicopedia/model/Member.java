package musicopedia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID memberId;

    @Column(nullable = false, name = "member_name")
    private String memberName;

    private String realName;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String image;

    private LocalDate birthDate;

    private LocalDate deathDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private java.util.List<Solo> soloIdentities = new java.util.ArrayList<>();

    @Column(length = 2)
    private String nationality;

    public boolean isDeceased() {
        return this.deathDate != null;
    }
}