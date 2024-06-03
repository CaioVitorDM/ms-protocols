package ufrn.imd.br.msprotocols.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ufrn.imd.br.msprotocols.model.Appointment;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Transactional
public class CustomAppointmentRepositoryImpl implements CustomAppointmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String INITIAL = "SELECT a FROM Appointment a WHERE a.active = TRUE ";

    @Override
    public Page<Appointment> searchByFilters(String description, String patientId, String doctorId,
                                             String local, String appointmentDate, Pageable pageable) {
        StringBuilder whereClause = new StringBuilder();

        String orderField = "appointmentDate";
        String orderDirection = "DESC";

        if (!pageable.getSort().isUnsorted()) {
            orderField = pageable.getSort().get().iterator().next().getProperty();
            orderDirection = pageable.getSort().get().iterator().next().getDirection().name();
        }


        if (description != null && !description.trim().isEmpty()) {
            whereClause.append(" AND LOWER(a.description) LIKE LOWER(:description)");
        }

        if (local != null && !local.trim().isEmpty()) {
            whereClause.append(" AND LOWER(a.local) LIKE LOWER(:local)");
        }

        if (appointmentDate != null && !appointmentDate.trim().isEmpty()) {
            whereClause.append(" AND FUNCTION('DATE', a.appointmentDate) = :appointmentDate");
        }
        if (patientId != null && !patientId.trim().isEmpty()) {
            whereClause.append(" AND a.patientId = :patientId");
        }

        if (doctorId != null && !doctorId.trim().isEmpty()) {
            whereClause.append(" AND a.doctorId = :doctorId");
        }

        String countQueryStr = "SELECT COUNT(a) FROM Appointment a WHERE a.active = TRUE" + whereClause;
        Query countQuery = entityManager.createQuery(countQueryStr);
        setQueryParameters(countQuery, description, patientId, doctorId, local, appointmentDate);

        long count = ((Number) countQuery.getSingleResult()).longValue();
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, count);
        }

        // Construção da consulta final com paginação
        String finalQuery = INITIAL + whereClause + " ORDER BY a.appointmentDate DESC";
        Query query = entityManager.createQuery(finalQuery, Appointment.class);
        setQueryParameters(query, description, patientId, doctorId, local, appointmentDate);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<Appointment> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }

    private void setQueryParameters(Query query, String description, String patientId,
                                    String doctorId, String local, String appointmentDate) {
        if (description != null && !description.trim().isEmpty()) {
            query.setParameter("description", "%" + description + "%");
        }
        if (local != null && !local.trim().isEmpty()) {
            query.setParameter("local", "%" + local + "%");
        }
        if (appointmentDate != null && !appointmentDate.trim().isEmpty()) {
            LocalDate localDate = LocalDate.parse(appointmentDate, DateTimeFormatter.ISO_LOCAL_DATE);
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            query.setParameter("appointmentDate", zonedDateTime);
        }
        if (patientId != null && !patientId.trim().isEmpty()) {
            query.setParameter("patientId", patientId);
        }

        if (doctorId != null && !doctorId.trim().isEmpty()) {
            query.setParameter("doctorId", doctorId);
        }
    }
}
