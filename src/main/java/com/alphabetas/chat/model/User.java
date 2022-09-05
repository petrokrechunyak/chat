package com.alphabetas.chat.model;

import com.alphabetas.chat.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 4, max = 40, message = "Довжина імені повинна бути більше ніж 4 символи та менше ніж 40")
    @NotBlank
    private String username;
    @Size(min = 5, message = "Довжина пароля повинна бути більше ніж 5 символів")
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.ROLE_USER;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "userid=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
