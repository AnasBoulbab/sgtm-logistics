package ma.aza.sgtm.logistics.converters;

import ma.aza.sgtm.logistics.dtos.AppUserGetDto;
import ma.aza.sgtm.logistics.entities.AppUser;

public class AppUserConverter {

    public static AppUserGetDto convert(AppUser appUser) {
        return AppUserGetDto.builder()
                .userId(appUser.getUserId())
                .createdAt(appUser.getCreatedAt())
                .updatedAt(appUser.getUpdatedAt())
                .username(appUser.getUsername())
                .role(appUser.getRole())
                .apiKey(appUser.getApiKey())
                .build();
    }

}
