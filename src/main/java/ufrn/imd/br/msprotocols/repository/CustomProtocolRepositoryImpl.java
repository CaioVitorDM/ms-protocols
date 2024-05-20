package ufrn.imd.br.msprotocols.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
    public Page<Protocol> searchByFilters(String name, String createdAt, String doctorId, Pageable pageable) {
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
        // Analisa o formato da data e adiciona a cláusula WHERE apropriada
            addDateClause(createdAt, whereClause);
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            whereClause.append(" AND p.doctorId = :doctorId");
        }

        String countQueryStr = "SELECT COUNT(p) FROM Protocol p WHERE p.active = TRUE" + whereClause;
        Query countQuery = entityManager.createQuery(countQueryStr);
        setQueryParameters(countQuery, name, createdAt, doctorId);

        long count = ((Number) countQuery.getSingleResult()).longValue();
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, count);
        }

        // Construção da consulta final com paginação
        String finalQuery = INITIAL + whereClause + " ORDER BY p.createdAt DESC";
        Query query = entityManager.createQuery(finalQuery, Protocol.class);
        setQueryParameters(query, name, createdAt, doctorId);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<Protocol> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }

    private void addDateClause(String createdAt, StringBuilder whereClause) {
        if (createdAt.matches("\\d{2}/\\d{2}/\\d{4}")) {  // Formato DD/MM/YYYY
            whereClause.append(" AND FUNCTION('to_char', p.createdAt, 'DD/MM/YYYY') = :createdAtFull");
        } else if (createdAt.matches("\\d{2}/\\d{2}/") || createdAt.matches("\\d{2}/\\d{2}")) {  // Formatos DD/MM/ ou DD/MM
            whereClause.append(" AND FUNCTION('to_char', p.createdAt, 'DD/MM') = :createdAtMonthDay");
        } else if (createdAt.matches("\\d{2}/") || createdAt.matches("\\d{2}")) {  // Formatos DD/ ou DD
            whereClause.append(" AND FUNCTION('to_char', p.createdAt, 'DD') = :createdAtDay");
        }
    }

    private void setQueryParameters(Query query, String name, String createdAt, String doctorId) {
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (createdAt != null && !createdAt.trim().isEmpty()) {
            if (createdAt.matches("\\d{2}/\\d{2}/\\d{4}")) {
                query.setParameter("createdAtFull", createdAt);
            } else if (createdAt.matches("\\d{2}/\\d{2}/") || createdAt.matches("\\d{2}/\\d{2}")) {
                query.setParameter("createdAtMonthDay", createdAt.replaceAll("/$", ""));
            } else if (createdAt.matches("\\d{2}/") || createdAt.matches("\\d{2}")) {
                query.setParameter("createdAtDay", createdAt.replace("/", ""));
            }
        }
        if (doctorId != null && !doctorId.trim().isEmpty()) {
            query.setParameter("doctorId", doctorId);
        }
    }



}