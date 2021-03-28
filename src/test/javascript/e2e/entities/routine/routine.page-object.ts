import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import RoutineUpdatePage from './routine-update.page-object';

const expect = chai.expect;
export class RoutineDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('pandaFitApp.routine.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-routine'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class RoutineComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('routine-heading'));
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
    await navBarPage.getEntityPage('routine');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateRoutine() {
    await this.createButton.click();
    return new RoutineUpdatePage();
  }

  async deleteRoutine() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const routineDeleteDialog = new RoutineDeleteDialog();
    await waitUntilDisplayed(routineDeleteDialog.deleteModal);
    expect(await routineDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/pandaFitApp.routine.delete.question/);
    await routineDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(routineDeleteDialog.deleteModal);

    expect(await isVisible(routineDeleteDialog.deleteModal)).to.be.false;
  }
}
