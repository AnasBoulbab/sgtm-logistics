package ma.aza.sgtm.logistics.services;

import ma.aza.sgtm.logistics.dtos.AppUserPostDto;
import ma.aza.sgtm.logistics.dtos.AppUserPutDto;
import ma.aza.sgtm.logistics.entities.AppUser;
import ma.aza.sgtm.logistics.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser getUser(Long userId) {
        // TODO define proper exception
        return appUserRepository.findById(userId).orElseThrow();
    }

    public AppUser getUser(String userName) {
        return appUserRepository.findByUsername(userName);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser addUser(AppUserPostDto appUserPostDto) {
        AppUser appUser = appUserRepository.findByUsername(appUserPostDto.getUsername());
        if (appUser!=null) throw new RuntimeException("this user already exist");
        appUser = AppUser.builder()
                .username(appUserPostDto.getUsername())
                .password(passwordEncoder.encode(appUserPostDto.getPassword()))
                .role("USER")
//                .role(appUserPostDto.getRole())
                .apiKey(appUserPostDto.getApiKey())
                .build();
        return appUserRepository.save(appUser);
    }

    public AppUser updateUser(AppUserPutDto appUserPutDto) {
        AppUser appUser = appUserRepository.findById(appUserPutDto.getUserId())
                .orElseThrow(()-> new RuntimeException("This user does not exist"));
        appUser.setUsername(appUserPutDto.getUsername());
        appUser.setPassword( passwordEncoder.encode(appUserPutDto.getPassword()) );
        return appUserRepository.save(appUser);
    }

    public AppUser update(AppUser appUser) {
        return this.appUserRepository.save(appUser);
    }

}
