package com.docswebapps.jh.homeinventory.web.rest;

import com.docswebapps.jh.homeinventory.repository.ItemMakeRepository;
import com.docswebapps.jh.homeinventory.service.ItemMakeService;
import com.docswebapps.jh.homeinventory.service.dto.ItemMakeDTO;
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
 * REST controller for managing {@link com.docswebapps.jh.homeinventory.domain.ItemMake}.
 */
@RestController
@RequestMapping("/api")
public class ItemMakeResource {

    private final Logger log = LoggerFactory.getLogger(ItemMakeResource.class);

    private static final String ENTITY_NAME = "itemMake";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemMakeService itemMakeService;

    private final ItemMakeRepository itemMakeRepository;

    public ItemMakeResource(ItemMakeService itemMakeService, ItemMakeRepository itemMakeRepository) {
        this.itemMakeService = itemMakeService;
        this.itemMakeRepository = itemMakeRepository;
    }

    /**
     * {@code POST  /item-makes} : Create a new itemMake.
     *
     * @param itemMakeDTO the itemMakeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemMakeDTO, or with status {@code 400 (Bad Request)} if the itemMake has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-makes")
    public ResponseEntity<ItemMakeDTO> createItemMake(@Valid @RequestBody ItemMakeDTO itemMakeDTO) throws URISyntaxException {
        log.debug("REST request to save ItemMake : {}", itemMakeDTO);
        if (itemMakeDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemMake cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemMakeDTO result = itemMakeService.save(itemMakeDTO);
        return ResponseEntity
            .created(new URI("/api/item-makes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-makes/:id} : Updates an existing itemMake.
     *
     * @param id the id of the itemMakeDTO to save.
     * @param itemMakeDTO the itemMakeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemMakeDTO,
     * or with status {@code 400 (Bad Request)} if the itemMakeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemMakeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-makes/{id}")
    public ResponseEntity<ItemMakeDTO> updateItemMake(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemMakeDTO itemMakeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemMake : {}, {}", id, itemMakeDTO);
        if (itemMakeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemMakeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemMakeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemMakeDTO result = itemMakeService.save(itemMakeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemMakeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-makes/:id} : Partial updates given fields of an existing itemMake, field will ignore if it is null
     *
     * @param id the id of the itemMakeDTO to save.
     * @param itemMakeDTO the itemMakeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemMakeDTO,
     * or with status {@code 400 (Bad Request)} if the itemMakeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemMakeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemMakeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-makes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemMakeDTO> partialUpdateItemMake(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemMakeDTO itemMakeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemMake partially : {}, {}", id, itemMakeDTO);
        if (itemMakeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemMakeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemMakeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemMakeDTO> result = itemMakeService.partialUpdate(itemMakeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemMakeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-makes} : get all the itemMakes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemMakes in body.
     */
    @GetMapping("/item-makes")
    public ResponseEntity<List<ItemMakeDTO>> getAllItemMakes(Pageable pageable) {
        log.debug("REST request to get a page of ItemMakes");
        Page<ItemMakeDTO> page = itemMakeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-makes/:id} : get the "id" itemMake.
     *
     * @param id the id of the itemMakeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemMakeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-makes/{id}")
    public ResponseEntity<ItemMakeDTO> getItemMake(@PathVariable Long id) {
        log.debug("REST request to get ItemMake : {}", id);
        Optional<ItemMakeDTO> itemMakeDTO = itemMakeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemMakeDTO);
    }

    /**
     * {@code DELETE  /item-makes/:id} : delete the "id" itemMake.
     *
     * @param id the id of the itemMakeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-makes/{id}")
    public ResponseEntity<Void> deleteItemMake(@PathVariable Long id) {
        log.debug("REST request to delete ItemMake : {}", id);
        itemMakeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
