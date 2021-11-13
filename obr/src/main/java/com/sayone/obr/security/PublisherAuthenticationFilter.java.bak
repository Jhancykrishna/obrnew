package com.sayone.obr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayone.obr.SpringApplicationContext;
import com.sayone.obr.model.request.PublisherLoginRequestModel;
import com.sayone.obr.service.PublisherService;
import com.sayone.obr.shared.dto.PublisherDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class PublisherAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public PublisherAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            PublisherLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), PublisherLoginRequestModel.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {

        String publisherName = ((User)auth.getPrincipal()).getUsername();

        String token = Jwts.builder().setSubject(publisherName).setExpiration(new Date(System.currentTimeMillis() + PublisherSecurityConstants.EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS512, PublisherSecurityConstants.getTokenSecret()).compact();

        PublisherService publisherService = (PublisherService) SpringApplicationContext.getBean("publisherServiceImpl");
        PublisherDto publisherDto = publisherService.getPublisher(publisherName);

        res.addHeader(PublisherSecurityConstants.HEADER_STRING, PublisherSecurityConstants.TOKEN_PREFIX + token);
        res.addHeader("PublisherID", publisherDto.getPublisherId());
    }
}
