package com.docswebapps.jh.homeinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.docswebapps.jh.homeinventory.IntegrationTest;
import com.docswebapps.jh.homeinventory.domain.ItemLocation;
import com.docswebapps.jh.homeinventory.repository.ItemLocationRepository;
import com.docswebapps.jh.homeinventory.service.dto.ItemLocationDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemLocationMapper;
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
 * Integration tests for the {@link ItemLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemLocationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/item-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemLocationRepository itemLocationRepository;

    @Autowired
    private ItemLocationMapper itemLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemLocationMockMvc;

    private ItemLocation itemLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemLocation createEntity(EntityManager em) {
        ItemLocation itemLocation = new ItemLocation()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return itemLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemLocation createUpdatedEntity(EntityManager em) {
        ItemLocation itemLocation = new ItemLocation()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return itemLocation;
    }

    @BeforeEach
    public void initTest() {
        itemLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createItemLocation() throws Exception {
        int databaseSizeBeforeCreate = itemLocationRepository.findAll().size();
        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);
        restItemLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeCreate + 1);
        ItemLocation testItemLocation = itemLocationList.get(itemLocationList.size() - 1);
        assertThat(testItemLocation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemLocation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemLocation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createItemLocationWithExistingId() throws Exception {
        // Create the ItemLocation with an existing ID
        itemLocation.setId(1L);
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        int databaseSizeBeforeCreate = itemLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLocationRepository.findAll().size();
        // set the field null
        itemLocation.setName(null);

        // Create the ItemLocation, which fails.
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        restItemLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemLocations() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        // Get all the itemLocationList
        restItemLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getItemLocation() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        // Get the itemLocation
        restItemLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, itemLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemLocation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemLocation() throws Exception {
        // Get the itemLocation
        restItemLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemLocation() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();

        // Update the itemLocation
        ItemLocation updatedItemLocation = itemLocationRepository.findById(itemLocation.getId()).get();
        // Disconnect from session so that the updates on updatedItemLocation are not directly saved in db
        em.detach(updatedItemLocation);
        updatedItemLocation.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(updatedItemLocation);

        restItemLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
        ItemLocation testItemLocation = itemLocationList.get(itemLocationList.size() - 1);
        assertThat(testItemLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemLocation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();
        itemLocation.setId(count.incrementAndGet());

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();
        itemLocation.setId(count.incrementAndGet());

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();
        itemLocation.setId(count.incrementAndGet());

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemLocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemLocationWithPatch() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();

        // Update the itemLocation using partial update
        ItemLocation partialUpdatedItemLocation = new ItemLocation();
        partialUpdatedItemLocation.setId(itemLocation.getId());

        partialUpdatedItemLocation.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        restItemLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemLocation))
            )
            .andExpect(status().isOk());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
        ItemLocation testItemLocation = itemLocationList.get(itemLocationList.size() - 1);
        assertThat(testItemLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemLocation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemLocation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemLocationWithPatch() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();

        // Update the itemLocation using partial update
        ItemLocation partialUpdatedItemLocation = new ItemLocation();
        partialUpdatedItemLocation.setId(itemLocation.getId());

        partialUpdatedItemLocation.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemLocation))
            )
            .andExpect(status().isOk());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
        ItemLocation testItemLocation = itemLocationList.get(itemLocationList.size() - 1);
        assertThat(testItemLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemLocation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();
        itemLocation.setId(count.incrementAndGet());

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();
        itemLocation.setId(count.incrementAndGet());

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemLocation() throws Exception {
        int databaseSizeBeforeUpdate = itemLocationRepository.findAll().size();
        itemLocation.setId(count.incrementAndGet());

        // Create the ItemLocation
        ItemLocationDTO itemLocationDTO = itemLocationMapper.toDto(itemLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemLocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemLocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemLocation in the database
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemLocation() throws Exception {
        // Initialize the database
        itemLocationRepository.saveAndFlush(itemLocation);

        int databaseSizeBeforeDelete = itemLocationRepository.findAll().size();

        // Delete the itemLocation
        restItemLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemLocation> itemLocationList = itemLocationRepository.findAll();
        assertThat(itemLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
