package com.pi.resources;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.dtos.CommandDto;
import com.pi.core_user.ports.in.IUserCommandIn;
import com.pi.core_user.usecases.CaseCreateUser;
import com.pi.core_user.usecases.CaseUpdateUser;
import com.pi.infrasctructure.mongo.UserDaoCommandAdapter;
import com.pi.infrasctructure.mongo.UserDaoQueryAdapter;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCommandAdapterController implements IUserCommandIn {

    private final CaseCreateUser caseCreateUser;
    private final CaseUpdateUser caseUpdateUser;

    public UserCommandAdapterController(
            CaseCreateUser caseCreateUser,
            CaseUpdateUser caseUpdateUser,
            UserDaoCommandAdapter userDaoCommandAdapter,
            UserDaoQueryAdapter userDaoQueryAdapter
    ) {
        this.caseCreateUser = caseCreateUser;
        this.caseUpdateUser = caseUpdateUser;
        this.caseCreateUser.setServices(userDaoCommandAdapter, userDaoQueryAdapter);
        this.caseUpdateUser.setServices(userDaoCommandAdapter, userDaoQueryAdapter);
    }

    @Override
    public ResponseEntity<User> postCreateUserTeacher(String commandType, String anonymousToken, String name, String email, String login, String code, String password) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(anonymousToken)
                .name(name)
                .email(email)
                .login(login)
                .code(code)
                .password(password)
                .build();

        caseCreateUser.setCommandDto(dto);
        return ResponseEntity.status(201).body(caseCreateUser.call());
    }

    @Override
    public ResponseEntity<User> postCreateUserStudent(String commandType, String anonymousToken, String name, String email, String login, String code, String password) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(anonymousToken)
                .name(name)
                .email(email)
                .login(login)
                .code(code)
                .password(password)
                .build();

        caseCreateUser.setCommandDto(dto);
        return ResponseEntity.status(201).body(caseCreateUser.call());
    }

    @Override
    public ResponseEntity<User> putUpdateUser(String commandType, String token, String name, String email, String password, String oldPassword) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(token)
                .name(name)
                .email(email)
                .password(password)
                .oldPassword(oldPassword)
                .build();

        caseUpdateUser.setCommandDto(dto);
        return ResponseEntity.status(201).body(caseUpdateUser.call());
    }
}
