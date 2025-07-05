package musicopedia.mapper;

import musicopedia.dto.request.ArtistRequestDTO;
import musicopedia.dto.response.ArtistResponseDTO;

import musicopedia.model.Artist;
import musicopedia.model.Groups;
import musicopedia.model.Solo;
import musicopedia.model.enums.ArtistType;
import musicopedia.factory.ArtistFactoryManager;
import org.springframework.stereotype.Component;
import musicopedia.builder.SoloBuilder;
import musicopedia.builder.GroupsBuilder;
import musicopedia.builder.ArtistBuilder;

@Component
public class ArtistMapper {

    private final ArtistFactoryManager artistFactoryManager;

    public ArtistMapper(ArtistFactoryManager artistFactoryManager) {
        this.artistFactoryManager = artistFactoryManager;
    }

    public Artist toEntity(ArtistRequestDTO dto) {
        // Use the factory pattern to create artist with type-specific logic
        return artistFactoryManager.createArtist(dto);
    }

    public void updateEntityFromDto(Artist artist, ArtistRequestDTO dto) {
        if (dto.getArtistName() != null) {
            artist.setArtistName(dto.getArtistName());
        }
        if (dto.getType() != null) {
            artist.setType(dto.getType());
        }
        if (dto.getSpotifyId() != null) {
            artist.setSpotifyId(dto.getSpotifyId());
        }
        if (dto.getDescription() != null) {
            artist.setDescription(dto.getDescription());
        }
        if (dto.getImage() != null) {
            artist.setImage(dto.getImage());
        }
        if (dto.getPrimaryLanguage() != null) {
            artist.setPrimaryLanguage(dto.getPrimaryLanguage());
        }
        if (dto.getGenre() != null) {
            artist.setGenre(dto.getGenre());
        }
        if (dto.getOriginCountry() != null) {
            artist.setOriginCountry(dto.getOriginCountry());
        }
    }

    public ArtistResponseDTO toResponseDto(Artist artist, Solo solo, Groups group) {
        ArtistResponseDTO dto = new ArtistResponseDTO();
        dto.setArtistId(artist.getArtistId());
        dto.setSpotifyId(artist.getSpotifyId());
        dto.setArtistName(artist.getArtistName());
        dto.setDescription(artist.getDescription());
        dto.setImage(artist.getImage());
        dto.setType(artist.getType());
        dto.setPrimaryLanguage(artist.getPrimaryLanguage());
        dto.setGenre(artist.getGenre());
        dto.setOriginCountry(artist.getOriginCountry());

        // Add solo-specific data if available
        if (solo != null) {
            dto.setBirthDate(solo.getBirthDate());
            dto.setDeathDate(solo.getDeathDate());
            dto.setSoloGender(solo.getGender());
            dto.setGroupAffiliationStatus(solo.getGroupAffiliationStatus());
        }

        // Add group-specific data if available
        if (group != null) {
            dto.setFormationDate(group.getFormationDate());
            dto.setDisbandDate(group.getDisbandDate());
            dto.setGroupGender(group.getGroupGender());
            dto.setActivityStatus(group.getActivityStatus());
        }

        return dto;
    }

    public ArtistResponseDTO toSummaryDto(Artist artist) {
        ArtistResponseDTO dto = new ArtistResponseDTO();
        dto.setArtistId(artist.getArtistId());
        dto.setArtistName(artist.getArtistName());
        dto.setType(artist.getType());
        dto.setImage(artist.getImage());
        dto.setGenre(artist.getGenre());
        dto.setOriginCountry(artist.getOriginCountry());
        return dto;
    }

    public Solo createSoloFromDto(ArtistRequestDTO dto, Artist artist) {
        if (dto.getType() != ArtistType.SOLO) {
            return null;
        }
        SoloBuilder builder = new SoloBuilder()
            .setArtistName(artist.getArtistName())
            .setSpotifyId(artist.getSpotifyId())
            .setDescription(artist.getDescription())
            .setImage(artist.getImage())
            .setType(artist.getType())
            .setPrimaryLanguage(artist.getPrimaryLanguage())
            .setGenre(artist.getGenre())
            .setOriginCountry(artist.getOriginCountry())
            .setBirthDate(dto.getBirthDate())
            .setDeathDate(dto.getDeathDate())
            .setGender(dto.getSoloGender())
            .setGroupAffiliationStatus(dto.getGroupAffiliationStatus());
        return builder.buildSolo();
    }

    public Groups createGroupFromDto(ArtistRequestDTO dto, Artist artist) {
        if (dto.getType() != ArtistType.GROUP) {
            return null;
        }
        GroupsBuilder builder = new GroupsBuilder()
            .setArtistName(artist.getArtistName())
            .setSpotifyId(artist.getSpotifyId())
            .setDescription(artist.getDescription())
            .setImage(artist.getImage())
            .setType(artist.getType())
            .setPrimaryLanguage(artist.getPrimaryLanguage())
            .setGenre(artist.getGenre())
            .setOriginCountry(artist.getOriginCountry())
            .setFormationDate(dto.getFormationDate())
            .setDisbandDate(dto.getDisbandDate())
            .setGroupGender(dto.getGroupGender())
            .setActivityStatus(dto.getActivityStatus());
        return builder.buildGroups();
    }

    public Artist createArtistFromDto(ArtistRequestDTO dto) {
        return new ArtistBuilder()
            .setArtistName(dto.getArtistName())
            .setSpotifyId(dto.getSpotifyId())
            .setDescription(dto.getDescription())
            .setImage(dto.getImage())
            .setType(dto.getType())
            .setPrimaryLanguage(dto.getPrimaryLanguage())
            .setGenre(dto.getGenre())
            .setOriginCountry(dto.getOriginCountry())
            .build();
    }
}
