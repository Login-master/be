package longrun.springsecuritytokenlogin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import longrun.springsecuritytokenlogin.domain.User;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminResponse {

    List<User> userList;
}
