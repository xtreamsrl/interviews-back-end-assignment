package it.ms.backendassignment.businesslogic;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.LoginResponseDto;
import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.dto.UserSignUpDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.repository.UserRepository;
import it.ms.backendassignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void nukeDb() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws BAException {
        UserSignUpDto userInput = new UserSignUpDto();

        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        User user = userService.createUser(userInput);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void shouldntCreateUserIfBadPasswords() {
        UserSignUpDto userInput = new UserSignUpDto();
        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("54321");

        assertThatThrownBy(() -> userService.createUser(userInput))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.PASSWORD_DO_NOT_MATCH);
    }

    @Test
    void shouldntCreateUserIfAlreadyPresent() throws BAException {
        UserSignUpDto userInput = new UserSignUpDto();
        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        userService.createUser(userInput);

        UserSignUpDto user2 = new UserSignUpDto();

        user2.setUsername("Carlo");
        user2.setPassword("1234");
        user2.setRepeatPassword("1234");

        assertThatThrownBy(() -> userService.createUser(user2))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.USERNAME_ALREADY_PRESENT);
    }

    @Test
    void shouldntCreateUserIfBadInput() {
        UserSignUpDto userInput = new UserSignUpDto();
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        assertThatThrownBy(() -> userService.createUser(userInput))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.BAD_USER_LOGIN_INPUT);
    }

    @Test
    void shouldAuthenticateUser() throws BAException {
        UserSignUpDto userInput = new UserSignUpDto();

        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        User user = userService.createUser(userInput);

        UserDto loginUser = new UserDto();
        loginUser.setUsername("Carlo");
        loginUser.setPassword("1234");

        LoginResponseDto responseDto = userService.loginUser(loginUser);

        assertThat(responseDto.getSuccess()).isTrue();
        assertThat(responseDto.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void shouldAuthenticateUserIfWrongPassword() throws BAException {
        UserSignUpDto userInput = new UserSignUpDto();

        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        User user = userService.createUser(userInput);

        UserDto loginUser = new UserDto();
        loginUser.setUsername("Carlo");
        loginUser.setPassword("ciao");

        assertThatCode(() -> userService.loginUser(loginUser))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.UNAUTHORIZED);
    }

    @Test
    void shouldAuthenticateUserIfBadInput() throws BAException {
        UserSignUpDto userInput = new UserSignUpDto();

        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        User user = userService.createUser(userInput);

        UserDto loginUser = new UserDto();
        loginUser.setUsername("Carlo");

        assertThatCode(() -> userService.loginUser(loginUser))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.BAD_USER_LOGIN_INPUT);
    }

}
