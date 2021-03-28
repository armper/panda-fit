import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ApplicationUserComponentsPage from './application-user.page-object';
import ApplicationUserUpdatePage from './application-user-update.page-object';
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

describe('ApplicationUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let applicationUserComponentsPage: ApplicationUserComponentsPage;
  let applicationUserUpdatePage: ApplicationUserUpdatePage;
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
    applicationUserComponentsPage = new ApplicationUserComponentsPage();
    applicationUserComponentsPage = await applicationUserComponentsPage.goToPage(navBarPage);
  });

  it('should load ApplicationUsers', async () => {
    expect(await applicationUserComponentsPage.title.getText()).to.match(/Application Users/);
    expect(await applicationUserComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete ApplicationUsers', async () => {
    const beforeRecordsCount = (await isVisible(applicationUserComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(applicationUserComponentsPage.table);
    applicationUserUpdatePage = await applicationUserComponentsPage.goToCreateApplicationUser();
    await applicationUserUpdatePage.enterData();

    expect(await applicationUserComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(applicationUserComponentsPage.table);
    await waitUntilCount(applicationUserComponentsPage.records, beforeRecordsCount + 1);
    expect(await applicationUserComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await applicationUserComponentsPage.deleteApplicationUser();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(applicationUserComponentsPage.records, beforeRecordsCount);
      expect(await applicationUserComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(applicationUserComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
