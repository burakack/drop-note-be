package burak.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import burak.model.AppUserRole;

@Data
@NoArgsConstructor
public class UserDataDTO {

    @ApiModelProperty(position = 0)
    private String username;
    @ApiModelProperty(position = 1)
    private String email;
    @ApiModelProperty(position = 2)
    private String password;
    @ApiModelProperty(position = 3)
    List<AppUserRole> appUserRoles;
    @ApiModelProperty(position = 4)
    private boolean isAcceptedMailUpdates;
    @ApiModelProperty(position = 6)
    private int age;
    @ApiModelProperty(position = 8)
    private String gender;
    @ApiModelProperty(position = 9)
    private String description;

}
