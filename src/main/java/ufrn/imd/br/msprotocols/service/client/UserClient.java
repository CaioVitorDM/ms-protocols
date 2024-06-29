package ufrn.imd.br.msprotocols.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-auth-server", path = "/api/auth-server/v1/users")
public interface UserClient {
    @GetMapping("/patient/check/{id}")
    ResponseEntity<ApiResponseDTO<Boolean>> isValidPatient(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);

    @GetMapping("/medic/check/{id}")
    ResponseEntity<ApiResponseDTO<Boolean>> isValidDoctor(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);

    @PostMapping("/patient/check")
    ResponseEntity<ApiResponseDTO<Map<Long, Boolean>>> checkPatientsExistence(@RequestHeader("Authorization") String token, @RequestBody List<Long> patientIds);

}
