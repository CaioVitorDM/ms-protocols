package ufrn.imd.br.msprotocols.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProtocolDTO(Long id, String name,
                          String description, Long fileId,
                          List<Long>patientsIdList, Boolean isSpecific, ZonedDateTime createdAt) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return new ProtocolDTO(
                this.id(),
                this.name(),
                this.description(),
                this.fileId(),
                this.patientsIdList(),
                this.isSpecific(),
                this.createdAt()
        );
    }
}
