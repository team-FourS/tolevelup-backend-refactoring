package com.fours.tolevelup.configuration.filter;


import com.fours.tolevelup.model.UserDTO;
import com.fours.tolevelup.service.user.UserServiceImpl;
import com.fours.tolevelup.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        System.out.println(header);

        if(header == null || !header.startsWith("Bearer")){
            log.error("Error occurs while getting header. header is null or invalid");
            filterChain.doFilter(request,response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();
            if(JwtTokenUtils.isExpired(token,key)){
                log.error("Key is expired");
                System.out.println("1-1");
                filterChain.doFilter(request,response);
                return;
            }
            String userId = JwtTokenUtils.getUserId(token,key);
            UserDTO user = userService.loadUserVoByUserId(userId);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,null,user.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }catch (RuntimeException e){
            System.out.println("R");
            log.error("Error occurs while validating. {}",e.toString());
            filterChain.doFilter(request,response);
            return;
        }

        filterChain.doFilter(request,response);
    }
}
