package ufrn.imd.br.msprotocols.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;
import ufrn.imd.br.msprotocols.model.builder.AppointmentBuilder;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments")
@Where(clause = "active = true")
public class Appointment extends BaseEntity {

    @NotBlank(message = "Error: A descrição não pode estar vazia.")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Error: O ID do paciente não pode ser nulo.")
    @Column(nullable = false)
    private Long patientId;

    @NotNull(message = "Error: O ID do médico não pode ser nulo.")
    @Column(nullable = false)
    private Long doctorId;

    @NotBlank(message = "Error: O local não pode estar vazio.")
    @Column(nullable = false)
    private String local;

    @NotNull(message = "Error: Data não pode ser nula.")
    @Column(nullable = false)
    private LocalDate appointmentDate;

    public static AppointmentBuilder builder(){
        return new AppointmentBuilder();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(patientId, that.patientId) && Objects.equals(doctorId, that.doctorId) && Objects.equals(local, that.local) && Objects.equals(appointmentDate, that.appointmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), patientId, doctorId, local, appointmentDate);
    }
}
