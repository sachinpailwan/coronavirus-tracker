package com.pailsom.coronavirustracker;

import com.pailsom.coronavirustracker.models.LocationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Controller
public class CoronaVirusController {

    @Autowired
    private CoronaVirusService service;

    @GetMapping("/")
    public String home(Model model) throws IOException {
        List<LocationStats> allStats = service.getStats();
        model.addAttribute("locationStats",allStats);
        model.addAttribute("totalReportedCases",allStats.stream().mapToInt(s->s.getLastTotalCases()).sum());
        model.addAttribute("totalNewCases",allStats.stream().mapToInt(s->s.getDiffFromPreDay()).sum());
        return "home";
    }
}
