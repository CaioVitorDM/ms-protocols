package ufrn.imd.br.msprotocols.mappers;

import org.springframework.stereotype.Component;
import ufrn.imd.br.msprotocols.dto.AppointmentDTO;
import ufrn.imd.br.msprotocols.model.Appointment;

@Component
public class AppointmentMapper implements DtoMapper<Appointment, AppointmentDTO> {
    @Override
    public AppointmentDTO toDto(Appointment entity) {
        return new AppointmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getLocal(),
                entity.getAppointmentDate()
        );
    }

    @Override
    public Appointment toEntity(AppointmentDTO appointmentDTO) {
        return Appointment.builder()
                .id(appointmentDTO.id())
                .title(appointmentDTO.title())
                .patientId(appointmentDTO.patientId())
                .doctorId(appointmentDTO.doctorId())
                .local(appointmentDTO.local())
                .appointmentDate(appointmentDTO.appointmentDate())
                .build();
    }
}
