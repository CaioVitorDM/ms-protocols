package ufrn.imd.br.msprotocols.controller;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.msprotocols.dto.ApiResponseDTO;
import ufrn.imd.br.msprotocols.dto.AppointmentDTO;
import ufrn.imd.br.msprotocols.dto.ProtocolDTO;
import ufrn.imd.br.msprotocols.model.Appointment;
import ufrn.imd.br.msprotocols.service.AppointmentService;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController extends GenericController<Appointment, AppointmentDTO, AppointmentService> {
    /**
     * Constructs a GenericController instance with the provided service.
     *
     * @param service The service associated with the controller.
     */
    protected AppointmentController(AppointmentService service) {
        super(service);
    }

    @GetMapping("/find-appointments")
    public ResponseEntity<ApiResponseDTO<Page<AppointmentDTO>>> findAppointments(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String local,
            @RequestParam(required = false) String appointmentDate)
    {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: appointments retrieved successfully",
                service.findAppointmentsByFilters(description, patientId, doctorId, local, appointmentDate, pageable),
                null
        ));
    }

    @PutMapping("/edit-appointment")
    public ResponseEntity<ApiResponseDTO<AppointmentDTO>> updateAppointment
            (@Valid @RequestBody AppointmentDTO dto){

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDTO<>(
                        true,
                        "Sucesso: Appointment edited.",
                        service.update(dto),
                        null
                )
        );
    }
}
