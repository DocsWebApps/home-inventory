package com.docswebapps.jh.homeinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.docswebapps.jh.homeinventory.IntegrationTest;
import com.docswebapps.jh.homeinventory.domain.*;
import com.docswebapps.jh.homeinventory.repository.ItemRepository;
import com.docswebapps.jh.homeinventory.service.dto.ItemDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemMapper;
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
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final Boolean DEFAULT_IS_COST_ESTIMATE = false;
    private static final Boolean UPDATED_IS_COST_ESTIMATE = true;

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_PURCHASE_DATE_ESTIMATE = false;
    private static final Boolean UPDATED_IS_PURCHASE_DATE_ESTIMATE = true;

    private static final Boolean DEFAULT_HAVE_RECEIPT = false;
    private static final Boolean UPDATED_HAVE_RECEIPT = true;

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .cost(DEFAULT_COST)
            .isCostEstimate(DEFAULT_IS_COST_ESTIMATE)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .isPurchaseDateEstimate(DEFAULT_IS_PURCHASE_DATE_ESTIMATE)
            .haveReceipt(DEFAULT_HAVE_RECEIPT)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        ItemCategory itemCategory;
        if (TestUtil.findAll(em, ItemCategory.class).isEmpty()) {
            itemCategory = ItemCategoryResourceIT.createEntity(em);
            em.persist(itemCategory);
            em.flush();
        } else {
            itemCategory = TestUtil.findAll(em, ItemCategory.class).get(0);
        }
        item.setItemCategory(itemCategory);
        // Add required entity
        ItemOwner itemOwner;
        if (TestUtil.findAll(em, ItemOwner.class).isEmpty()) {
            itemOwner = ItemOwnerResourceIT.createEntity(em);
            em.persist(itemOwner);
            em.flush();
        } else {
            itemOwner = TestUtil.findAll(em, ItemOwner.class).get(0);
        }
        item.setItemOwner(itemOwner);
        // Add required entity
        ItemLocation itemLocation;
        if (TestUtil.findAll(em, ItemLocation.class).isEmpty()) {
            itemLocation = ItemLocationResourceIT.createEntity(em);
            em.persist(itemLocation);
            em.flush();
        } else {
            itemLocation = TestUtil.findAll(em, ItemLocation.class).get(0);
        }
        item.setItemLocation(itemLocation);
        // Add required entity
        ItemModel itemModel;
        if (TestUtil.findAll(em, ItemModel.class).isEmpty()) {
            itemModel = ItemModelResourceIT.createEntity(em);
            em.persist(itemModel);
            em.flush();
        } else {
            itemModel = TestUtil.findAll(em, ItemModel.class).get(0);
        }
        item.setItemModel(itemModel);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .cost(UPDATED_COST)
            .isCostEstimate(UPDATED_IS_COST_ESTIMATE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .isPurchaseDateEstimate(UPDATED_IS_PURCHASE_DATE_ESTIMATE)
            .haveReceipt(UPDATED_HAVE_RECEIPT)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        ItemCategory itemCategory;
        if (TestUtil.findAll(em, ItemCategory.class).isEmpty()) {
            itemCategory = ItemCategoryResourceIT.createUpdatedEntity(em);
            em.persist(itemCategory);
            em.flush();
        } else {
            itemCategory = TestUtil.findAll(em, ItemCategory.class).get(0);
        }
        item.setItemCategory(itemCategory);
        // Add required entity
        ItemOwner itemOwner;
        if (TestUtil.findAll(em, ItemOwner.class).isEmpty()) {
            itemOwner = ItemOwnerResourceIT.createUpdatedEntity(em);
            em.persist(itemOwner);
            em.flush();
        } else {
            itemOwner = TestUtil.findAll(em, ItemOwner.class).get(0);
        }
        item.setItemOwner(itemOwner);
        // Add required entity
        ItemLocation itemLocation;
        if (TestUtil.findAll(em, ItemLocation.class).isEmpty()) {
            itemLocation = ItemLocationResourceIT.createUpdatedEntity(em);
            em.persist(itemLocation);
            em.flush();
        } else {
            itemLocation = TestUtil.findAll(em, ItemLocation.class).get(0);
        }
        item.setItemLocation(itemLocation);
        // Add required entity
        ItemModel itemModel;
        if (TestUtil.findAll(em, ItemModel.class).isEmpty()) {
            itemModel = ItemModelResourceIT.createUpdatedEntity(em);
            em.persist(itemModel);
            em.flush();
        } else {
            itemModel = TestUtil.findAll(em, ItemModel.class).get(0);
        }
        item.setItemModel(itemModel);
        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testItem.getIsCostEstimate()).isEqualTo(DEFAULT_IS_COST_ESTIMATE);
        assertThat(testItem.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testItem.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testItem.getIsPurchaseDateEstimate()).isEqualTo(DEFAULT_IS_PURCHASE_DATE_ESTIMATE);
        assertThat(testItem.getHaveReceipt()).isEqualTo(DEFAULT_HAVE_RECEIPT);
        assertThat(testItem.getAdditionalInfo()).isEqualTo(DEFAULT_ADDITIONAL_INFO);
        assertThat(testItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsCostEstimateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setIsCostEstimate(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPurchaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setPurchaseDate(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPurchaseDateEstimateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setIsPurchaseDateEstimate(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHaveReceiptIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setHaveReceipt(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].isCostEstimate").value(hasItem(DEFAULT_IS_COST_ESTIMATE.booleanValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPurchaseDateEstimate").value(hasItem(DEFAULT_IS_PURCHASE_DATE_ESTIMATE.booleanValue())))
            .andExpect(jsonPath("$.[*].haveReceipt").value(hasItem(DEFAULT_HAVE_RECEIPT.booleanValue())))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.isCostEstimate").value(DEFAULT_IS_COST_ESTIMATE.booleanValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.isPurchaseDateEstimate").value(DEFAULT_IS_PURCHASE_DATE_ESTIMATE.booleanValue()))
            .andExpect(jsonPath("$.haveReceipt").value(DEFAULT_HAVE_RECEIPT.booleanValue()))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .cost(UPDATED_COST)
            .isCostEstimate(UPDATED_IS_COST_ESTIMATE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .isPurchaseDateEstimate(UPDATED_IS_PURCHASE_DATE_ESTIMATE)
            .haveReceipt(UPDATED_HAVE_RECEIPT)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testItem.getIsCostEstimate()).isEqualTo(UPDATED_IS_COST_ESTIMATE);
        assertThat(testItem.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testItem.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testItem.getIsPurchaseDateEstimate()).isEqualTo(UPDATED_IS_PURCHASE_DATE_ESTIMATE);
        assertThat(testItem.getHaveReceipt()).isEqualTo(UPDATED_HAVE_RECEIPT);
        assertThat(testItem.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
        assertThat(testItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .cost(UPDATED_COST)
            .isCostEstimate(UPDATED_IS_COST_ESTIMATE)
            .isPurchaseDateEstimate(UPDATED_IS_PURCHASE_DATE_ESTIMATE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testItem.getIsCostEstimate()).isEqualTo(UPDATED_IS_COST_ESTIMATE);
        assertThat(testItem.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testItem.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testItem.getIsPurchaseDateEstimate()).isEqualTo(UPDATED_IS_PURCHASE_DATE_ESTIMATE);
        assertThat(testItem.getHaveReceipt()).isEqualTo(DEFAULT_HAVE_RECEIPT);
        assertThat(testItem.getAdditionalInfo()).isEqualTo(DEFAULT_ADDITIONAL_INFO);
        assertThat(testItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .cost(UPDATED_COST)
            .isCostEstimate(UPDATED_IS_COST_ESTIMATE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .isPurchaseDateEstimate(UPDATED_IS_PURCHASE_DATE_ESTIMATE)
            .haveReceipt(UPDATED_HAVE_RECEIPT)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testItem.getIsCostEstimate()).isEqualTo(UPDATED_IS_COST_ESTIMATE);
        assertThat(testItem.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testItem.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testItem.getIsPurchaseDateEstimate()).isEqualTo(UPDATED_IS_PURCHASE_DATE_ESTIMATE);
        assertThat(testItem.getHaveReceipt()).isEqualTo(UPDATED_HAVE_RECEIPT);
        assertThat(testItem.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
        assertThat(testItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
