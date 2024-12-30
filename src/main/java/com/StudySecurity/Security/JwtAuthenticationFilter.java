package com.StudySecurity.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private CustomUserService customUserService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserService customUserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserService = customUserService;
    }

    /*
        doFilterInternal:
        This method processes every HTTP request that passes through the filter.
        It allows you to inspect, modify, or reject the request before it reaches the next filter or controller.
         */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // get string from HTTP Request
        String token = getTokenFromRequest(request);

        // Validate token also check it is empty or not
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

            // get Username from token
            String usernameFromToken = jwtTokenProvider.getUsernameFromToken(token);

            // get the object of user from DB
            UserDetails userDetails = customUserService.loadUserByUsername(usernameFromToken);

            //Creates an authentication object to represent the authenticated user.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());

            /* This line attaches metadata from the HTTP request (e.g., IP address, session ID)
                to the UsernamePasswordAuthenticationToken for better tracking and security context management.

                Attaches additional request details (e.g., IP address, session ID)
                to the authentication object using WebAuthenticationDetailsSource.
             */
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Ensures the user is considered authenticated for the current request and subsequent authorization checks.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Passes the request and response to the next filter in the filter chain.
        filterChain.doFilter(request, response);
    }

    // Get JWT Token from HTTP Request
    private String getTokenFromRequest(HttpServletRequest request){

        // In postman there is header value and it is key value pair
        String bearerToken = request.getHeader("Authorization");

        // In postman JWT Token has two parts bearer and JWT token so we just need the JWT Token so extract it.
        // Bearer  = takes 7 space so get the substring from 7 to onwards.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
