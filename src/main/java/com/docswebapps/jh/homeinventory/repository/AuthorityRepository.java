package com.docswebapps.jh.homeinventory.repository;

import com.docswebapps.jh.homeinventory.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
