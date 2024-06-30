package ufrn.imd.br.msprotocols.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ufrn.imd.br.msprotocols.model.Protocol;
import ufrn.imd.br.msprotocols.repository.CustomProtocolRepository;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Transactional
public class CustomProtocolRepositoryImpl implements CustomProtocolRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String INITIAL = "SELECT p FROM Protocol p WHERE p.active = TRUE ";

    @Override
    public Page<Protocol> searchByFilters(String name, String createdAt, String doctorId, String patientId, Pageable pageable) {
        StringBuilder whereClause = new StringBuilder();

        String orderField = "createdAt";
        String orderDirection = "DESC";

        if (!pageable.getSort().isUnsorted()) {
            orderField = pageable.getSort().get().iterator().next().getProperty();
            orderDirection = pageable.getSort().get().iterator().next().getDirection().name();
        }


        if (name != null && !name.trim().isEmpty()) {
            whereClause.append(" AND LOWER(p.name) LIKE LOWER(:name)");
        }
        if (createdAt != null && !createdAt.trim().isEmpty()) {
            whereClause.append(" AND FUNCTION('DATE', p.createdAt) = :createdAt");
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            whereClause.append(" AND p.doctorId = :doctorId");
        }

        if (patientId != null && !patientId.trim().isEmpty()) {
            whereClause.append(" AND (");
            whereClause.append(" (p.isSpecific = TRUE AND :patientId MEMBER OF p.patientsIdList)");
            whereClause.append(" OR (p.isSpecific = FALSE AND p.doctorId = :doctorId)");
            whereClause.append(")");
        }

        String countQueryStr = "SELECT COUNT(p) FROM Protocol p WHERE p.active = TRUE" + whereClause;
        Query countQuery = entityManager.createQuery(countQueryStr);
        setQueryParameters(countQuery, name, createdAt, doctorId, patientId);

        long count = ((Number) countQuery.getSingleResult()).longValue();
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, count);
        }

        String finalQuery = INITIAL + whereClause;
        Query query = entityManager.createQuery(finalQuery, Protocol.class);
        setQueryParameters(query, name, createdAt, doctorId, patientId);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<Protocol> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }

    private void setQueryParameters(Query query, String name, String createdAt, String doctorId, String patientId) {
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (createdAt != null && !createdAt.trim().isEmpty()) {
            LocalDate localDate = LocalDate.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE);
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            query.setParameter("createdAt", zonedDateTime);
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            query.setParameter("doctorId", doctorId);
        }
        if (patientId != null && !patientId.trim().isEmpty()) {
            query.setParameter("patientId", patientId);
        }
    }

    public List<Protocol> findByDoctorIdOrderByCreatedAtDesc(Long doctorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Protocol> query = cb.createQuery(Protocol.class);
        Root<Protocol> protocol = query.from(Protocol.class);

        query.select(protocol)
                .where(cb.equal(protocol.get("doctorId"), doctorId))
                .orderBy(cb.desc(protocol.get("createdAt")))
                .distinct(true);

        return entityManager.createQuery(query)
                .setMaxResults(5)
                .getResultList();
    }

}