package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PlayerGetAllResponseDto {
    private Player players;


    public static class Player {
        public int id;
        public String screenName;
        public String gender;
        public int age;
    }
}

