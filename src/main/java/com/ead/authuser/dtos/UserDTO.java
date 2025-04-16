package com.ead.authuser.dtos;

import com.ead.authuser.validation.UserNameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    public interface UserView {
        public static interface RegistrationPost {}
        public static interface UserPut {}
        public static interface PasswordPut {}
        public static interface ImagePut {}
    }

    private UUID userId;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @UserNameConstraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String userName;

    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String fullName;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.PasswordPut.class,UserView.RegistrationPost.class})
    @JsonView({UserView.PasswordPut.class,UserView.RegistrationPost.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 270, groups = UserView.PasswordPut.class)
    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.PasswordPut.class,UserView.RegistrationPost.class})
    private String phoneNumber;

    @JsonView({UserView.PasswordPut.class,UserView.RegistrationPost.class})
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
