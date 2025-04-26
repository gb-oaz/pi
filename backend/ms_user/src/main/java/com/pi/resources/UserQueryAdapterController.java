package com.pi.resources;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.dtos.QueryDto;
import com.pi.core_user.ports.in.IUserQueryIn;
import com.pi.core_user.usecases.CaseGetUser;
import com.pi.core_user.usecases.CaseGetUsers;
import com.pi.infrasctructure.mongo.UserDaoQueryAdapter;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserQueryAdapterController implements IUserQueryIn {

    private final CaseGetUser caseGetUser;
    private final CaseGetUsers caseGetUsers;

    public UserQueryAdapterController(
            CaseGetUser caseGetUser,
            CaseGetUsers caseGetUsers,
            UserDaoQueryAdapter userDaoQueryAdapter
    ) {
        this.caseGetUser = caseGetUser;
        this.caseGetUsers = caseGetUsers;
        this.caseGetUsers.setServices(userDaoQueryAdapter);
        this.caseGetUser.setServices(userDaoQueryAdapter);
    }

    @Override
    public ResponseEntity<User> getUserByProjection(String queryType, String authorization, String email, String login, String code) throws GlobalException {
        var dto = QueryDto.builder()
                .queryType(queryType)
                .token(authorization)
                .email(email)
                .login(login)
                .code(code)
                .build();

        caseGetUser.setQueryDto(dto);
        return ResponseEntity.status(200).body(caseGetUser.call());
    }

    @Override
    public ResponseEntity<Pageable<User>> getUsersByProjection(String queryType, String authorization, String name, String email, String login, String code, Integer page, Integer size) throws GlobalException {
        var dto = QueryDto.builder()
                .queryType(queryType)
                .token(authorization)
                .name(name)
                .email(email)
                .login(login)
                .code(code)
                .page(page)
                .size(size)
                .build();

        caseGetUsers.setQueryDto(dto);
        return ResponseEntity.status(200).body(caseGetUsers.call());
    }
}
