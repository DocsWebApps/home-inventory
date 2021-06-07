package com.docswebapps.jh.homeinventory.repository;

import com.docswebapps.jh.homeinventory.domain.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemModelRepository extends JpaRepository<ItemModel, Long> {}
