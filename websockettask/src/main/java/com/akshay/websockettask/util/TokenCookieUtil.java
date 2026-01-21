package com.akshay.websockettask.util;

import com.akshay.websockettask.Exceptions.RefreshTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenCookieUtil {


    @Value("${auth.refresh.cookie.path:/auth}")
    private String cookiePath;

    @Value("${auth.refresh.cookie.max-age}")
    private int maxAge;

    @Value("${auth.refresh.cookie.secure:true}")
    private boolean secure;

    // add token inside cookie
    public void add(HttpServletResponse response, String token,String cookieName) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    // clear token from cookie
    public void clear(HttpServletResponse response,String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);

        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    // extract token from cookie
    public String extract(HttpServletRequest request,String cookieName) {
        if (request.getCookies() == null) {
            throw new RefreshTokenException("Refresh token cookie missing");
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RefreshTokenException("Refresh token cookie not found");
    }
}
