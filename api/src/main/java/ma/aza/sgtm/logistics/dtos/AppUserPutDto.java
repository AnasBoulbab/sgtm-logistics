package ma.aza.sgtm.logistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserPutDto {

    private Long userId;
    private String username;
    private String password;

}
