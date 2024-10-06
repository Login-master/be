package longrun.springsecuritysessionlogin.dto.response;

import lombok.Data;
@Data
public class ForgotIdResponse {
    public String id;

    public ForgotIdResponse(String id){
        this.id=id;
    }
}
