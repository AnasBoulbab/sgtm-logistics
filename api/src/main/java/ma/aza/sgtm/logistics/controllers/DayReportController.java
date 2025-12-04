package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.services.DayReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/day-reports")
@RequiredArgsConstructor
public class DayReportController {

    private final DayReportService dayReportService;



}
