package com.fours.tolevelup.controller.request;


public record UserUpdateRequest(
        String password,
        String name,
        String email,
        String intro
) {
}
