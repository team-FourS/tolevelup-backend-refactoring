package com.fours.tolevelup.service.user;

import com.fours.tolevelup.controller.response.UserResponse;
import com.fours.tolevelup.model.RankDTO;
import com.fours.tolevelup.model.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    void userJoin(String id,String password,String name,String email);
    String login(String id,String password);
    UserResponse.UserData findUserData(String id);



    void userLevelUp(String id);
    /*
    void userJoin(UserDTO.JoinForm joinForm);
    boolean userLoginCheck(UserDTO.LoginData loginDat);
    UserDTO.UserPersonalInfo findUserPersonalInfo(String id);
    UserDTO.UserPersonalInfo changeUserPersonalInfo(UserDTO.UserPersonalInfo userData);

    UserDTO.UserProfile findUserProfile(String id);
    UserDTO.UserProfile changeUserProfile(UserDTO.UserProfile userProfile);

    UserDTO.UserData findUserData(String id);
    void userDelete(String id);

    String login(String id,String password);

     */
}
