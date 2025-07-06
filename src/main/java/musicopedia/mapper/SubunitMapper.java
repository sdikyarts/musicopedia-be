package musicopedia.mapper;

import musicopedia.dto.request.SubunitRequestDTO;
import musicopedia.dto.response.SubunitResponseDTO;
import musicopedia.factory.SubunitFactory;
import musicopedia.model.Subunit;
import musicopedia.model.Groups;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class SubunitMapper {
    private final SubunitFactory subunitFactory;

    public SubunitMapper(SubunitFactory subunitFactory) {
        this.subunitFactory = subunitFactory;
    }

    @Async
    public CompletableFuture<Subunit> toEntity(SubunitRequestDTO dto, Groups mainGroup, Groups groupSubunit) {
        // Use the factory to create subunit with validation
        return subunitFactory.createSubunit(dto, mainGroup, groupSubunit);
    }

    @Async
    public CompletableFuture<SubunitResponseDTO> toResponseDTO(Subunit subunit) {
        SubunitResponseDTO dto = new SubunitResponseDTO();
        dto.setSubunitId(subunit.getSubunitId());
        dto.setMainGroupId(subunit.getMainGroup() != null ? subunit.getMainGroup().getArtistId() : null);
        dto.setSubunitName(subunit.getSubunitName());
        dto.setDescription(subunit.getDescription());
        dto.setImage(subunit.getImage());
        dto.setFormationDate(subunit.getFormationDate());
        dto.setDisbandDate(subunit.getDisbandDate());
        dto.setSubunitGender(subunit.getSubunitGender() != null ? subunit.getSubunitGender().name() : null);
        dto.setActivityStatus(subunit.getActivityStatus() != null ? subunit.getActivityStatus().name() : null);
        dto.setOriginCountry(subunit.getOriginCountry());
        dto.setGroupSubunitId(subunit.getGroupSubunit() != null ? subunit.getGroupSubunit().getArtistId() : null);
        dto.setMainGroupName(
            subunit.getMainGroup() != null && subunit.getMainGroup().getArtist() != null
                ? subunit.getMainGroup().getArtist().getArtistName()
                : null
        );
        dto.setGroupSubunitName(
            subunit.getGroupSubunit() != null && subunit.getGroupSubunit().getArtist() != null
                ? subunit.getGroupSubunit().getArtist().getArtistName()
                : null
        );
        return CompletableFuture.completedFuture(dto);
    }
}
