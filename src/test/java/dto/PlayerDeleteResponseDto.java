package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.testng.annotations.Test;
import util.StatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Test
@Builder
public class PlayerDeleteResponseDto {
    private Object body;
    public StatusCode statusCode;
    public int statusCodeValue;
}
