package com.docswebapps.jh.homeinventory.web.rest;

import com.docswebapps.jh.homeinventory.repository.ItemImageRepository;
import com.docswebapps.jh.homeinventory.service.ItemImageService;
import com.docswebapps.jh.homeinventory.service.dto.ItemImageDTO;
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
 * REST controller for managing {@link com.docswebapps.jh.homeinventory.domain.ItemImage}.
 */
@RestController
@RequestMapping("/api")
public class ItemImageResource {

    private final Logger log = LoggerFactory.getLogger(ItemImageResource.class);

    private static final String ENTITY_NAME = "itemImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemImageService itemImageService;

    private final ItemImageRepository itemImageRepository;

    public ItemImageResource(ItemImageService itemImageService, ItemImageRepository itemImageRepository) {
        this.itemImageService = itemImageService;
        this.itemImageRepository = itemImageRepository;
    }

    /**
     * {@code POST  /item-images} : Create a new itemImage.
     *
     * @param itemImageDTO the itemImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemImageDTO, or with status {@code 400 (Bad Request)} if the itemImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-images")
    public ResponseEntity<ItemImageDTO> createItemImage(@Valid @RequestBody ItemImageDTO itemImageDTO) throws URISyntaxException {
        log.debug("REST request to save ItemImage : {}", itemImageDTO);
        if (itemImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemImageDTO result = itemImageService.save(itemImageDTO);
        return ResponseEntity
            .created(new URI("/api/item-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-images/:id} : Updates an existing itemImage.
     *
     * @param id the id of the itemImageDTO to save.
     * @param itemImageDTO the itemImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemImageDTO,
     * or with status {@code 400 (Bad Request)} if the itemImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-images/{id}")
    public ResponseEntity<ItemImageDTO> updateItemImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemImageDTO itemImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemImage : {}, {}", id, itemImageDTO);
        if (itemImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemImageDTO result = itemImageService.save(itemImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-images/:id} : Partial updates given fields of an existing itemImage, field will ignore if it is null
     *
     * @param id the id of the itemImageDTO to save.
     * @param itemImageDTO the itemImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemImageDTO,
     * or with status {@code 400 (Bad Request)} if the itemImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-images/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemImageDTO> partialUpdateItemImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemImageDTO itemImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemImage partially : {}, {}", id, itemImageDTO);
        if (itemImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemImageDTO> result = itemImageService.partialUpdate(itemImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-images} : get all the itemImages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemImages in body.
     */
    @GetMapping("/item-images")
    public ResponseEntity<List<ItemImageDTO>> getAllItemImages(Pageable pageable) {
        log.debug("REST request to get a page of ItemImages");
        Page<ItemImageDTO> page = itemImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-images/:id} : get the "id" itemImage.
     *
     * @param id the id of the itemImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-images/{id}")
    public ResponseEntity<ItemImageDTO> getItemImage(@PathVariable Long id) {
        log.debug("REST request to get ItemImage : {}", id);
        Optional<ItemImageDTO> itemImageDTO = itemImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemImageDTO);
    }

    /**
     * {@code DELETE  /item-images/:id} : delete the "id" itemImage.
     *
     * @param id the id of the itemImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-images/{id}")
    public ResponseEntity<Void> deleteItemImage(@PathVariable Long id) {
        log.debug("REST request to delete ItemImage : {}", id);
        itemImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
