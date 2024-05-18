package burak.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class UserUpdateDto {
    @ApiModelProperty(position = 0)
    private String username;

    @ApiModelProperty(position = 2)
    private int age;

    @ApiModelProperty(position = 3)
    private String description;

    @ApiModelProperty(position = 4)
    private byte[] userImage;

}
