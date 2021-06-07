package com.docswebapps.jh.homeinventory.web.rest;

import com.docswebapps.jh.homeinventory.repository.ItemModelRepository;
import com.docswebapps.jh.homeinventory.service.ItemModelService;
import com.docswebapps.jh.homeinventory.service.dto.ItemModelDTO;
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
 * REST controller for managing {@link com.docswebapps.jh.homeinventory.domain.ItemModel}.
 */
@RestController
@RequestMapping("/api")
public class ItemModelResource {

    private final Logger log = LoggerFactory.getLogger(ItemModelResource.class);

    private static final String ENTITY_NAME = "itemModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemModelService itemModelService;

    private final ItemModelRepository itemModelRepository;

    public ItemModelResource(ItemModelService itemModelService, ItemModelRepository itemModelRepository) {
        this.itemModelService = itemModelService;
        this.itemModelRepository = itemModelRepository;
    }

    /**
     * {@code POST  /item-models} : Create a new itemModel.
     *
     * @param itemModelDTO the itemModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemModelDTO, or with status {@code 400 (Bad Request)} if the itemModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-models")
    public ResponseEntity<ItemModelDTO> createItemModel(@Valid @RequestBody ItemModelDTO itemModelDTO) throws URISyntaxException {
        log.debug("REST request to save ItemModel : {}", itemModelDTO);
        if (itemModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemModelDTO result = itemModelService.save(itemModelDTO);
        return ResponseEntity
            .created(new URI("/api/item-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-models/:id} : Updates an existing itemModel.
     *
     * @param id the id of the itemModelDTO to save.
     * @param itemModelDTO the itemModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemModelDTO,
     * or with status {@code 400 (Bad Request)} if the itemModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-models/{id}")
    public ResponseEntity<ItemModelDTO> updateItemModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemModelDTO itemModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemModel : {}, {}", id, itemModelDTO);
        if (itemModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemModelDTO result = itemModelService.save(itemModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-models/:id} : Partial updates given fields of an existing itemModel, field will ignore if it is null
     *
     * @param id the id of the itemModelDTO to save.
     * @param itemModelDTO the itemModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemModelDTO,
     * or with status {@code 400 (Bad Request)} if the itemModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-models/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemModelDTO> partialUpdateItemModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemModelDTO itemModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemModel partially : {}, {}", id, itemModelDTO);
        if (itemModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemModelDTO> result = itemModelService.partialUpdate(itemModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-models} : get all the itemModels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemModels in body.
     */
    @GetMapping("/item-models")
    public ResponseEntity<List<ItemModelDTO>> getAllItemModels(Pageable pageable) {
        log.debug("REST request to get a page of ItemModels");
        Page<ItemModelDTO> page = itemModelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-models/:id} : get the "id" itemModel.
     *
     * @param id the id of the itemModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-models/{id}")
    public ResponseEntity<ItemModelDTO> getItemModel(@PathVariable Long id) {
        log.debug("REST request to get ItemModel : {}", id);
        Optional<ItemModelDTO> itemModelDTO = itemModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemModelDTO);
    }

    /**
     * {@code DELETE  /item-models/:id} : delete the "id" itemModel.
     *
     * @param id the id of the itemModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-models/{id}")
    public ResponseEntity<Void> deleteItemModel(@PathVariable Long id) {
        log.debug("REST request to delete ItemModel : {}", id);
        itemModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
