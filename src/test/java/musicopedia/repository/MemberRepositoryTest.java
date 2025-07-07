package musicopedia.repository;

import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.Solo;
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
    
    @Autowired
    private SoloRepository soloRepository;

    private Artist soloArtist;
    private Solo hyunjinSolo;

    @BeforeEach
    void setup() {
        soloArtist = new Artist();
        soloArtist.setArtistName("IU");
        soloArtist.setType(ArtistType.SOLO);
        artistRepository.save(soloArtist);

        Member member1 = new Member();
        member1.setMemberName("Felix");
        member1.setRealName("Felix Yongbok Lee");
        member1.setBirthDate(LocalDate.of(2000, 9, 15));
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setMemberName("Hyunjin");
        member2.setRealName("Hwang Hyun-jin");
        member2.setBirthDate(LocalDate.of(2000, 3, 20));
        memberRepository.save(member2);

        hyunjinSolo = new Solo();
        hyunjinSolo.setArtist(soloArtist);
        hyunjinSolo.setMember(member2);
        soloRepository.save(hyunjinSolo);
        member2.getSoloIdentities().add(hyunjinSolo);
        memberRepository.save(member2);

        Member member3 = new Member();
        member3.setMemberName("HAN");
        member3.setRealName("Han Ji-sung");
        member3.setBirthDate(LocalDate.of(2000, 9, 14));
        memberRepository.save(member3);
    }

    @Test
    public void testFindByMemberNameContainingIgnoreCase() {
        List<Member> hanMembers = memberRepository.findByMemberNameContainingIgnoreCase("HAN");
        assertEquals(1, hanMembers.size());
        assertEquals("HAN", hanMembers.get(0).getMemberName());
        assertEquals("Han Ji-sung", hanMembers.get(0).getRealName());
        List<Member> hyunMembers = memberRepository.findByMemberNameContainingIgnoreCase("hyun");
        assertEquals(1, hyunMembers.size());
        assertEquals("Hyunjin", hyunMembers.get(0).getMemberName());
        assertEquals("Hwang Hyun-jin", hyunMembers.get(0).getRealName());
        // Check that Hyunjin has a solo identity with the correct artist
        Member hyunjin = hyunMembers.get(0);
        assertFalse(hyunjin.getSoloIdentities().isEmpty());
        assertEquals("IU", hyunjin.getSoloIdentities().get(0).getArtist().getArtistName());
    }

    @Test
    public void testFindByRealNameContainingIgnoreCase() {
        List<Member> leeMembers = memberRepository.findByRealNameContainingIgnoreCase("Lee");
        assertEquals(1, leeMembers.size());
        assertEquals("Felix", leeMembers.get(0).getMemberName());
        assertEquals("Felix Yongbok Lee", leeMembers.get(0).getRealName());
        // Check that Felix has no solo identities
        Member felix = leeMembers.get(0);
        assertTrue(felix.getSoloIdentities() == null || felix.getSoloIdentities().isEmpty());
    }

    @Test
    public void testFindByMemberName() {
        Optional<Member> member = memberRepository.findByMemberName("Felix");
        assertTrue(member.isPresent());
        assertEquals(LocalDate.of(2000, 9, 15), member.get().getBirthDate());
    }

    @Test
    public void testFindByBirthDate() {
        LocalDate birthDate = LocalDate.of(2000, 9, 15);
        List<Member> members = memberRepository.findByBirthDate(birthDate);
        assertEquals(1, members.size());
        assertEquals("Felix", members.get(0).getMemberName());
    }

    @Test
    public void testFindByBirthDateAfter() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        List<Member> members = memberRepository.findByBirthDateAfter(date);
        assertEquals(3, members.size());
    }

    @Test
    public void testFindByBirthDateBefore() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        List<Member> members = memberRepository.findByBirthDateBefore(date);
        assertEquals(0, members.size());
    }
}
