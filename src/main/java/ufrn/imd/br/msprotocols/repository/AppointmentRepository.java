package ufrn.imd.br.msprotocols.repository;

import org.springframework.data.jpa.repository.Query;
import ufrn.imd.br.msprotocols.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends GenericRepository<Appointment>, CustomAppointmentRepository {

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate >= :date ORDER BY a.appointmentDate ASC")
    Optional<Appointment> findNextAppointmentByDoctorId(Long doctorId, LocalDate date);


}
