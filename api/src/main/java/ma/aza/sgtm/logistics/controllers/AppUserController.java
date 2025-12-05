package ma.aza.sgtm.logistics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.aza.sgtm.logistics.converters.AppUserConverter;
import ma.aza.sgtm.logistics.dtos.AppUserGetDto;
import ma.aza.sgtm.logistics.dtos.AppUserPostDto;
import ma.aza.sgtm.logistics.dtos.AppUserPutDto;
import ma.aza.sgtm.logistics.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app-users")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Manage application users (admin only)")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user",
            parameters = @Parameter(name = "id", description = "User id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User returned",
                            content = @Content(schema = @Schema(implementation = AppUserGetDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public AppUserGetDto getUser(@PathVariable Long id) {
        return AppUserConverter.convert( appUserService.getUser(id) );
    }

    @GetMapping
    @Operation(
            summary = "List users",
            description = "Returns all application users.",
            responses = @ApiResponse(responseCode = "200", description = "Users returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUserGetDto.class))))
    )
    public List<AppUserGetDto> getAllUsers() {
        return appUserService.getAllUsers().stream().map(AppUserConverter::convert).collect(Collectors.toList());
    }

    @PostMapping
    @Operation(
            summary = "Create user",
            description = "Creates a new user with a role.",
            responses = @ApiResponse(responseCode = "200", description = "User created",
                    content = @Content(schema = @Schema(implementation = AppUserGetDto.class)))
    )
    public AppUserGetDto addUser(@RequestBody AppUserPostDto appUserPostDto) {
        return AppUserConverter.convert( appUserService.addUser(appUserPostDto) );
    }

    @PutMapping
    @Operation(
            summary = "Update user",
            description = "Updates user information and role.",
            responses = @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = AppUserGetDto.class)))
    )
    public AppUserGetDto updateUser(@RequestBody AppUserPutDto appUserPutDto) {
        return AppUserConverter.convert( appUserService.updateUser(appUserPutDto) );
    }

}
