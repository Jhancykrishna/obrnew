package com.sayone.obr.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class PublisherAuthorizationFilter extends BasicAuthenticationFilter {

    public PublisherAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(PublisherSecurityConstants.HEADER_STRING);
        if(header == null || !header.startsWith(PublisherSecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(PublisherSecurityConstants.HEADER_STRING);

        if(token != null) {
            token = token.replace(PublisherSecurityConstants.TOKEN_PREFIX,"");
            String publisher = Jwts.parser().setSigningKey(PublisherSecurityConstants.getTokenSecret()).parseClaimsJws(token).getBody().getSubject();

            if(publisher != null) {
                return new UsernamePasswordAuthenticationToken(publisher,null,new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
