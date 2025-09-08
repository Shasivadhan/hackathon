package com.ecommerce.security;

import com.ecommerce.entity.User;
import com.ecommerce.repo.UserRepository;
import com.ecommerce.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository users;

    /** Returns the Mongo _id of the authenticated user (via JWT). */
    public String id() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated())
            throw new BadRequestException("Unauthenticated");
        String username = auth.getName();  // set by JwtRequestFilter
        User u = users.findByUsername(username);
        if (u == null) throw new BadRequestException("User not found: " + username);
        return u.getId();                  // use DB id for cart.userId etc.
    }

    /** If sometimes you want the username too. */
    public String username() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated())
            throw new BadRequestException("Unauthenticated");
        return auth.getName();
    }
}
