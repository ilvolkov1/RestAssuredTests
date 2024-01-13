package models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserResponseModel extends CreateUserRequestModel{
    String id, createdAt;

}