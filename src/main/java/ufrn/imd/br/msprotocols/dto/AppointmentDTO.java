package ufrn.imd.br.msprotocols.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppointmentDTO(Long id, String description, Long patientId,
                             Long doctorId, String local,
                             ZonedDateTime appointmentDate) implements EntityDTO{
    @Override
    public EntityDTO toResponse() {
        return new AppointmentDTO(
                this.id(),
                this.description(),
                this.patientId(),
                this.doctorId(),
                this.local(),
                this.appointmentDate()
        );
    }
}
