package ufrn.imd.br.msprotocols.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;
import ufrn.imd.br.msprotocols.model.builder.AppointmentBuilder;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments")
@Where(clause = "active = true")
public class Appointment extends BaseEntity {

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @NotBlank
    @Column(nullable = false)
    private String local;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private ZonedDateTime appointmentDate;

    public static AppointmentBuilder builder(){
        return new AppointmentBuilder();
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

    public ZonedDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(ZonedDateTime appointmentDate) {
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
