package com.docswebapps.jh.homeinventory.repository;

import com.docswebapps.jh.homeinventory.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {}
