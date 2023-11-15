package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlayerGetByPlayerIdResponseDto {

    private int age;
    private String gender;
    private int id;
    private String login;
    private String password;
    private String role;
    private String screenName;

}
