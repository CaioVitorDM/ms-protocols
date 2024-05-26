package ufrn.imd.br.msprotocols.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msprotocols.dto.AppointmentDTO;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.mappers.AppointmentMapper;
import ufrn.imd.br.msprotocols.mappers.DtoMapper;
import ufrn.imd.br.msprotocols.model.Appointment;
import ufrn.imd.br.msprotocols.repository.AppointmentRepository;
import ufrn.imd.br.msprotocols.repository.GenericRepository;

@Transactional
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

    public Page<AppointmentDTO> findAppointmentsByFilters(String title, String patientId, String local, String appointmentDate, Pageable pageable) {
        return appointmentRepository.searchByFilters(title, patientId, local, appointmentDate, pageable).map(mapper::toDto);
    }
}
