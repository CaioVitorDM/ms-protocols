package ufrn.imd.br.msprotocols.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;

import java.time.ZonedDateTime;
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
    private final ProtocolMapper protocolMapper;

    public ProtocolService(ProtocolRepository protocolRepository, ProtocolMapper protocolMapper) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
    }

    @Override
    public GenericRepository<Protocol> getRepository() {
        return this.protocolRepository;
    }

    @Override
    public DtoMapper<Protocol, ProtocolDTO> getDtoMapper() {
        return this.protocolMapper;
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

    public Page<ProtocolDTO> findProtocolsByFilters(String name, String createdAt, String doctorId, String patientId, Pageable pageable) {
        return protocolRepository.searchByFilters(name, createdAt, doctorId, patientId, pageable).map(protocolMapper::toDto);
    }


}
