package com.scoremanagement.repositories;

import com.scoremanagement.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    boolean existsByUsername(String username);
}
