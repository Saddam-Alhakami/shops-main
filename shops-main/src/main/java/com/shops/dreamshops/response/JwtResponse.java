package com.shops.dreamshops.response;

import com.shops.dreamshops.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private  Long id;
    private String token;
    private Collection<String>roles;


}
