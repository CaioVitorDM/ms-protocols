package ufrn.imd.br.msprotocols.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ufrn.imd.br.msprotocols.model.Appointment;
import ufrn.imd.br.msprotocols.model.Protocol;

public interface CustomAppointmentRepository {

    Page<Appointment> searchByFilters(String description, String patientId, String doctorId, String local, String appointmentDate, Pageable pageable);
}
