package ma.aza.sgtm.logistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserGetDto {

    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    private String role;
    private String apiKey;

}
