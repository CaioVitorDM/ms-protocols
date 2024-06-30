package ufrn.imd.br.msprotocols.repository;

import ufrn.imd.br.msprotocols.model.Protocol;

import java.util.List;
import java.util.Optional;

public interface ProtocolRepository extends GenericRepository<Protocol>, CustomProtocolRepository {

    boolean existsByFileId(Long id);
    Optional<Protocol> findByFileIdAndIdNot(Long fileId, Long protocolId);

    Optional<Protocol> findByName(String name);


}
