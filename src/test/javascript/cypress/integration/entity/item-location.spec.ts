import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ItemLocation e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ItemLocations', () => {
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ItemLocation').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ItemLocation page', () => {
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('itemLocation');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ItemLocation page', () => {
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ItemLocation');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ItemLocation page', () => {
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ItemLocation');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of ItemLocation', () => {
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ItemLocation');

    cy.get(`[data-cy="name"]`)
      .type('invoice payment Netherlands', { force: true })
      .invoke('val')
      .should('match', new RegExp('invoice payment Netherlands'));

    cy.get(`[data-cy="createdDate"]`).type('2021-06-06').should('have.value', '2021-06-06');

    cy.get(`[data-cy="lastModifiedDate"]`).type('2021-06-06').should('have.value', '2021-06-06');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of ItemLocation', () => {
    cy.intercept('GET', '/api/item-locations*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/item-locations/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-location');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('itemLocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/item-locations*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('item-location');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
