package com.docswebapps.jh.homeinventory.web.rest;

import com.docswebapps.jh.homeinventory.repository.ItemCategoryRepository;
import com.docswebapps.jh.homeinventory.service.ItemCategoryService;
import com.docswebapps.jh.homeinventory.service.dto.ItemCategoryDTO;
import com.docswebapps.jh.homeinventory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.docswebapps.jh.homeinventory.domain.ItemCategory}.
 */
@RestController
@RequestMapping("/api")
public class ItemCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ItemCategoryResource.class);

    private static final String ENTITY_NAME = "itemCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemCategoryService itemCategoryService;

    private final ItemCategoryRepository itemCategoryRepository;

    public ItemCategoryResource(ItemCategoryService itemCategoryService, ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryService = itemCategoryService;
        this.itemCategoryRepository = itemCategoryRepository;
    }

    /**
     * {@code POST  /item-categories} : Create a new itemCategory.
     *
     * @param itemCategoryDTO the itemCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemCategoryDTO, or with status {@code 400 (Bad Request)} if the itemCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-categories")
    public ResponseEntity<ItemCategoryDTO> createItemCategory(@Valid @RequestBody ItemCategoryDTO itemCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemCategory : {}", itemCategoryDTO);
        if (itemCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemCategoryDTO result = itemCategoryService.save(itemCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/item-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-categories/:id} : Updates an existing itemCategory.
     *
     * @param id the id of the itemCategoryDTO to save.
     * @param itemCategoryDTO the itemCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the itemCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-categories/{id}")
    public ResponseEntity<ItemCategoryDTO> updateItemCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemCategoryDTO itemCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemCategory : {}, {}", id, itemCategoryDTO);
        if (itemCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemCategoryDTO result = itemCategoryService.save(itemCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-categories/:id} : Partial updates given fields of an existing itemCategory, field will ignore if it is null
     *
     * @param id the id of the itemCategoryDTO to save.
     * @param itemCategoryDTO the itemCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the itemCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemCategoryDTO> partialUpdateItemCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemCategoryDTO itemCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemCategory partially : {}, {}", id, itemCategoryDTO);
        if (itemCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemCategoryDTO> result = itemCategoryService.partialUpdate(itemCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-categories} : get all the itemCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemCategories in body.
     */
    @GetMapping("/item-categories")
    public ResponseEntity<List<ItemCategoryDTO>> getAllItemCategories(Pageable pageable) {
        log.debug("REST request to get a page of ItemCategories");
        Page<ItemCategoryDTO> page = itemCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-categories/:id} : get the "id" itemCategory.
     *
     * @param id the id of the itemCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-categories/{id}")
    public ResponseEntity<ItemCategoryDTO> getItemCategory(@PathVariable Long id) {
        log.debug("REST request to get ItemCategory : {}", id);
        Optional<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemCategoryDTO);
    }

    /**
     * {@code DELETE  /item-categories/:id} : delete the "id" itemCategory.
     *
     * @param id the id of the itemCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-categories/{id}")
    public ResponseEntity<Void> deleteItemCategory(@PathVariable Long id) {
        log.debug("REST request to delete ItemCategory : {}", id);
        itemCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
