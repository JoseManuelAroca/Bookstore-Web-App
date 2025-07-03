package com.store.util;

import com.store.config.details.ImprovedUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
@Configuration
public class GetContexHolder {

    public ImprovedUserDetails  GetCookieImproved(){
        String userName = "";
        ImprovedUserDetails improvedUserDetails = new ImprovedUserDetails();
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimoa@anonimo.com";
            improvedUserDetails.setUsername(userName);
        }
        else {
            improvedUserDetails = ((ImprovedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        return improvedUserDetails;
    }
}
