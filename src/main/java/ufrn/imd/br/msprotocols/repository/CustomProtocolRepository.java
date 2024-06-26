package ufrn.imd.br.msprotocols.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ufrn.imd.br.msprotocols.model.Protocol;

import java.time.ZonedDateTime;
import java.util.List;

public interface CustomProtocolRepository {
    Page<Protocol> searchByFilters(String name, String createdAt, String doctorId, String patientId, Pageable pageable);

    List<Protocol> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);

}