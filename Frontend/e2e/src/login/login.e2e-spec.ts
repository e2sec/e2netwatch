import { LoginPage } from './login.po';
import { DashboardPage } from '../dashboard/dashboard.po';

describe('workspace-project App', () => {
  let loginPage: LoginPage;
  let dashboardPage: DashboardPage;

  beforeEach(() => {
    loginPage = new LoginPage();
    dashboardPage = new DashboardPage();
  });

  it('when login is successful — he should redirect to dashboard page', () => {
    loginPage.navigateTo();
    loginPage.fillCredentials();
    expect(dashboardPage.getTitle()).toEqual('Dashboard');
  });
});
