package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlayerUpdateRequestDto {
    private int age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;
}
