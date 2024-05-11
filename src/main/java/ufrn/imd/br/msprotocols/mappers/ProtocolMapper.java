package ufrn.imd.br.msprotocols.mappers;


import org.springframework.stereotype.Component;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.model.Protocol;

@Component
public class ProtocolMapper implements DtoMapper<Protocol, ProtocolDTO> {

    @Override
    public ProtocolDTO toDto(Protocol entity) {
        return new ProtocolDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getFileId(),
                entity.getPatientsIdList(),
                entity.getSpecific(),
                entity.getCreatedAt()
        );
    }

    @Override
    public Protocol toEntity(ProtocolDTO protocolDTO) {
        return Protocol.builder()
                .id(protocolDTO.id())
                .name(protocolDTO.name())
                .description(protocolDTO.description())
                .fileId(protocolDTO.fileId())
                .patientsIdList(protocolDTO.patientsIdList())
                .isSpecific(protocolDTO.isSpecific())
                .createdAt(protocolDTO.createdAt())
                .build();
    }
}
