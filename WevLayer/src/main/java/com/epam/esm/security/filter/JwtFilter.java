package com.epam.esm.security.filter;

import com.epam.esm.errors.AnswerMessageJson;
import com.epam.esm.security.jwtProvider.JwtProvider;
import com.epam.esm.service.CustomerUserServiceDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private JwtProvider jwtProvider;
    private CustomerUserServiceDetails userDetailsService;
    private ObjectMapper objectMapper;
    public static final int CODE_AUTHENTICATION_EXCEPTION = 51;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException {
        try {
            Optional<String> token = jwtProvider.resolveToken((HttpServletRequest) req);
            if (token.isPresent() && jwtProvider.validateToken(token.get())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtProvider.getLoginFromToken(token.get()));
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            AnswerMessageJson answerMessageJson = new AnswerMessageJson();
            answerMessageJson.setCode(String.valueOf(CODE_AUTHENTICATION_EXCEPTION));
            answerMessageJson.setStatus(HttpStatus.FORBIDDEN.toString());
            answerMessageJson.setMessage("Access is forbidden");
            ((HttpServletResponse) res).setStatus(HttpStatus.FORBIDDEN.value());
            res.getWriter().write(objectMapper.writeValueAsString(answerMessageJson));
        }
    }
}
