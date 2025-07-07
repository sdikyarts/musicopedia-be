package musicopedia.mapper;

import musicopedia.builder.MemberBuilder;
import musicopedia.dto.request.MemberRequestDTO;
import musicopedia.dto.response.MemberResponseDTO;

import musicopedia.factory.MemberFactory;
import musicopedia.model.Artist;
import musicopedia.model.Member;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistType;
import musicopedia.service.ArtistService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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
        if (dto.getMemberName() != null) {
            member.setMemberName(dto.getMemberName());
        }
        if (dto.getRealName() != null) {
            member.setRealName(dto.getRealName());
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
            CompletableFuture<Optional<Artist>> artistFuture = artistService.findByIdAsync(dto.getSoloArtistId());
            if (artistFuture == null) {
                return CompletableFuture.completedFuture(null);
            }
            return artistFuture.thenCompose(optionalArtist -> {
                Artist soloArtist = optionalArtist.orElseThrow(() ->
                    new IllegalArgumentException("Solo artist not found with ID: " + dto.getSoloArtistId()));
                if (soloArtist.getType() != ArtistType.SOLO) {
                    throw new IllegalArgumentException("Referenced artist must be of type SOLO, but was: " + soloArtist.getType());
                }
                java.util.List<Solo> newSoloIdentities = new java.util.ArrayList<>();
                Solo solo = new Solo(soloArtist, member);
                newSoloIdentities.add(solo);
                member.setSoloIdentities(newSoloIdentities);
                return CompletableFuture.completedFuture(null);
            });
        }
        // Do not clear soloIdentities if soloArtistId is null
        return CompletableFuture.completedFuture(null);
    }

    public MemberResponseDTO toResponseDTO(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setMemberId(member.getMemberId());
        dto.setMemberName(member.getMemberName());
        dto.setRealName(member.getRealName());
        dto.setDescription(member.getDescription());
        dto.setImage(member.getImage());
        dto.setBirthDate(member.getBirthDate());
        // Set solo artist information if available
        Solo firstSolo = (member.getSoloIdentities() != null && !member.getSoloIdentities().isEmpty()) ? member.getSoloIdentities().get(0) : null;
        if (firstSolo != null && firstSolo.getArtist() != null) {
            dto.setSoloArtistId(firstSolo.getArtist().getArtistId());
            dto.setSoloArtistName(firstSolo.getArtist().getArtistName());
            dto.setHasOfficialSoloDebut(true);
        } else {
            dto.setHasOfficialSoloDebut(false);
            dto.setSoloArtistId(null);
            dto.setSoloArtistName(null);
        }
        return dto;
    }

    public MemberResponseDTO toSummaryDTO(Member member) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setMemberId(member.getMemberId());
        dto.setMemberName(member.getMemberName());
        dto.setImage(member.getImage());
        dto.setRealName(member.getRealName());
        // Set solo career information
        Solo firstSolo = (member.getSoloIdentities() != null && !member.getSoloIdentities().isEmpty()) ? member.getSoloIdentities().get(0) : null;
        if (firstSolo != null && firstSolo.getArtist() != null) {
            dto.setHasOfficialSoloDebut(true);
            dto.setSoloArtistName(firstSolo.getArtist().getArtistName());
        } else {
            dto.setHasOfficialSoloDebut(false);
            dto.setSoloArtistName(null);
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
        Member member = new MemberBuilder()
            .setMemberName(dto.getMemberName())
            .setRealName(dto.getRealName())
            .setDescription(dto.getDescription())
            .setImage(dto.getImage())
            .setBirthDate(dto.getBirthDate())
            .build();
        if (soloArtist != null) {
            Solo solo = new Solo(soloArtist, member);
            member.getSoloIdentities().add(solo);
        }
        return member;
    }
}
