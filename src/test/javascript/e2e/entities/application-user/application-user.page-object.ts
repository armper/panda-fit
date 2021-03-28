import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import ApplicationUserUpdatePage from './application-user-update.page-object';

const expect = chai.expect;
export class ApplicationUserDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('pandaFitApp.applicationUser.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-applicationUser'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class ApplicationUserComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('application-user-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('application-user');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateApplicationUser() {
    await this.createButton.click();
    return new ApplicationUserUpdatePage();
  }

  async deleteApplicationUser() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const applicationUserDeleteDialog = new ApplicationUserDeleteDialog();
    await waitUntilDisplayed(applicationUserDeleteDialog.deleteModal);
    expect(await applicationUserDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/pandaFitApp.applicationUser.delete.question/);
    await applicationUserDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(applicationUserDeleteDialog.deleteModal);

    expect(await isVisible(applicationUserDeleteDialog.deleteModal)).to.be.false;
  }
}
