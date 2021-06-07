package com.docswebapps.jh.homeinventory.web.rest;

import com.docswebapps.jh.homeinventory.repository.ItemOwnerRepository;
import com.docswebapps.jh.homeinventory.service.ItemOwnerService;
import com.docswebapps.jh.homeinventory.service.dto.ItemOwnerDTO;
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
 * REST controller for managing {@link com.docswebapps.jh.homeinventory.domain.ItemOwner}.
 */
@RestController
@RequestMapping("/api")
public class ItemOwnerResource {

    private final Logger log = LoggerFactory.getLogger(ItemOwnerResource.class);

    private static final String ENTITY_NAME = "itemOwner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemOwnerService itemOwnerService;

    private final ItemOwnerRepository itemOwnerRepository;

    public ItemOwnerResource(ItemOwnerService itemOwnerService, ItemOwnerRepository itemOwnerRepository) {
        this.itemOwnerService = itemOwnerService;
        this.itemOwnerRepository = itemOwnerRepository;
    }

    /**
     * {@code POST  /item-owners} : Create a new itemOwner.
     *
     * @param itemOwnerDTO the itemOwnerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemOwnerDTO, or with status {@code 400 (Bad Request)} if the itemOwner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-owners")
    public ResponseEntity<ItemOwnerDTO> createItemOwner(@Valid @RequestBody ItemOwnerDTO itemOwnerDTO) throws URISyntaxException {
        log.debug("REST request to save ItemOwner : {}", itemOwnerDTO);
        if (itemOwnerDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemOwner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemOwnerDTO result = itemOwnerService.save(itemOwnerDTO);
        return ResponseEntity
            .created(new URI("/api/item-owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-owners/:id} : Updates an existing itemOwner.
     *
     * @param id the id of the itemOwnerDTO to save.
     * @param itemOwnerDTO the itemOwnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemOwnerDTO,
     * or with status {@code 400 (Bad Request)} if the itemOwnerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemOwnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-owners/{id}")
    public ResponseEntity<ItemOwnerDTO> updateItemOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemOwnerDTO itemOwnerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemOwner : {}, {}", id, itemOwnerDTO);
        if (itemOwnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemOwnerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemOwnerDTO result = itemOwnerService.save(itemOwnerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemOwnerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-owners/:id} : Partial updates given fields of an existing itemOwner, field will ignore if it is null
     *
     * @param id the id of the itemOwnerDTO to save.
     * @param itemOwnerDTO the itemOwnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemOwnerDTO,
     * or with status {@code 400 (Bad Request)} if the itemOwnerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemOwnerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemOwnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-owners/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemOwnerDTO> partialUpdateItemOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemOwnerDTO itemOwnerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemOwner partially : {}, {}", id, itemOwnerDTO);
        if (itemOwnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemOwnerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemOwnerDTO> result = itemOwnerService.partialUpdate(itemOwnerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemOwnerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-owners} : get all the itemOwners.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemOwners in body.
     */
    @GetMapping("/item-owners")
    public ResponseEntity<List<ItemOwnerDTO>> getAllItemOwners(Pageable pageable) {
        log.debug("REST request to get a page of ItemOwners");
        Page<ItemOwnerDTO> page = itemOwnerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-owners/:id} : get the "id" itemOwner.
     *
     * @param id the id of the itemOwnerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemOwnerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-owners/{id}")
    public ResponseEntity<ItemOwnerDTO> getItemOwner(@PathVariable Long id) {
        log.debug("REST request to get ItemOwner : {}", id);
        Optional<ItemOwnerDTO> itemOwnerDTO = itemOwnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemOwnerDTO);
    }

    /**
     * {@code DELETE  /item-owners/:id} : delete the "id" itemOwner.
     *
     * @param id the id of the itemOwnerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-owners/{id}")
    public ResponseEntity<Void> deleteItemOwner(@PathVariable Long id) {
        log.debug("REST request to delete ItemOwner : {}", id);
        itemOwnerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
