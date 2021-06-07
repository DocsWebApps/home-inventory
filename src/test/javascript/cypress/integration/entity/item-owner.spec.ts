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

describe('ItemOwner e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load ItemOwners', () => {
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('ItemOwner').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details ItemOwner page', () => {
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('itemOwner');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create ItemOwner page', () => {
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ItemOwner');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit ItemOwner page', () => {
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('ItemOwner');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of ItemOwner', () => {
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ItemOwner');

    cy.get(`[data-cy="name"]`).type('Automated alliance', { force: true }).invoke('val').should('match', new RegExp('Automated alliance'));

    cy.get(`[data-cy="createdDate"]`).type('2021-06-06').should('have.value', '2021-06-06');

    cy.get(`[data-cy="lastModifiedDate"]`).type('2021-06-07').should('have.value', '2021-06-07');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of ItemOwner', () => {
    cy.intercept('GET', '/api/item-owners*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/item-owners/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('item-owner');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('itemOwner').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/item-owners*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('item-owner');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
