package ufrn.imd.br.msprotocols.repository;

import ufrn.imd.br.msprotocols.model.Protocol;

import java.util.Optional;

public interface ProtocolRepository extends GenericRepository<Protocol> {

    boolean existsByFileId(Long id);
    Optional<Protocol> findByFileIdAndIdNot(Long fileId, Long protocolId);
}
