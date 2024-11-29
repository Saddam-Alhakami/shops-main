package com.shops.dreamshops.repository;

import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String roleName);

}
