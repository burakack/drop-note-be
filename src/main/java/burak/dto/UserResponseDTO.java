package burak.dto;

import java.util.List;
import java.util.Set;

import burak.model.Note;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import burak.model.AppUserRole;

@Data
public class UserResponseDTO {

    @ApiModelProperty(position = 0)
    private Integer id;
    @ApiModelProperty(position = 1)
    private String username;
    @ApiModelProperty(position = 2)
    private String email;
    @ApiModelProperty(position = 3)
    private String description;
    @ApiModelProperty(position = 4)
    List<AppUserRole> appUserRoles;


}
