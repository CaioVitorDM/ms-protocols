package ufrn.imd.br.msprotocols.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;

import java.beans.PropertyDescriptor;
import java.util.*;

import ufrn.imd.br.msprotocols.mappers.ProtocolMapper;
import ufrn.imd.br.msprotocols.model.Protocol;
import ufrn.imd.br.msprotocols.repository.GenericRepository;
import ufrn.imd.br.msprotocols.repository.ProtocolRepository;
import ufrn.imd.br.msprotocols.service.client.FileClient;
import ufrn.imd.br.msprotocols.service.client.UserClient;
import ufrn.imd.br.msprotocols.utils.exception.BusinessException;
import ufrn.imd.br.msprotocols.utils.validators.GenericEntityValidator;

@Service
public class ProtocolService implements GenericService<Protocol, ProtocolDTO>{

    private final ProtocolRepository protocolRepository;
    private final ProtocolMapper protocolMapper;
    private final UserClient userClient;

    private final FileClient fileClient;

    public ProtocolService(ProtocolRepository protocolRepository, ProtocolMapper protocolMapper,
                           UserClient userClient, FileClient fileClient) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
        this.userClient = userClient;
        this.fileClient = fileClient;
    }

    @Override
    public GenericRepository<Protocol> getRepository() {
        return this.protocolRepository;
    }

    @Override
    public DtoMapper<Protocol, ProtocolDTO> getDtoMapper() {
        return this.protocolMapper;
    }

    public Page<ProtocolDTO> findProtocolsByFilters(String name, String createdAt, String doctorId, String patientId, Pageable pageable) {
        return protocolRepository.searchByFilters(name, createdAt, doctorId, patientId, pageable).map(protocolMapper::toDto);
    }

    public ProtocolDTO update(ProtocolDTO dto, String token){
        Protocol updatedEntity = protocolMapper.toEntity(dto);
        Long protocolId = dto.id();

        Protocol bdEntity = protocolRepository.findById(protocolId).orElseThrow(() -> new BusinessException(
                "Error: Protocol not found with id [" + protocolId + "]", HttpStatus.NOT_FOUND
        ));

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        validateBeforeUpdate(bdEntity, token);

        return protocolMapper.toDto(protocolRepository.save(bdEntity));
    }

    /**
     * Retrieves the names of properties with null values from the given object.
     *
     * @param source The object from which to extract property names.
     * @return An array of property names with null values.
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void validateDoctor(Long doctorId, String token) {

        ResponseEntity<ApiResponseDTO<Boolean>> responseEntity = userClient.isValidDoctor(token, doctorId);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new BusinessException("Failure in communication with authentication service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ApiResponseDTO<Boolean> response = responseEntity.getBody();
        if (response.getData() == null || !response.getData()) {
            throw new BusinessException("Invalid doctor ID: " + doctorId, HttpStatus.BAD_REQUEST);
        }

    }

    private void validateFileId(Long fileId, Long protocolId, String token) {

        ResponseEntity<ApiResponseDTO<Boolean>> responseEntity = fileClient.isValidFile(token, fileId);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new BusinessException("Failure in communication with file service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ApiResponseDTO<Boolean> response = responseEntity.getBody();
        if (response.getData() == null || !response.getData()) {
            throw new BusinessException("Invalid file ID: " + fileId, HttpStatus.BAD_REQUEST);
        }

        Optional<Protocol> existingProtocolWithFile = protocolRepository.findByFileIdAndIdNot(fileId, protocolId);

        if (existingProtocolWithFile.isPresent()) {
            throw new BusinessException(
                    "Arquivo inválido: " + fileId + ". Já existe outro protocolo cadastrado com esse arquivo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    private void validateFileId(Long id, String token){

        ResponseEntity<ApiResponseDTO<Boolean>> responseEntity = fileClient.isValidFile(token, id);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new BusinessException("Failure in communication with file service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ApiResponseDTO<Boolean> response = responseEntity.getBody();
        if (response.getData() == null || !response.getData()) {
            throw new BusinessException("Invalid file ID: " + id, HttpStatus.BAD_REQUEST);
        }

        if(protocolRepository.existsByFileId(id)){
            throw new BusinessException(
                    "Arquivo inválido: " + id + ". Já existe um protocolo cadastrado com esse arquivo.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePatients(List<Long> patientsIdList, String token) {
        ResponseEntity<ApiResponseDTO<Map<Long, Boolean>>> response = userClient.checkPatientsExistence(token, patientsIdList);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
            Map<Long, Boolean> patientExistenceMap = response.getBody().getData();
            for (Map.Entry<Long, Boolean> entry : patientExistenceMap.entrySet()) {
                if (!entry.getValue()) {
                    throw new BusinessException("Invalid patient ID: " + entry.getKey(), HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            throw new BusinessException("Error checking patient IDs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void validateBeforeSave(Protocol entity, String token){

        if (!entity.getSpecific()) {
            entity.setPatientsIdList(new ArrayList<>());
        } else {
            if (entity.getPatientsIdList() == null || entity.getPatientsIdList().isEmpty()) {
                throw new IllegalArgumentException("Quando protocolo é especifico, a lista de pacientes não pode ser nula ou vazia.");
            } else {
                validatePatients(entity.getPatientsIdList(), token);
            }
        }

        GenericEntityValidator.validate(entity);
        validateFileId(entity.getFileId(), token);
        validateDoctor(entity.getDoctorId(), token);
        validatePatients(entity.getPatientsIdList(), token);
    }

    @Override
    public void validateBeforeUpdate(Protocol entity, String token) {

        if (!entity.getSpecific()) {
            entity.setPatientsIdList(new ArrayList<>());
        } else {
            if (entity.getPatientsIdList() == null || entity.getPatientsIdList().isEmpty()) {
                throw new IllegalArgumentException("Quando protocolo é especifico, a lista de pacientes não pode ser nula ou vazia.");
            } else {
                validatePatients(entity.getPatientsIdList(), token);
            }
        }

        GenericEntityValidator.validate(entity);
        validateFileId(entity.getFileId(), entity.getId(), token);
        validateDoctor(entity.getDoctorId(), token);
        validatePatients(entity.getPatientsIdList(), token);
    }


}
