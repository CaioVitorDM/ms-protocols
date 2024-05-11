package ufrn.imd.br.msprotocols.repository;

import ufrn.imd.br.msprotocols.model.Protocol;

public interface ProtocolRepository extends GenericRepository<Protocol> {

    boolean existsByFileId(Long id);
}
