import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class RoutineUpdatePage {
  pageTitle: ElementFinder = element(by.id('pandaFitApp.routine.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#routine-name'));
  dateStartedInput: ElementFinder = element(by.css('input#routine-dateStarted'));
  dateEndedInput: ElementFinder = element(by.css('input#routine-dateEnded'));
  goalDateInput: ElementFinder = element(by.css('input#routine-goalDate'));
  startingBodyWeightInput: ElementFinder = element(by.css('input#routine-startingBodyWeight'));
  endingBodyWeightInput: ElementFinder = element(by.css('input#routine-endingBodyWeight'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setDateStartedInput(dateStarted) {
    await this.dateStartedInput.sendKeys(dateStarted);
  }

  async getDateStartedInput() {
    return this.dateStartedInput.getAttribute('value');
  }

  async setDateEndedInput(dateEnded) {
    await this.dateEndedInput.sendKeys(dateEnded);
  }

  async getDateEndedInput() {
    return this.dateEndedInput.getAttribute('value');
  }

  async setGoalDateInput(goalDate) {
    await this.goalDateInput.sendKeys(goalDate);
  }

  async getGoalDateInput() {
    return this.goalDateInput.getAttribute('value');
  }

  async setStartingBodyWeightInput(startingBodyWeight) {
    await this.startingBodyWeightInput.sendKeys(startingBodyWeight);
  }

  async getStartingBodyWeightInput() {
    return this.startingBodyWeightInput.getAttribute('value');
  }

  async setEndingBodyWeightInput(endingBodyWeight) {
    await this.endingBodyWeightInput.sendKeys(endingBodyWeight);
  }

  async getEndingBodyWeightInput() {
    return this.endingBodyWeightInput.getAttribute('value');
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
    await this.setNameInput('name');
    expect(await this.getNameInput()).to.match(/name/);
    await waitUntilDisplayed(this.saveButton);
    await this.setDateStartedInput('01-01-2001');
    expect(await this.getDateStartedInput()).to.eq('2001-01-01');
    await waitUntilDisplayed(this.saveButton);
    await this.setDateEndedInput('01-01-2001');
    expect(await this.getDateEndedInput()).to.eq('2001-01-01');
    await waitUntilDisplayed(this.saveButton);
    await this.setGoalDateInput('01-01-2001');
    expect(await this.getGoalDateInput()).to.eq('2001-01-01');
    await waitUntilDisplayed(this.saveButton);
    await this.setStartingBodyWeightInput('5');
    expect(await this.getStartingBodyWeightInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setEndingBodyWeightInput('5');
    expect(await this.getEndingBodyWeightInput()).to.eq('5');
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
