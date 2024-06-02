package com.coderdot.repository;

import com.coderdot.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.coderdot.entities.Diag;

public interface DiagRepository extends JpaRepository<Diag, Long> {
    Page<Diag> findAllByCreatedByAndDeletedFalse(Customer customer, Pageable pageable);
}
