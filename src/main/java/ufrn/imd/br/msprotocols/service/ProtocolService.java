package ufrn.imd.br.msprotocols.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;
import java.util.Optional;

import ufrn.imd.br.msprotocols.mappers.ProtocolMapper;
import ufrn.imd.br.msprotocols.model.Protocol;
import ufrn.imd.br.msprotocols.repository.GenericRepository;
import ufrn.imd.br.msprotocols.repository.ProtocolRepository;
import ufrn.imd.br.msprotocols.utils.exception.BusinessException;

@Transactional
@Service
public class ProtocolService implements GenericService<Protocol, ProtocolDTO>{

    private final ProtocolRepository protocolRepository;
    private final ProtocolMapper mapper;

    public ProtocolService(ProtocolRepository protocolRepository, ProtocolMapper mapper) {
        this.protocolRepository = protocolRepository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Protocol> getRepository() {
        return this.protocolRepository;
    }

    @Override
    public DtoMapper<Protocol, ProtocolDTO> getDtoMapper() {
        return this.mapper;
    }

    @Override
    public void validateBeforeSave(Protocol entity){
        GenericService.super.validateBeforeSave(entity);
        validateFileId(entity.getFileId());
    }

    @Override
    public void validateBeforeUpdate(Protocol entity){
        GenericService.super.validateBeforeUpdate(entity);
        validateFileId(entity.getFileId(), entity.getId());
    }

    private void validateFileId(Long fileId, Long protocolId) {
        Optional<Protocol> existingProtocolWithFile = protocolRepository.findByFileIdAndIdNot(fileId, protocolId);

        if (existingProtocolWithFile.isPresent()) {
            throw new BusinessException(
                    "Arquivo inválido: " + fileId + ". Já existe outro protocolo cadastrado com esse arquivo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    private void validateFileId(Long id){
        if(protocolRepository.existsByFileId(id)){
            throw new BusinessException(
                    "Arquivo inválido: " + id + ". Já existe um protocolo cadastrado com esse arquivo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
