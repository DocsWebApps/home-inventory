import {
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ItemModel e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ItemModels', () => {
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ItemModel').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ItemModel page', () => {
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('itemModel');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ItemModel page', () => {
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ItemModel');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ItemModel page', () => {
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ItemModel');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of ItemModel', () => {
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('ItemModel');

    cy.get(`[data-cy="name"]`).type('Forge Tasty', { force: true }).invoke('val').should('match', new RegExp('Forge Tasty'));


    cy.get(`[data-cy="createdDate"]`).type('2021-06-06').should('have.value', '2021-06-06');


    cy.get(`[data-cy="lastModifiedDate"]`).type('2021-06-07').should('have.value', '2021-06-07');

    cy.setFieldSelectToLastOfEntity('itemMake');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/item-models*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of ItemModel', () => {
    cy.intercept('GET', '/api/item-models*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/item-models/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-model');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('itemModel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/item-models*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('item-model');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
