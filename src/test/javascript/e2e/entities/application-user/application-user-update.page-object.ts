import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class ApplicationUserUpdatePage {
  pageTitle: ElementFinder = element(by.id('pandaFitApp.applicationUser.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  rankingInput: ElementFinder = element(by.css('input#application-user-ranking'));
  internalUserSelect: ElementFinder = element(by.css('select#application-user-internalUser'));
  routineSelect: ElementFinder = element(by.css('select#application-user-routine'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setRankingInput(ranking) {
    await this.rankingInput.sendKeys(ranking);
  }

  async getRankingInput() {
    return this.rankingInput.getAttribute('value');
  }

  async internalUserSelectLastOption() {
    await this.internalUserSelect.all(by.tagName('option')).last().click();
  }

  async internalUserSelectOption(option) {
    await this.internalUserSelect.sendKeys(option);
  }

  getInternalUserSelect() {
    return this.internalUserSelect;
  }

  async getInternalUserSelectedOption() {
    return this.internalUserSelect.element(by.css('option:checked')).getText();
  }

  async routineSelectLastOption() {
    await this.routineSelect.all(by.tagName('option')).last().click();
  }

  async routineSelectOption(option) {
    await this.routineSelect.sendKeys(option);
  }

  getRoutineSelect() {
    return this.routineSelect;
  }

  async getRoutineSelectedOption() {
    return this.routineSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setRankingInput('5');
    expect(await this.getRankingInput()).to.eq('5');
    await this.internalUserSelectLastOption();
    // this.routineSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
