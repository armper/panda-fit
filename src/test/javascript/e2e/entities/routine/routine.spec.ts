import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import RoutineComponentsPage from './routine.page-object';
import RoutineUpdatePage from './routine-update.page-object';
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

describe('Routine e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let routineComponentsPage: RoutineComponentsPage;
  let routineUpdatePage: RoutineUpdatePage;
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
    routineComponentsPage = new RoutineComponentsPage();
    routineComponentsPage = await routineComponentsPage.goToPage(navBarPage);
  });

  it('should load Routines', async () => {
    expect(await routineComponentsPage.title.getText()).to.match(/Routines/);
    expect(await routineComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Routines', async () => {
    const beforeRecordsCount = (await isVisible(routineComponentsPage.noRecords)) ? 0 : await getRecordsCount(routineComponentsPage.table);
    routineUpdatePage = await routineComponentsPage.goToCreateRoutine();
    await routineUpdatePage.enterData();

    expect(await routineComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(routineComponentsPage.table);
    await waitUntilCount(routineComponentsPage.records, beforeRecordsCount + 1);
    expect(await routineComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await routineComponentsPage.deleteRoutine();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(routineComponentsPage.records, beforeRecordsCount);
      expect(await routineComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(routineComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
