import {
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Item e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Items', () => {
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Item').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Item page', () => {
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('item');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Item page', () => {
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Item');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Item page', () => {
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Item');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Item', () => {
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Item');

    cy.get(`[data-cy="cost"]`).type('46770').should('have.value', '46770');


    cy.get(`[data-cy="isCostEstimate"]`).should('not.be.checked');
    cy.get(`[data-cy="isCostEstimate"]`).click().should('be.checked');

    cy.get(`[data-cy="serialNumber"]`).type('Bedfordshire knowledge haptic', { force: true }).invoke('val').should('match', new RegExp('Bedfordshire knowledge haptic'));


    cy.get(`[data-cy="purchaseDate"]`).type('2021-06-07').should('have.value', '2021-06-07');


    cy.get(`[data-cy="isPurchaseDateEstimate"]`).should('not.be.checked');
    cy.get(`[data-cy="isPurchaseDateEstimate"]`).click().should('be.checked');

    cy.get(`[data-cy="haveReceipt"]`).should('not.be.checked');
    cy.get(`[data-cy="haveReceipt"]`).click().should('be.checked');

    cy.get(`[data-cy="additionalInfo"]`).type('../fake-data/blob/hipster.txt', { force: true }).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));


    cy.get(`[data-cy="createdDate"]`).type('2021-06-06').should('have.value', '2021-06-06');


    cy.get(`[data-cy="lastModifiedDate"]`).type('2021-06-07').should('have.value', '2021-06-07');

    cy.setFieldSelectToLastOfEntity('itemCategory');

    cy.setFieldSelectToLastOfEntity('itemOwner');

    cy.setFieldSelectToLastOfEntity('itemLocation');

    cy.setFieldSelectToLastOfEntity('itemModel');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/items*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Item', () => {
    cy.intercept('GET', '/api/items*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/items/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('item').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/items*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('item');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
