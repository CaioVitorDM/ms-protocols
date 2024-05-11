package ufrn.imd.br.msprotocols.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;
import ufrn.imd.br.msprotocols.dto.EntityDTO;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.model.Protocol;
import ufrn.imd.br.msprotocols.service.ProtocolService;

@CrossOrigin(origins = "http://localhost:4200/")
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

}
