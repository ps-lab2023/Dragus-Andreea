package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.TestDataBuilder;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import com.SoftwareDesign.BeautySalon.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenUserObject_whenAddValidUser_thenReturnUserObject() {
        User validUser = TestDataBuilder.buildValidUser1();

        given(userRepository.findByUserName(validUser.getUserName()))
                .willReturn(Optional.empty());

        try {
        given(userRepository.saveIfValid(validUser))
                .willReturn(Optional.of(validUser));

        Assertions.assertDoesNotThrow( () -> userService.addUser(validUser));

        then(userRepository)
                .should()
                .saveIfValid(validUser);

        } catch(InvalidUserException exc) {
            fail("Should not throw exception");
        }

    }

    @Test
    public void givenUserObject_whenAddInvalidUser_thenThrowException() throws InvalidUserException, DataBaseFailException {
        User invalidUser = TestDataBuilder.buildInvalidUser();

        given(userRepository.findByUserName(invalidUser.getUserName()))
                .willReturn(Optional.empty());

        willThrow(new InvalidUserException())
                .given(userRepository)
                        . saveIfValid(invalidUser);

        Assertions.assertThrows(InvalidUserException.class, () -> userService.addUser(invalidUser));
    }

    @Test
    public void givenExistingUsername_whenAddUser_thenThrowException() throws InvalidUserException {
        User user = TestDataBuilder.buildValidUser1();

        given(userRepository.findByUserName(user.getUserName()))
                .willReturn(Optional.of(user));

        Assertions.assertThrows(DataBaseFailException.class, () -> userService.addUser(user));

        then(userRepository)
                .should(never())
                .saveIfValid(user);

    }

    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws UserNotFoundException {
        User user = TestDataBuilder.buildValidUser1();
        user.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        AtomicReference<User> foundUser = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundUser.set(userService.getUserById(user.getId())));

        Assertions.assertEquals(foundUser.get(), user);

        then(userRepository)
                .should()
                .findById(user.getId());

    }

    @Test
    public void givenNotExistingUserId_whenGetUserById_thenThrowsException() throws UserNotFoundException {
        given(userRepository.findById(100L)).willReturn(Optional.empty());

        AtomicReference<User> foundUser = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> foundUser.set(userService.getUserById(100L)));

        Assertions.assertNull(foundUser.get());

        then(userRepository)
                .should()
                .findById(100L);

    }

    @Test
    public void givenName_whenGetUserByName_thenReturnUserObject() {
        User user = TestDataBuilder.buildValidUser1();
        user.setId(1L);

        given(userRepository.findByName(user.getName()))
                .willReturn(Optional.of(user));

        AtomicReference<User> foundUser = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundUser.set(userService.getUserByName(user.getName())));

        Assertions.assertEquals(foundUser.get(), user);

        then(userRepository)
                .should()
                .findByName(user.getName());


    }

    @Test
    public void givenNotExistingName_whenGetUserByName_thenThrowException() {
        String notExistingName = "John Doe";

        given(userRepository.findByName(notExistingName))
                .willReturn(Optional.empty());

        AtomicReference<User> foundUser = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> foundUser.set(userService.getUserByName(notExistingName)));

        Assertions.assertNull(foundUser.get());

        then(userRepository)
                .should()
                .findByName(notExistingName);

    }

    @Test
    public void givenUserName_whenGetUserByUserName_thenReturnUserObject() {
        User user = TestDataBuilder.buildValidUser1();
        user.setId(1L);

        given(userRepository.findByUserName(user.getUserName()))
                .willReturn(Optional.of(user));

        AtomicReference<User> foundUser = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundUser.set(userService.getUserByUserName(user.getUserName())));

        Assertions.assertEquals(foundUser.get(), user);

        then(userRepository)
                .should()
                .findByUserName(user.getUserName());

    }

    @Test
    public void givenNotExistingUserName_whenGetUserByUserName_thenThrowException() {
        String notExistingUsername = "johnDoe@domain.com";

        given(userRepository.findByUserName(notExistingUsername))
                .willReturn(Optional.empty());

        AtomicReference<User> foundUser = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> foundUser.set(userService.getUserByUserName(notExistingUsername)));

        Assertions.assertNull(foundUser.get());

        then(userRepository)
                .should()
                .findByUserName(notExistingUsername);
    }

    @Test
    public void givenUserType_whenGetAllUsersByUserType_thenReturnUsersList()  {
        User user1 = TestDataBuilder.buildValidUser1();
        user1.setId(1L);

        User user2 = TestDataBuilder.buildValidUser2();
        user2.setId(2L);

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        given(userRepository.findAllByUserType(UserType.EMPLOYEE))
                .willReturn(List.of(user1, user2));

        AtomicReference<List<User>> foundUsers = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundUsers.set(userService.getAllByUserType(UserType.EMPLOYEE)));

        Assertions.assertEquals(expected, foundUsers.get());

        then(userRepository)
                .should()
                .findAllByUserType(UserType.EMPLOYEE);
    }

    @Test
    public void givenNotExistingUserType_whenGetAllUsersByUserType_thenThrowException()  {
        given(userRepository.findAllByUserType(UserType.CLIENT))
                .willReturn(Collections.emptyList());

        AtomicReference<List<User>> foundUsers = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> foundUsers.set(userService.getAllByUserType(UserType.CLIENT)));

        Assertions.assertTrue(CollectionUtils.isEmpty(foundUsers.get()));

        then(userRepository)
                .should()
                .findAllByUserType(UserType.CLIENT);
    }

    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser() {
        User savedUser = TestDataBuilder.buildValidUser1();
        savedUser.setId(1L);

        given(userRepository.findByUserName(savedUser.getUserName()))
                .willReturn(Optional.empty());

        try {
            given(userRepository.saveIfValid(savedUser))
                    .willReturn(Optional.of(savedUser));

            Assertions.assertDoesNotThrow( () -> userService.addUser(savedUser));

            then(userRepository)
                    .should()
                    .saveIfValid(savedUser);

        } catch(InvalidUserException exc) {
            fail("Should not throw exception at saving");
        }

        try {
            given(userRepository.findById(savedUser.getId()))
                    .willReturn(Optional.of(savedUser));

            User userToBeUpdated = TestDataBuilder.buildValidUserToUpdate();
            userToBeUpdated.setId(savedUser.getId());

            AtomicReference<User> updatedUser = new AtomicReference<>();
            Assertions.assertDoesNotThrow(() -> updatedUser.set(userService.updateUser(userToBeUpdated)));

            Assertions.assertEquals(updatedUser.get(), userToBeUpdated);
            Assertions.assertEquals(updatedUser.get().getId(), savedUser.getId());

            then(userRepository)
                    .should(atLeast(2))
                    .saveIfValid(userToBeUpdated);

        } catch(InvalidUserException exception) {
            fail("Should not throw exception at updating");
        }

    }

    @Test
    public void givenNotExistingUserObject_whenUpdateUser_thenThrowException() throws InvalidUserException {
        long notExistingId = 1L;

        given(userRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        User userToBeUpdated = TestDataBuilder.buildInvalidUserToUpdate();
        userToBeUpdated.setId(notExistingId);

        AtomicReference<User> updatedUser = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> updatedUser.set(userService.updateUser(userToBeUpdated)));

        Assertions.assertNull(updatedUser.get());

        then(userRepository)
                .should(never())
                .saveIfValid(userToBeUpdated);

    }

    @Test
    public void givenUserObject_whenDeleteUserById_thenNothing() {
        User user =TestDataBuilder.buildValidUser1();
        user.setId(1L);

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        doNothing().when(userRepository).deleteById(user.getId());

        Assertions.assertDoesNotThrow(() -> userService.deleteUserById(user.getId()));

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));

        then(userRepository)
                .should()
                .deleteById(user.getId());

    }

    @Test
    public void givenNotExistingId_whenDeleteUserById_thenThrowException() {
        long notExistingId = 100L;

        given(userRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(notExistingId));

        then(userRepository)
                .should(never())
                .deleteById(notExistingId);

    }


}
