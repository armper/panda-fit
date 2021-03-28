import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import CycleComponentsPage from './cycle.page-object';
import CycleUpdatePage from './cycle-update.page-object';
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

describe('Cycle e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cycleComponentsPage: CycleComponentsPage;
  let cycleUpdatePage: CycleUpdatePage;
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
    cycleComponentsPage = new CycleComponentsPage();
    cycleComponentsPage = await cycleComponentsPage.goToPage(navBarPage);
  });

  it('should load Cycles', async () => {
    expect(await cycleComponentsPage.title.getText()).to.match(/Cycles/);
    expect(await cycleComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Cycles', async () => {
    const beforeRecordsCount = (await isVisible(cycleComponentsPage.noRecords)) ? 0 : await getRecordsCount(cycleComponentsPage.table);
    cycleUpdatePage = await cycleComponentsPage.goToCreateCycle();
    await cycleUpdatePage.enterData();

    expect(await cycleComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(cycleComponentsPage.table);
    await waitUntilCount(cycleComponentsPage.records, beforeRecordsCount + 1);
    expect(await cycleComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await cycleComponentsPage.deleteCycle();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(cycleComponentsPage.records, beforeRecordsCount);
      expect(await cycleComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(cycleComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
