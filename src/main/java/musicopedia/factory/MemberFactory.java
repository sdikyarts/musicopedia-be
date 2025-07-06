package musicopedia.factory;

import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.model.Artist;
import musicopedia.model.Member;
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
                    
                    member.setSoloArtist(soloArtist);
                    return CompletableFuture.completedFuture(member);
                });
        }
        
        return CompletableFuture.completedFuture(member);
    }

    /**
     * Updates a member's solo artist reference.
     * Used when a member gets an official solo debut.
     */
    public void linkToSoloArtist(Member member, Artist soloArtist) {
        if (soloArtist.getType() != ArtistType.SOLO) {
            throw new IllegalArgumentException(
                String.format("Cannot link member '%s' to artist '%s' - only SOLO artists can be linked to members", 
                member.getMemberName(), soloArtist.getArtistName()));
        }
        
        member.setSoloArtist(soloArtist);
    }

    /**
     * Removes solo artist reference from a member.
     * Used if a solo artist relationship needs to be removed.
     */
    public void unlinkFromSoloArtist(Member member) {
        member.setSoloArtist(null);
    }
}
