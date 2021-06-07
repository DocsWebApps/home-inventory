package com.docswebapps.jh.homeinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.docswebapps.jh.homeinventory.IntegrationTest;
import com.docswebapps.jh.homeinventory.domain.ItemOwner;
import com.docswebapps.jh.homeinventory.repository.ItemOwnerRepository;
import com.docswebapps.jh.homeinventory.service.dto.ItemOwnerDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemOwnerMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ItemOwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemOwnerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/item-owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemOwnerRepository itemOwnerRepository;

    @Autowired
    private ItemOwnerMapper itemOwnerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemOwnerMockMvc;

    private ItemOwner itemOwner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemOwner createEntity(EntityManager em) {
        ItemOwner itemOwner = new ItemOwner()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return itemOwner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemOwner createUpdatedEntity(EntityManager em) {
        ItemOwner itemOwner = new ItemOwner()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return itemOwner;
    }

    @BeforeEach
    public void initTest() {
        itemOwner = createEntity(em);
    }

    @Test
    @Transactional
    void createItemOwner() throws Exception {
        int databaseSizeBeforeCreate = itemOwnerRepository.findAll().size();
        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);
        restItemOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeCreate + 1);
        ItemOwner testItemOwner = itemOwnerList.get(itemOwnerList.size() - 1);
        assertThat(testItemOwner.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemOwner.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemOwner.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createItemOwnerWithExistingId() throws Exception {
        // Create the ItemOwner with an existing ID
        itemOwner.setId(1L);
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        int databaseSizeBeforeCreate = itemOwnerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOwnerRepository.findAll().size();
        // set the field null
        itemOwner.setName(null);

        // Create the ItemOwner, which fails.
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        restItemOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemOwners() throws Exception {
        // Initialize the database
        itemOwnerRepository.saveAndFlush(itemOwner);

        // Get all the itemOwnerList
        restItemOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getItemOwner() throws Exception {
        // Initialize the database
        itemOwnerRepository.saveAndFlush(itemOwner);

        // Get the itemOwner
        restItemOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, itemOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemOwner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemOwner() throws Exception {
        // Get the itemOwner
        restItemOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemOwner() throws Exception {
        // Initialize the database
        itemOwnerRepository.saveAndFlush(itemOwner);

        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();

        // Update the itemOwner
        ItemOwner updatedItemOwner = itemOwnerRepository.findById(itemOwner.getId()).get();
        // Disconnect from session so that the updates on updatedItemOwner are not directly saved in db
        em.detach(updatedItemOwner);
        updatedItemOwner.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(updatedItemOwner);

        restItemOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemOwnerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
        ItemOwner testItemOwner = itemOwnerList.get(itemOwnerList.size() - 1);
        assertThat(testItemOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemOwner.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemOwner.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItemOwner() throws Exception {
        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();
        itemOwner.setId(count.incrementAndGet());

        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemOwnerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemOwner() throws Exception {
        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();
        itemOwner.setId(count.incrementAndGet());

        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemOwner() throws Exception {
        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();
        itemOwner.setId(count.incrementAndGet());

        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOwnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemOwnerWithPatch() throws Exception {
        // Initialize the database
        itemOwnerRepository.saveAndFlush(itemOwner);

        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();

        // Update the itemOwner using partial update
        ItemOwner partialUpdatedItemOwner = new ItemOwner();
        partialUpdatedItemOwner.setId(itemOwner.getId());

        partialUpdatedItemOwner.name(UPDATED_NAME);

        restItemOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemOwner))
            )
            .andExpect(status().isOk());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
        ItemOwner testItemOwner = itemOwnerList.get(itemOwnerList.size() - 1);
        assertThat(testItemOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemOwner.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemOwner.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemOwnerWithPatch() throws Exception {
        // Initialize the database
        itemOwnerRepository.saveAndFlush(itemOwner);

        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();

        // Update the itemOwner using partial update
        ItemOwner partialUpdatedItemOwner = new ItemOwner();
        partialUpdatedItemOwner.setId(itemOwner.getId());

        partialUpdatedItemOwner.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemOwner))
            )
            .andExpect(status().isOk());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
        ItemOwner testItemOwner = itemOwnerList.get(itemOwnerList.size() - 1);
        assertThat(testItemOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemOwner.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemOwner.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItemOwner() throws Exception {
        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();
        itemOwner.setId(count.incrementAndGet());

        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemOwnerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemOwner() throws Exception {
        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();
        itemOwner.setId(count.incrementAndGet());

        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemOwner() throws Exception {
        int databaseSizeBeforeUpdate = itemOwnerRepository.findAll().size();
        itemOwner.setId(count.incrementAndGet());

        // Create the ItemOwner
        ItemOwnerDTO itemOwnerDTO = itemOwnerMapper.toDto(itemOwner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemOwnerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemOwner in the database
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemOwner() throws Exception {
        // Initialize the database
        itemOwnerRepository.saveAndFlush(itemOwner);

        int databaseSizeBeforeDelete = itemOwnerRepository.findAll().size();

        // Delete the itemOwner
        restItemOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemOwner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemOwner> itemOwnerList = itemOwnerRepository.findAll();
        assertThat(itemOwnerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
