package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.UserDetailsService;
import com.example.demo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private JwtUtil jwtUtil;

  private UserDetailsService userDetailsService;


  // Constructor injection of dependencies
  public JwtFilter(JwtUtil jwtUtil,UserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    System.out.println("Authorization Header: " + authHeader);

    String username = null;
    String token = null;

    System.out.println("Coming jwtFilter request : " + request);
    System.out.println("Coming jwtFilter response : " + response);
    System.out.println("Coming jwtFilter filterChain : " + filterChain);

    // Check if the authorization header contains a Bearer token
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7); // Extract the token
      username = jwtUtil.extractUsername(token); // Extract the username from the token
    }

    // If the username is present and no authentication exists in the context
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // Load user details by username
      var userDetails = userDetailsService.loadUserByUsername(username);

      // If the token is valid
      if (jwtUtil.validateToken(token)) {
        // Create authentication token and set it in the security context
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Continue with the filter chain
    filterChain.doFilter(request, response);
  }
}
