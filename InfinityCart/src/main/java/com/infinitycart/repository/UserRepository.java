package com.infinitycart.repository;

import com.infinitycart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{

    User findByEmail(String email);

}
