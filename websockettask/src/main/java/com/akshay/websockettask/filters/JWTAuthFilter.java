package com.akshay.websockettask.filters;

import com.akshay.websockettask.util.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver resolver; //here there is two different implementation i want handlerExceptionResolver

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String token = extractToken(request);
            String username = extractUsername(token);
            //first check already SecurityContextHolder has principal then we don't need to authenticate
            if (shouldAuthenticate(username)) {
                UserDetails userDetails = loadUser(username);
                if (isTokenValid(token, userDetails)) {
                    setAuthentication(request, userDetails);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (RuntimeException ex){

            resolver.resolveException(request, response, null, ex);
        }
    }

    // get token from authorization header
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // extract username from token
    private String extractUsername(String token) {
        if (token != null) {
            return jwtUtil.extractUsername(token);
        }
        return null;
    }

    // check if authentication is required
    private boolean shouldAuthenticate(String username) {
        return username != null
                && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    // load user from database
    private UserDetails loadUser(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    // validate jwt token
    private boolean isTokenValid(String token, UserDetails userDetails) {
        return jwtUtil.isTokenValid(token, userDetails);
    }

    // set authentication in security context
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
