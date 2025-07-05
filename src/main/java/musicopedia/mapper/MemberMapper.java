package musicopedia.mapper;

import musicopedia.builder.MemberBuilder;
import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;

import musicopedia.factory.MemberFactory;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class MemberMapper {

    private final ArtistService artistService;
    private final MemberFactory memberFactory;

    public MemberMapper(ArtistService artistService, MemberFactory memberFactory) {
        this.artistService = artistService;
        this.memberFactory = memberFactory;
    }

    public CompletableFuture<Member> toEntity(MemberRequestDTO dto) {
        // Use the factory to create member with validation
        return memberFactory.createMember(dto);
    }

    public CompletableFuture<Void> updateEntityFromDto(Member member, MemberRequestDTO dto) {
        if (dto.getFullName() != null) {
            member.setFullName(dto.getFullName());
        }
        if (dto.getDescription() != null) {
            member.setDescription(dto.getDescription());
        }
        if (dto.getImage() != null) {
            member.setImage(dto.getImage());
        }
        if (dto.getBirthDate() != null) {
            member.setBirthDate(dto.getBirthDate());
        }
        
        // Handle solo artist update
        if (dto.getSoloArtistId() != null) {
            return artistService.findByIdAsync(dto.getSoloArtistId())
                .thenCompose(optionalArtist -> {
                    Artist soloArtist = optionalArtist.orElseThrow(() -> 
                        new IllegalArgumentException("Solo artist not found with ID: " + dto.getSoloArtistId()));
                    
                    // Validate that the referenced artist is actually a solo artist
                    if (soloArtist.getType() != ArtistType.SOLO) {
                        throw new IllegalArgumentException("Referenced artist must be of type SOLO, but was: " + soloArtist.getType());
                    }
                    
                    member.setSoloArtist(soloArtist);
                    return CompletableFuture.completedFuture(null);
                });
        } else {
            // If soloArtistId is explicitly null in the update, remove the reference
            member.setSoloArtist(null);
            return CompletableFuture.completedFuture(null);
        }
    }

    public MemberResponseDTO toResponseDTO(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setMemberId(member.getMemberId());
        dto.setFullName(member.getFullName());
        dto.setDescription(member.getDescription());
        dto.setImage(member.getImage());
        dto.setBirthDate(member.getBirthDate());
        
        // Set solo artist information if available
        if (member.getSoloArtist() != null) {
            dto.setSoloArtistId(member.getSoloArtist().getArtistId());
            dto.setSoloArtistName(member.getSoloArtist().getArtistName());
            dto.setHasOfficialSoloDebut(true);
        } else {
            dto.setHasOfficialSoloDebut(false);
        }
        
        return dto;
    }

    public MemberResponseDTO toSummaryDTO(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setMemberId(member.getMemberId());
        dto.setFullName(member.getFullName());
        dto.setImage(member.getImage());
        
        // Set solo career information
        if (member.getSoloArtist() != null) {
            dto.setHasOfficialSoloDebut(true);
            dto.setSoloArtistName(member.getSoloArtist().getArtistName());
        } else {
            dto.setHasOfficialSoloDebut(false);
        }
        
        return dto;
    }

    public List<MemberResponseDTO> toResponseDTOList(List<Member> members) {
        return members.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<MemberResponseDTO> toSummaryDTOList(List<Member> members) {
        return members.stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    public Member createMemberFromDto(MemberRequestDTO dto, Artist soloArtist) {
        return new MemberBuilder()
            .setFullName(dto.getFullName())
            .setDescription(dto.getDescription())
            .setImage(dto.getImage())
            .setBirthDate(dto.getBirthDate())
            .setSoloArtist(soloArtist)
            .build();
    }
}
