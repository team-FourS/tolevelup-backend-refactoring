package com.fours.tolevelup.exception;

import com.fours.tolevelup.controller.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class CustomJwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(ErrorCode.FORBIDDEN_ACCESS.getStatus().value());
        response.getWriter().write(Response.error(ErrorCode.FORBIDDEN_ACCESS.name()).toStream());
    }
}
