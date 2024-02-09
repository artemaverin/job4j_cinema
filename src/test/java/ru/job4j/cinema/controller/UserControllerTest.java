package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegistrationPageThenGetPage() {
        var model = new ConcurrentModel();
        var view = userController.getRegistrationPage(model);
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRequestRegisterThenGetMainPage() {
        var user = new User(1, "email", "name", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var registratedUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/");
        assertThat(registratedUser).isEqualTo(user);
    }

    @Test
    public void whenSomeErrorWhileRegisterThenGetErrorPageWithMessage() {
        when(userService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(model, new User());
        var message = model.getAttribute("error");

        assertThat(view).isEqualTo("users/register");
        assertThat(message).isEqualTo("Пользователь с такой почтой уже существует");
    }

    @Test
    public void whenRequestLoginPageThenGetPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenRequestLoginUserPageThenGetMainPage() {
        var user = new User(1, "name", "email", "password");
        var emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(userService.findByEmailAndPassword(emailArgumentCaptor.capture(), passwordArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var httpServletRequest = new MockHttpServletRequest();
        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, httpServletRequest);

        var emailLogin = emailArgumentCaptor.getValue();
        var passwordLogin = passwordArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/");
        assertThat(emailLogin).isEqualTo("email");
        assertThat(passwordLogin).isEqualTo("password");
    }

    @Test
    public void whenSomeErrorWhileLoginThenGetErrorPageWithMessage() {
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var httpServletRequest = new MockHttpServletRequest();
        var view = userController.loginUser(new User(), model, httpServletRequest);
        var message = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(message).isEqualTo("Почта или пароль введены неверно");
    }

    @Test
    public void whenLogoutThenGetPage() {
        var httpServletRequest = new MockHttpServletRequest();
        var session = httpServletRequest.getSession();
        var view = userController.logout(session);

        assertThat(view).isEqualTo("redirect:/users/login");
    }

}