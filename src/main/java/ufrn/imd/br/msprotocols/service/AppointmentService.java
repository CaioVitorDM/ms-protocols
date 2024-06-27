package ufrn.imd.br.msprotocols.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msprotocols.dto.AppointmentDTO;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.mappers.AppointmentMapper;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;
import ufrn.imd.br.msprotocols.model.Appointment;
import ufrn.imd.br.msprotocols.repository.AppointmentRepository;
import ufrn.imd.br.msprotocols.repository.GenericRepository;
import ufrn.imd.br.msprotocols.utils.exception.BusinessException;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

@Service
public class AppointmentService implements GenericService<Appointment, AppointmentDTO>{

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper mapper;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
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

    public AppointmentDTO update(AppointmentDTO dto){
        Appointment updatedEntity = mapper.toEntity(dto);
        Long appointmentId = dto.id();

        Appointment bdEntity = appointmentRepository.findById(appointmentId).orElseThrow(() -> new BusinessException(
                "Error: Appointment not found with id [" + appointmentId + "]", HttpStatus.NOT_FOUND
        ));

        BeanUtils.copyProperties(updatedEntity, bdEntity, getNullPropertyNames(updatedEntity));

        validateBeforeSave(bdEntity);

        return mapper.toDto(appointmentRepository.save(bdEntity));
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
}
