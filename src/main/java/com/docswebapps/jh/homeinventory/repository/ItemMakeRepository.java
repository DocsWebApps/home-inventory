package com.docswebapps.jh.homeinventory.repository;

import com.docswebapps.jh.homeinventory.domain.ItemMake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemMake entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemMakeRepository extends JpaRepository<ItemMake, Long> {}
