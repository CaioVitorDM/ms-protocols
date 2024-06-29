package ufrn.imd.br.msprotocols.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;

@FeignClient(name = "ms-file-server", path = "/api/file-server/v1/files")
public interface FileClient {

    @GetMapping("/check/{id}")
    ResponseEntity<ApiResponseDTO<Boolean>> isValidFile(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);

}
