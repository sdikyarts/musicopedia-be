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

    @Column(nullable = false)
    private String fullName;
    
    @Lob
    private String image;

    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solo_artist_id")
    private Artist soloArtist;
}