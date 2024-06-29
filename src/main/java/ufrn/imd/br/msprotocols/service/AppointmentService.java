package ufrn.imd.br.msprotocols.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;
import ufrn.imd.br.msprotocols.dto.AppointmentDTO;
import org.springframework.http.ResponseEntity;
import ufrn.imd.br.msprotocols.mappers.AppointmentMapper;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;
import ufrn.imd.br.msprotocols.model.Appointment;
import ufrn.imd.br.msprotocols.repository.AppointmentRepository;
import ufrn.imd.br.msprotocols.repository.GenericRepository;
import ufrn.imd.br.msprotocols.service.client.UserClient;
import ufrn.imd.br.msprotocols.utils.exception.BusinessException;
import ufrn.imd.br.msprotocols.utils.validators.GenericEntityValidator;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

@Service
public class AppointmentService implements GenericService<Appointment, AppointmentDTO>{

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper mapper;

    private final UserClient userClient;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper mapper, UserClient userClient) {
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
        this.userClient = userClient;
    }

    @Override
    public GenericRepository<Appointment> getRepository() {
        return appointmentRepository;
    }

    @Override
    public DtoMapper<Appointment, AppointmentDTO> getDtoMapper() {
        return mapper;
    }

    public Page<AppointmentDTO> findAppointmentsByFilters(String title, String patientId, String doctorId, String local, String appointmentDate, Pageable pageable) {
        return appointmentRepository.searchByFilters(title, patientId, doctorId, local, appointmentDate, pageable).map(mapper::toDto);
    }

    public AppointmentDTO update(AppointmentDTO dto, String token){
        Appointment updatedEntity = mapper.toEntity(dto);
        Long appointmentId = dto.id();

        Appointment bdEntity = appointmentRepository.findById(appointmentId).orElseThrow(() -> new BusinessException(
                "Error: Appointment not found with id [" + appointmentId + "]", HttpStatus.NOT_FOUND
        ));

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        validateBeforeUpdate(bdEntity, token);

        return mapper.toDto(appointmentRepository.save(bdEntity));
    }


    public AppointmentDTO create(AppointmentDTO dto, String token) {
        Appointment entity = getDtoMapper().toEntity(dto);
        validateBeforeSave(entity, token);
        return getDtoMapper().toDto(getRepository().save(entity));
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

    public void validatePatient(Long patientId, String token) {
        ResponseEntity<ApiResponseDTO<Boolean>> responseEntity = userClient.isValidPatient(token, patientId);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new BusinessException("Failure in communication with authentication service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ApiResponseDTO<Boolean> response = responseEntity.getBody();
        if (response.getData() == null || !response.getData()) {
            throw new BusinessException("Invalid patient ID: " + patientId, HttpStatus.BAD_REQUEST);
        }
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


    @Override
    public void validateBeforeSave(Appointment entity, String token) {
        GenericEntityValidator.validate(entity);
        validatePatient(entity.getPatientId(), token);
        validateDoctor(entity.getDoctorId(), token);
    }

    @Override
    public void validateBeforeUpdate(Appointment entity, String token) {
        GenericEntityValidator.validate(entity);
        validatePatient(entity.getPatientId(), token);
        validateDoctor(entity.getDoctorId(), token);
    }
}
