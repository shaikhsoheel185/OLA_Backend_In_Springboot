package com.app.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.util.List;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import io.jsonwebtoken.Claims;


public class JwtTokenValidationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt= request.getHeader(JwtSecurityContext.JWT_HEADER);


        if(jwt != null) {

            try {

                //extracting the word Bearer
                jwt = jwt.substring(7);


                SecretKey key= Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());

                Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String username= String.valueOf(claims.get("email"));

                String authorities= (String)claims.get("authorities");

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, auths);


//				List<GrantedAuthority> authorities=(List<GrantedAuthority>)claims.get("authorities");
//				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);


                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received... error");
            }



        }

        filterChain.doFilter(request, response);

    }
}
