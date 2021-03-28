import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class ExcerciseUpdatePage {
  pageTitle: ElementFinder = element(by.id('pandaFitApp.excercise.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  typeSelect: ElementFinder = element(by.css('select#excercise-type'));
  currentVolumeInput: ElementFinder = element(by.css('input#excercise-currentVolume'));
  startingVolumeInput: ElementFinder = element(by.css('input#excercise-startingVolume'));
  goalVolumeInput: ElementFinder = element(by.css('input#excercise-goalVolume'));
  routineSelect: ElementFinder = element(by.css('select#excercise-routine'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setTypeSelect(type) {
    await this.typeSelect.sendKeys(type);
  }

  async getTypeSelect() {
    return this.typeSelect.element(by.css('option:checked')).getText();
  }

  async typeSelectLastOption() {
    await this.typeSelect.all(by.tagName('option')).last().click();
  }
  async setCurrentVolumeInput(currentVolume) {
    await this.currentVolumeInput.sendKeys(currentVolume);
  }

  async getCurrentVolumeInput() {
    return this.currentVolumeInput.getAttribute('value');
  }

  async setStartingVolumeInput(startingVolume) {
    await this.startingVolumeInput.sendKeys(startingVolume);
  }

  async getStartingVolumeInput() {
    return this.startingVolumeInput.getAttribute('value');
  }

  async setGoalVolumeInput(goalVolume) {
    await this.goalVolumeInput.sendKeys(goalVolume);
  }

  async getGoalVolumeInput() {
    return this.goalVolumeInput.getAttribute('value');
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
    await this.typeSelectLastOption();
    await waitUntilDisplayed(this.saveButton);
    await this.setCurrentVolumeInput('5');
    expect(await this.getCurrentVolumeInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setStartingVolumeInput('5');
    expect(await this.getStartingVolumeInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setGoalVolumeInput('5');
    expect(await this.getGoalVolumeInput()).to.eq('5');
    await this.routineSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
