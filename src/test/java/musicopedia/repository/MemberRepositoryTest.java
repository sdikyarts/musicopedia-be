package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(RepositoryTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    private Artist soloArtist;

    @BeforeEach
    void setup() {
        soloArtist = new Artist();
        soloArtist.setArtistName("IU");
        soloArtist.setType(ArtistType.Solo);
        artistRepository.save(soloArtist);

        Member member1 = new Member();
        member1.setFullName("Kim Namjoon");
        member1.setBirthDate(LocalDate.of(1994, 9, 12));
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setFullName("Lee Ji-eun");
        member2.setBirthDate(LocalDate.of(1993, 5, 16));
        member2.setSoloArtist(soloArtist);
        memberRepository.save(member2);

        Member member3 = new Member();
        member3.setFullName("Jung Hoseok");
        member3.setBirthDate(LocalDate.of(1994, 2, 18));
        memberRepository.save(member3);
    }

    @Test
    public void testFindByFullNameContainingIgnoreCase() {
        List<Member> kimMembers = memberRepository.findByFullNameContainingIgnoreCase("Kim");
        assertEquals(1, kimMembers.size());
        assertEquals("Kim Namjoon", kimMembers.get(0).getFullName());
        
        List<Member> leeMembers = memberRepository.findByFullNameContainingIgnoreCase("lee");
        assertEquals(1, leeMembers.size());
        assertEquals("Lee Ji-eun", leeMembers.get(0).getFullName());
    }

    @Test
    public void testFindByFullName() {
        Optional<Member> member = memberRepository.findByFullName("Kim Namjoon");
        assertTrue(member.isPresent());
        assertEquals(LocalDate.of(1994, 9, 12), member.get().getBirthDate());
    }

    @Test
    public void testFindByBirthDate() {
        LocalDate birthDate = LocalDate.of(1994, 9, 12);
        List<Member> members = memberRepository.findByBirthDate(birthDate);
        assertEquals(1, members.size());
        assertEquals("Kim Namjoon", members.get(0).getFullName());
    }

    @Test
    public void testFindByBirthDateAfter() {
        LocalDate date = LocalDate.of(1994, 3, 1);
        List<Member> members = memberRepository.findByBirthDateAfter(date);
        assertEquals(1, members.size());
        assertEquals("Kim Namjoon", members.get(0).getFullName());
    }

    @Test
    public void testFindByBirthDateBefore() {
        LocalDate date = LocalDate.of(1994, 3, 1);
        List<Member> members = memberRepository.findByBirthDateBefore(date);
        assertEquals(2, members.size());
    }

    @Test
    public void testFindBySoloArtist() {
        List<Member> members = memberRepository.findBySoloArtist(soloArtist);
        assertEquals(1, members.size());
        assertEquals("Lee Ji-eun", members.get(0).getFullName());
    }

    @Test
    public void testFindBySoloArtistId() {
        List<Member> members = memberRepository.findBySoloArtistId(soloArtist.getArtistId());
        assertEquals(1, members.size());
        assertEquals("Lee Ji-eun", members.get(0).getFullName());
    }

    @Test
    public void testCountBySoloArtistId() {
        long count = memberRepository.countBySoloArtistId(soloArtist.getArtistId());
        assertEquals(1, count);
    }

    @Test
    public void testExistsByFullName() {
        assertTrue(memberRepository.existsByFullName("Kim Namjoon"));
        assertFalse(memberRepository.existsByFullName("Unknown Person"));
    }

    @Test
    public void testFindBySoloArtistIsNull() {
        List<Member> members = memberRepository.findBySoloArtistIsNull();
        assertEquals(2, members.size());
    }
}
