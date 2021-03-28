import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class CycleUpdatePage {
  pageTitle: ElementFinder = element(by.id('pandaFitApp.cycle.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  repsInput: ElementFinder = element(by.css('input#cycle-reps'));
  volumeInput: ElementFinder = element(by.css('input#cycle-volume'));
  excerciseSelect: ElementFinder = element(by.css('select#cycle-excercise'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setRepsInput(reps) {
    await this.repsInput.sendKeys(reps);
  }

  async getRepsInput() {
    return this.repsInput.getAttribute('value');
  }

  async setVolumeInput(volume) {
    await this.volumeInput.sendKeys(volume);
  }

  async getVolumeInput() {
    return this.volumeInput.getAttribute('value');
  }

  async excerciseSelectLastOption() {
    await this.excerciseSelect.all(by.tagName('option')).last().click();
  }

  async excerciseSelectOption(option) {
    await this.excerciseSelect.sendKeys(option);
  }

  getExcerciseSelect() {
    return this.excerciseSelect;
  }

  async getExcerciseSelectedOption() {
    return this.excerciseSelect.element(by.css('option:checked')).getText();
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
    await this.setRepsInput('5');
    expect(await this.getRepsInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setVolumeInput('5');
    expect(await this.getVolumeInput()).to.eq('5');
    await this.excerciseSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
