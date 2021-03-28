import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ExcerciseComponentsPage from './excercise.page-object';
import ExcerciseUpdatePage from './excercise-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('Excercise e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let excerciseComponentsPage: ExcerciseComponentsPage;
  let excerciseUpdatePage: ExcerciseUpdatePage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();
    await signInPage.username.sendKeys(username);
    await signInPage.password.sendKeys(password);
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    excerciseComponentsPage = new ExcerciseComponentsPage();
    excerciseComponentsPage = await excerciseComponentsPage.goToPage(navBarPage);
  });

  it('should load Excercises', async () => {
    expect(await excerciseComponentsPage.title.getText()).to.match(/Excercises/);
    expect(await excerciseComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Excercises', async () => {
    const beforeRecordsCount = (await isVisible(excerciseComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(excerciseComponentsPage.table);
    excerciseUpdatePage = await excerciseComponentsPage.goToCreateExcercise();
    await excerciseUpdatePage.enterData();

    expect(await excerciseComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(excerciseComponentsPage.table);
    await waitUntilCount(excerciseComponentsPage.records, beforeRecordsCount + 1);
    expect(await excerciseComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await excerciseComponentsPage.deleteExcercise();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(excerciseComponentsPage.records, beforeRecordsCount);
      expect(await excerciseComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(excerciseComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
