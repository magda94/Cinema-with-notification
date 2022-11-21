package com.user.service.entity;

import com.user.service.dtos.UserDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "userTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String uuid;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;


    public UserDto toDto() {
        return UserDto.builder()
                .login(this.getLogin())
                .name(this.getName())
                .lastName(this.getLastName())
                .build();
    }

}
