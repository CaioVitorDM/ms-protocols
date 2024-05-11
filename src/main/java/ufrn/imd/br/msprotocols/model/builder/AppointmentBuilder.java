package ufrn.imd.br.msprotocols.model.builder;

import ufrn.imd.br.msprotocols.model.Appointment;

import java.time.ZonedDateTime;

public class AppointmentBuilder {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String local;
    private ZonedDateTime appointmentDate;

    public AppointmentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AppointmentBuilder patientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public AppointmentBuilder doctorId(Long doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public AppointmentBuilder local(String local) {
        this.local = local;
        return this;
    }

    public AppointmentBuilder appointmentDate(ZonedDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public Appointment build() {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setLocal(local);
        appointment.setAppointmentDate(appointmentDate);
        return appointment;
    }
}
