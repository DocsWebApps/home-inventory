package com.docswebapps.jh.homeinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.docswebapps.jh.homeinventory.IntegrationTest;
import com.docswebapps.jh.homeinventory.domain.ItemMake;
import com.docswebapps.jh.homeinventory.repository.ItemMakeRepository;
import com.docswebapps.jh.homeinventory.service.dto.ItemMakeDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemMakeMapper;
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
 * Integration tests for the {@link ItemMakeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemMakeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/item-makes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemMakeRepository itemMakeRepository;

    @Autowired
    private ItemMakeMapper itemMakeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMakeMockMvc;

    private ItemMake itemMake;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemMake createEntity(EntityManager em) {
        ItemMake itemMake = new ItemMake()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return itemMake;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemMake createUpdatedEntity(EntityManager em) {
        ItemMake itemMake = new ItemMake()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return itemMake;
    }

    @BeforeEach
    public void initTest() {
        itemMake = createEntity(em);
    }

    @Test
    @Transactional
    void createItemMake() throws Exception {
        int databaseSizeBeforeCreate = itemMakeRepository.findAll().size();
        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);
        restItemMakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemMakeDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeCreate + 1);
        ItemMake testItemMake = itemMakeList.get(itemMakeList.size() - 1);
        assertThat(testItemMake.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemMake.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemMake.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createItemMakeWithExistingId() throws Exception {
        // Create the ItemMake with an existing ID
        itemMake.setId(1L);
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        int databaseSizeBeforeCreate = itemMakeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemMakeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemMakeRepository.findAll().size();
        // set the field null
        itemMake.setName(null);

        // Create the ItemMake, which fails.
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        restItemMakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemMakeDTO)))
            .andExpect(status().isBadRequest());

        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemMakes() throws Exception {
        // Initialize the database
        itemMakeRepository.saveAndFlush(itemMake);

        // Get all the itemMakeList
        restItemMakeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemMake.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getItemMake() throws Exception {
        // Initialize the database
        itemMakeRepository.saveAndFlush(itemMake);

        // Get the itemMake
        restItemMakeMockMvc
            .perform(get(ENTITY_API_URL_ID, itemMake.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemMake.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemMake() throws Exception {
        // Get the itemMake
        restItemMakeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemMake() throws Exception {
        // Initialize the database
        itemMakeRepository.saveAndFlush(itemMake);

        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();

        // Update the itemMake
        ItemMake updatedItemMake = itemMakeRepository.findById(itemMake.getId()).get();
        // Disconnect from session so that the updates on updatedItemMake are not directly saved in db
        em.detach(updatedItemMake);
        updatedItemMake.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(updatedItemMake);

        restItemMakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemMakeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemMakeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
        ItemMake testItemMake = itemMakeList.get(itemMakeList.size() - 1);
        assertThat(testItemMake.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemMake.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemMake.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItemMake() throws Exception {
        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();
        itemMake.setId(count.incrementAndGet());

        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemMakeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemMakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemMake() throws Exception {
        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();
        itemMake.setId(count.incrementAndGet());

        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemMakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemMake() throws Exception {
        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();
        itemMake.setId(count.incrementAndGet());

        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMakeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemMakeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemMakeWithPatch() throws Exception {
        // Initialize the database
        itemMakeRepository.saveAndFlush(itemMake);

        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();

        // Update the itemMake using partial update
        ItemMake partialUpdatedItemMake = new ItemMake();
        partialUpdatedItemMake.setId(itemMake.getId());

        partialUpdatedItemMake.name(UPDATED_NAME);

        restItemMakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemMake.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemMake))
            )
            .andExpect(status().isOk());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
        ItemMake testItemMake = itemMakeList.get(itemMakeList.size() - 1);
        assertThat(testItemMake.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemMake.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemMake.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemMakeWithPatch() throws Exception {
        // Initialize the database
        itemMakeRepository.saveAndFlush(itemMake);

        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();

        // Update the itemMake using partial update
        ItemMake partialUpdatedItemMake = new ItemMake();
        partialUpdatedItemMake.setId(itemMake.getId());

        partialUpdatedItemMake.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemMakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemMake.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemMake))
            )
            .andExpect(status().isOk());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
        ItemMake testItemMake = itemMakeList.get(itemMakeList.size() - 1);
        assertThat(testItemMake.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemMake.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemMake.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItemMake() throws Exception {
        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();
        itemMake.setId(count.incrementAndGet());

        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemMakeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemMakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemMake() throws Exception {
        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();
        itemMake.setId(count.incrementAndGet());

        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemMakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemMake() throws Exception {
        int databaseSizeBeforeUpdate = itemMakeRepository.findAll().size();
        itemMake.setId(count.incrementAndGet());

        // Create the ItemMake
        ItemMakeDTO itemMakeDTO = itemMakeMapper.toDto(itemMake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMakeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemMakeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemMake in the database
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemMake() throws Exception {
        // Initialize the database
        itemMakeRepository.saveAndFlush(itemMake);

        int databaseSizeBeforeDelete = itemMakeRepository.findAll().size();

        // Delete the itemMake
        restItemMakeMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemMake.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemMake> itemMakeList = itemMakeRepository.findAll();
        assertThat(itemMakeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
