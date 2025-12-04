package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.services.VehicleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;



}
