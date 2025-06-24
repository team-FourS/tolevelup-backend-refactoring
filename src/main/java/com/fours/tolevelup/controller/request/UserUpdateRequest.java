package com.fours.tolevelup.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserUpdateRequest{
    private String password;
    private String name;
    private String email;
    private String intro;

}