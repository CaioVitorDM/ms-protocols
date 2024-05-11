package ufrn.imd.br.msprotocols.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.imd.br.msprotocols.dto.AppointmentDTO;
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
}
