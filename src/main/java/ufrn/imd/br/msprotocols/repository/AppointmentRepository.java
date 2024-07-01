package ufrn.imd.br.msprotocols.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufrn.imd.br.msprotocols.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends GenericRepository<Appointment>, CustomAppointmentRepository {

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate >= :today AND a.active = TRUE ORDER BY a.appointmentDate ASC")
    List<Appointment> findNextAppointment(@Param("doctorId") String doctorId, @Param("today") LocalDate today);


}
