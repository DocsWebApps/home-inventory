package com.docswebapps.jh.homeinventory.repository;

import com.docswebapps.jh.homeinventory.domain.ItemLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemLocationRepository extends JpaRepository<ItemLocation, Long> {}
