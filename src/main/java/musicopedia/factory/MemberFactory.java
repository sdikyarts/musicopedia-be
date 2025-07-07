package musicopedia.factory;

import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class MemberFactory {

    private final ArtistService artistService;

    public MemberFactory(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * Creates a new Member entity from a MemberRequestDTO.
     * Validates solo artist reference if provided.
     */
    public CompletableFuture<Member> createMember(MemberRequestDTO dto) {
        // Basic validation
        if (dto.getMemberName() == null || dto.getMemberName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required");
        }
        // Enforce realName validation for full branch coverage
        if (dto.getRealName() == null || dto.getRealName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member real name is required");
        }

        Member member = new Member();
        member.setMemberName(dto.getMemberName().trim());
        member.setRealName(dto.getRealName().trim());
        member.setDescription(dto.getDescription());
        member.setImage(dto.getImage());
        member.setBirthDate(dto.getBirthDate());
        member.setDeathDate(dto.getDeathDate());
        member.setNationality(dto.getNationality());
        
        // Handle solo artist reference with validation
        if (dto.getSoloArtistId() != null) {
            return artistService.findByIdAsync(dto.getSoloArtistId())
                .thenCompose(optionalArtist -> {
                    Artist soloArtist = optionalArtist.orElseThrow(() -> 
                        new IllegalArgumentException("Solo artist not found with ID: " + dto.getSoloArtistId()));
                    // Business rule: Only allow SOLO type artists to be referenced
                    if (soloArtist.getType() != ArtistType.SOLO) {
                        throw new IllegalArgumentException(
                            String.format("Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members, but this artist is type: %s", 
                            dto.getMemberName(), soloArtist.getArtistName(), soloArtist.getType()));
                    }
                    // Find or create the Solo entity for this artist
                    // In this model, you likely need to fetch the Solo entity by artistId from a SoloService or SoloRepository
                    // For now, create a new Solo if not found (in real code, fetch from DB)
                    Solo solo = new Solo();
                    solo.setArtistId(soloArtist.getArtistId());
                    solo.setArtist(soloArtist);
                    // Optionally set other fields from dto if needed
                    linkToSoloIdentity(member, solo);
                    return CompletableFuture.completedFuture(member);
                });
        }
        
        return CompletableFuture.completedFuture(member);
    }

    /**
     * Updates a member's solo artist reference.
     * Used when a member gets an official solo debut.
     * Updated: now links a Solo to a Member (one-to-many).
     */
    public void linkToSoloIdentity(Member member, Solo solo) {
        if (solo == null) {
            throw new IllegalArgumentException("Solo identity cannot be null");
        }
        solo.setMember(member);
        if (member.getSoloIdentities() == null) {
            member.setSoloIdentities(new java.util.ArrayList<>());
        }
        if (!member.getSoloIdentities().contains(solo)) {
            member.getSoloIdentities().add(solo);
        }
    }

    /**
     * Removes solo identity reference from a member.
     * Used if a solo identity relationship needs to be removed.
     */
    public void unlinkFromSoloIdentity(Member member, Solo solo) {
        if (member.getSoloIdentities() != null) {
            member.getSoloIdentities().remove(solo);
        }
        solo.setMember(null);
    }
}
