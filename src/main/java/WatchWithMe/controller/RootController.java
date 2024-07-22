package WatchWithMe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RootController {

    @GetMapping("/health")
    @Operation(summary = "Health Check API", description = "AWS health check API")
    public String health() {
        return "It works!";
    }
}

