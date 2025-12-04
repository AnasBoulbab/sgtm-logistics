package ma.aza.sgtm.logistics.controllers;

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
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{id}")
    public AppUserGetDto getUser(@PathVariable Long id) {
        return AppUserConverter.convert( appUserService.getUser(id) );
    }

    @GetMapping
    public List<AppUserGetDto> getAllUsers() {
        return appUserService.getAllUsers().stream().map(AppUserConverter::convert).collect(Collectors.toList());
    }

    @PostMapping
    public AppUserGetDto addUser(@RequestBody AppUserPostDto appUserPostDto) {
        return AppUserConverter.convert( appUserService.addUser(appUserPostDto) );
    }

    @PutMapping
    public AppUserGetDto updateUser(@RequestBody AppUserPutDto appUserPutDto) {
        return AppUserConverter.convert( appUserService.updateUser(appUserPutDto) );
    }

}
