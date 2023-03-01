package hexlet.code.controller;

import hexlet.code.dto.AuthenticationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.LoginController.LOGIN_CONTROLLER_PATH;

@Tag(name = "login-controller", description = "Get JWT token")
@RestController
@RequestMapping("${base-url}" + LOGIN_CONTROLLER_PATH)
public class LoginController {
    public static final String LOGIN_CONTROLLER_PATH = "/login";

    @Operation(summary = "Generate JWT token")
    @ApiResponse(responseCode = "200")
    @PostMapping
    public String login(@RequestBody AuthenticationDto authenticationDto) {
        // Этот эндпоинт обсуживается фильтром JWTAuthenticationFilter
        // и нужен только для получения доступа к нему из документации swagger
        return null;
    }
}
