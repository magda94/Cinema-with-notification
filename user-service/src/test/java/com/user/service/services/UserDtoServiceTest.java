package com.user.service.services;

import autofixture.publicinterface.Any;
import com.user.service.container.PostgresqlContainer;
import com.user.service.exceptions.UserExistException;
import com.user.service.exceptions.UserNotFoundException;
import com.user.service.repository.UserEntityRepository;
import com.user.service.utils.UserDtoUtils;
import com.user.service.utils.UserEntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserDtoServiceTest extends PostgresqlContainer {

    @Autowired
    private UserEntityRepository userEntityRepository;

    private UserDtoService userDtoService;

    @BeforeEach
    public void before() {
        userEntityRepository.deleteAll();
        userDtoService = new UserDtoService(userEntityRepository);
    }

    @Test
    public void shouldReturnUserWithLogin() {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        //WHEN
        var result = userDtoService.getUserWithLogin(userEntity.getLogin());

        //THEN
        assertThat(result).isEqualTo(userEntity.toDto());
    }

    @Test
    public void shouldThrowExceptionWhenUserNotExist() {
        //WHEN
        Throwable throwable = catchThrowable(() -> userDtoService.getUserWithLogin(Any.string()));

        //THEN
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void shouldAddNewUser() {
        //GIVEN
        var user = UserDtoUtils.createUserDto();

        //WHEN
        var result = userDtoService.createUser(user);

        //THEN
        assertThat(result).isEqualTo(user);
        var entity = userEntityRepository.findByLogin(user.getLogin());
        assertThat(entity).isNotEmpty();
        assertThat(entity.get().toDto()).isEqualTo(user);
    }

    @Test
    public void shouldNotAddUserWhereLoginOccupied() {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var user = UserDtoUtils.createUserDtoWithLogin(userEntity.getLogin());

        //WHEN
        Throwable throwable = catchThrowable(() -> userDtoService.createUser(user));

        //THEN
        assertThat(throwable).isInstanceOf(UserExistException.class);
    }

    public void shouldUpdateUser() {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var user = UserDtoUtils.createUserDto();

        //WHEN
        var result = userDtoService.updateUser(userEntity.getLogin(), user);

        //THEN
        assertThat(user).isEqualTo(result);
        var oldEntity = userEntityRepository.findByLogin(userEntity.getLogin());
        var newEntity = userEntityRepository.findByLogin(user.getLogin());

        assertThat(oldEntity).isEmpty();
        assertThat(newEntity).isNotEmpty();
        assertThat(newEntity.get().toDto()).isEqualTo(user);
    }

    @Test
    public void shouldUpdateWithTheSameLogin() {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var user = UserDtoUtils.createUserDtoWithLogin(userEntity.getLogin());

        //WHEN
        var result = userDtoService.updateUser(userEntity.getLogin(), user);

        //THEN
        assertThat(user).isEqualTo(result);
        var entity = userEntityRepository.findByLogin(userEntity.getLogin());

        assertThat(entity).isNotEmpty();
        assertThat(entity.get().toDto()).isEqualTo(user);
    }

    @Test
    public void shouldNotUpdateWhenNewLoginExist() {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        var userEntity2 = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity2);

        var user = UserDtoUtils.createUserDtoWithLogin(userEntity2.getLogin());

        //WHEN
        var throwable = catchThrowable(() -> userDtoService.updateUser(userEntity.getLogin(), user));

        //THEN
        assertThat(throwable).isInstanceOf(UserExistException.class);
    }

    @Test
    public void shouldDeleteUser() {
        //GIVEN
        var userEntity = UserEntityUtils.createUserEntity();
        userEntityRepository.save(userEntity);

        //WHEN
        userDtoService.deleteUser(userEntity.getLogin());

        //THEN
        assertThat(userEntityRepository.count()).isEqualTo(0);
    }

    @Test
    public void shouldThrowExceptionWhenLoginNotExist() {
        //WHEN
        var throwable = catchThrowable(() -> userDtoService.deleteUser(Any.string()));

        //THEN
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }
}