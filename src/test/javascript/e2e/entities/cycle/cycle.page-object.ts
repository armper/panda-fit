import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import CycleUpdatePage from './cycle-update.page-object';

const expect = chai.expect;
export class CycleDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('pandaFitApp.cycle.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-cycle'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class CycleComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('cycle-heading'));
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
    await navBarPage.getEntityPage('cycle');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateCycle() {
    await this.createButton.click();
    return new CycleUpdatePage();
  }

  async deleteCycle() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const cycleDeleteDialog = new CycleDeleteDialog();
    await waitUntilDisplayed(cycleDeleteDialog.deleteModal);
    expect(await cycleDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/pandaFitApp.cycle.delete.question/);
    await cycleDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(cycleDeleteDialog.deleteModal);

    expect(await isVisible(cycleDeleteDialog.deleteModal)).to.be.false;
  }
}
