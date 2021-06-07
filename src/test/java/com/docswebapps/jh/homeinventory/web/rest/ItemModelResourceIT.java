package com.docswebapps.jh.homeinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.docswebapps.jh.homeinventory.IntegrationTest;
import com.docswebapps.jh.homeinventory.domain.ItemMake;
import com.docswebapps.jh.homeinventory.domain.ItemModel;
import com.docswebapps.jh.homeinventory.repository.ItemModelRepository;
import com.docswebapps.jh.homeinventory.service.dto.ItemModelDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemModelMapper;
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
 * Integration tests for the {@link ItemModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/item-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemModelRepository itemModelRepository;

    @Autowired
    private ItemModelMapper itemModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemModelMockMvc;

    private ItemModel itemModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemModel createEntity(EntityManager em) {
        ItemModel itemModel = new ItemModel()
            .name(DEFAULT_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        ItemMake itemMake;
        if (TestUtil.findAll(em, ItemMake.class).isEmpty()) {
            itemMake = ItemMakeResourceIT.createEntity(em);
            em.persist(itemMake);
            em.flush();
        } else {
            itemMake = TestUtil.findAll(em, ItemMake.class).get(0);
        }
        itemModel.setItemMake(itemMake);
        return itemModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemModel createUpdatedEntity(EntityManager em) {
        ItemModel itemModel = new ItemModel()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        ItemMake itemMake;
        if (TestUtil.findAll(em, ItemMake.class).isEmpty()) {
            itemMake = ItemMakeResourceIT.createUpdatedEntity(em);
            em.persist(itemMake);
            em.flush();
        } else {
            itemMake = TestUtil.findAll(em, ItemMake.class).get(0);
        }
        itemModel.setItemMake(itemMake);
        return itemModel;
    }

    @BeforeEach
    public void initTest() {
        itemModel = createEntity(em);
    }

    @Test
    @Transactional
    void createItemModel() throws Exception {
        int databaseSizeBeforeCreate = itemModelRepository.findAll().size();
        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);
        restItemModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModelDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeCreate + 1);
        ItemModel testItemModel = itemModelList.get(itemModelList.size() - 1);
        assertThat(testItemModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemModel.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemModel.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createItemModelWithExistingId() throws Exception {
        // Create the ItemModel with an existing ID
        itemModel.setId(1L);
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        int databaseSizeBeforeCreate = itemModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemModelRepository.findAll().size();
        // set the field null
        itemModel.setName(null);

        // Create the ItemModel, which fails.
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        restItemModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModelDTO)))
            .andExpect(status().isBadRequest());

        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemModels() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        // Get all the itemModelList
        restItemModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getItemModel() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        // Get the itemModel
        restItemModelMockMvc
            .perform(get(ENTITY_API_URL_ID, itemModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemModel() throws Exception {
        // Get the itemModel
        restItemModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemModel() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();

        // Update the itemModel
        ItemModel updatedItemModel = itemModelRepository.findById(itemModel.getId()).get();
        // Disconnect from session so that the updates on updatedItemModel are not directly saved in db
        em.detach(updatedItemModel);
        updatedItemModel.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(updatedItemModel);

        restItemModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
        ItemModel testItemModel = itemModelList.get(itemModelList.size() - 1);
        assertThat(testItemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemModel.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemModel.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItemModel() throws Exception {
        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();
        itemModel.setId(count.incrementAndGet());

        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemModel() throws Exception {
        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();
        itemModel.setId(count.incrementAndGet());

        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemModel() throws Exception {
        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();
        itemModel.setId(count.incrementAndGet());

        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemModelWithPatch() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();

        // Update the itemModel using partial update
        ItemModel partialUpdatedItemModel = new ItemModel();
        partialUpdatedItemModel.setId(itemModel.getId());

        partialUpdatedItemModel.name(UPDATED_NAME);

        restItemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemModel))
            )
            .andExpect(status().isOk());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
        ItemModel testItemModel = itemModelList.get(itemModelList.size() - 1);
        assertThat(testItemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemModel.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemModel.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemModelWithPatch() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();

        // Update the itemModel using partial update
        ItemModel partialUpdatedItemModel = new ItemModel();
        partialUpdatedItemModel.setId(itemModel.getId());

        partialUpdatedItemModel.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemModel))
            )
            .andExpect(status().isOk());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
        ItemModel testItemModel = itemModelList.get(itemModelList.size() - 1);
        assertThat(testItemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemModel.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemModel.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItemModel() throws Exception {
        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();
        itemModel.setId(count.incrementAndGet());

        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemModel() throws Exception {
        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();
        itemModel.setId(count.incrementAndGet());

        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemModel() throws Exception {
        int databaseSizeBeforeUpdate = itemModelRepository.findAll().size();
        itemModel.setId(count.incrementAndGet());

        // Create the ItemModel
        ItemModelDTO itemModelDTO = itemModelMapper.toDto(itemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemModel in the database
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemModel() throws Exception {
        // Initialize the database
        itemModelRepository.saveAndFlush(itemModel);

        int databaseSizeBeforeDelete = itemModelRepository.findAll().size();

        // Delete the itemModel
        restItemModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemModel> itemModelList = itemModelRepository.findAll();
        assertThat(itemModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
