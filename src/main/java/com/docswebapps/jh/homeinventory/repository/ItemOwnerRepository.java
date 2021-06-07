package com.docswebapps.jh.homeinventory.repository;

import com.docswebapps.jh.homeinventory.domain.ItemOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemOwner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemOwnerRepository extends JpaRepository<ItemOwner, Long> {}
