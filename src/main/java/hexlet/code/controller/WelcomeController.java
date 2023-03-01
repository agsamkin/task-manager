package hexlet.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    public static final String WELCOME_CONTROLLER_PATH = "/welcome";

    @Operation(summary = "Welcome path")
    @ApiResponse(responseCode = "200")
    @GetMapping(WELCOME_CONTROLLER_PATH)
    public String welcome() {
        return "Welcome to Spring";
    }
}

