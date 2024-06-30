package ufrn.imd.br.msprotocols.controller;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.model.Protocol;
import ufrn.imd.br.msprotocols.service.ProtocolService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/protocols")
public class ProtocolController extends GenericController<Protocol, ProtocolDTO, ProtocolService>{
    /**
     * Constructs a GenericController instance with the provided service.
     *
     * @param service The service associated with the controller.
     */
    protected ProtocolController(ProtocolService service) {
        super(service);
    }


//    @PostMapping("/register")
//    public ResponseEntity<ApiResponseDTO<EntityDTO>> createProtocol (
//            @Valid @RequestBody ProtocolDTO dto
//    ){
//        return ResponseEntity.status(HttpStatus.CREATED).body(
//                new ApiResponseDTO<>(
//                        true,
//                        "Sucess: Protocol created successfully.",
//                        service.create(dto).toResponse(),
//                        null
//                )
//        );
//    }

    @GetMapping("/find-protocols")
    public ResponseEntity<ApiResponseDTO<Page<ProtocolDTO>>> findProtocols(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String createdAt,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String doctorId)
    {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: protocols retrieved successfully",
                service.findProtocolsByFilters(name, createdAt, doctorId, patientId, pageable),
                null
        ));
    }

    @PutMapping("/edit-protocol")
    public ResponseEntity<ApiResponseDTO<ProtocolDTO>> updateProtocol
            (@Valid @RequestBody ProtocolDTO dto, @RequestHeader("Authorization") String token){

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucess: Protocol edited.",
                        service.update(dto, token),
                        null
                )
        );
    }


    @GetMapping("/find-by-doctor")
    public ResponseEntity<ApiResponseDTO<List<ProtocolDTO>>> findByDoctorId(
            @RequestParam Long doctorId) {
        List<ProtocolDTO> protocols = service.findByDoctorId(doctorId);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Protocols retrieved successfully.",
                protocols,
                null
        ));
    }



}
