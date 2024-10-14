package longrun.springsecuritysessionlogin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotIdResponse {
    public String id;

    public ForgotIdResponse(String id){
        this.id=id;
    }
}
