import {
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ItemImage e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ItemImages', () => {
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ItemImage').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ItemImage page', () => {
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('itemImage');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ItemImage page', () => {
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ItemImage');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ItemImage page', () => {
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ItemImage');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of ItemImage', () => {
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('ItemImage');

    cy.get(`[data-cy="name"]`).type('Vista Salad Hill', { force: true }).invoke('val').should('match', new RegExp('Vista Salad Hill'));


    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');


    cy.get(`[data-cy="createdDate"]`).type('2021-06-07').should('have.value', '2021-06-07');


    cy.get(`[data-cy="lastModifiedDate"]`).type('2021-06-06').should('have.value', '2021-06-06');

    cy.setFieldSelectToLastOfEntity('item');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/item-images*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of ItemImage', () => {
    cy.intercept('GET', '/api/item-images*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/item-images/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-image');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('itemImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/item-images*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('item-image');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
