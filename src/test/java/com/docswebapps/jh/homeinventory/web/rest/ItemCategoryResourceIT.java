package com.docswebapps.jh.homeinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.docswebapps.jh.homeinventory.IntegrationTest;
import com.docswebapps.jh.homeinventory.domain.ItemCategory;
import com.docswebapps.jh.homeinventory.repository.ItemCategoryRepository;
import com.docswebapps.jh.homeinventory.service.dto.ItemCategoryDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemCategoryMapper;
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
 * Integration tests for the {@link ItemCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/item-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemCategoryMockMvc;

    private ItemCategory itemCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCategory createEntity(EntityManager em) {
        ItemCategory itemCategory = new ItemCategory()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return itemCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCategory createUpdatedEntity(EntityManager em) {
        ItemCategory itemCategory = new ItemCategory()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return itemCategory;
    }

    @BeforeEach
    public void initTest() {
        itemCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createItemCategory() throws Exception {
        int databaseSizeBeforeCreate = itemCategoryRepository.findAll().size();
        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);
        restItemCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemCategory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemCategory.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createItemCategoryWithExistingId() throws Exception {
        // Create the ItemCategory with an existing ID
        itemCategory.setId(1L);
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        int databaseSizeBeforeCreate = itemCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCategoryRepository.findAll().size();
        // set the field null
        itemCategory.setName(null);

        // Create the ItemCategory, which fails.
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        restItemCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemCategories() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        // Get all the itemCategoryList
        restItemCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        // Get the itemCategory
        restItemCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, itemCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemCategory() throws Exception {
        // Get the itemCategory
        restItemCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Update the itemCategory
        ItemCategory updatedItemCategory = itemCategoryRepository.findById(itemCategory.getId()).get();
        // Disconnect from session so that the updates on updatedItemCategory are not directly saved in db
        em.detach(updatedItemCategory);
        updatedItemCategory.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(updatedItemCategory);

        restItemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemCategory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemCategory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();
        itemCategory.setId(count.incrementAndGet());

        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();
        itemCategory.setId(count.incrementAndGet());

        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();
        itemCategory.setId(count.incrementAndGet());

        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemCategoryWithPatch() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Update the itemCategory using partial update
        ItemCategory partialUpdatedItemCategory = new ItemCategory();
        partialUpdatedItemCategory.setId(itemCategory.getId());

        partialUpdatedItemCategory.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemCategory))
            )
            .andExpect(status().isOk());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemCategory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemCategory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemCategoryWithPatch() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Update the itemCategory using partial update
        ItemCategory partialUpdatedItemCategory = new ItemCategory();
        partialUpdatedItemCategory.setId(itemCategory.getId());

        partialUpdatedItemCategory.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemCategory))
            )
            .andExpect(status().isOk());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemCategory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemCategory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();
        itemCategory.setId(count.incrementAndGet());

        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();
        itemCategory.setId(count.incrementAndGet());

        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();
        itemCategory.setId(count.incrementAndGet());

        // Create the ItemCategory
        ItemCategoryDTO itemCategoryDTO = itemCategoryMapper.toDto(itemCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        int databaseSizeBeforeDelete = itemCategoryRepository.findAll().size();

        // Delete the itemCategory
        restItemCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
