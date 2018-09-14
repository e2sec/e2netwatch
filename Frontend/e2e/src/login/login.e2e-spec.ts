import { LoginPage } from './login.po';
import { browser } from 'protractor';

describe('Login page', () => {
  let loginPage: LoginPage;

  beforeEach(() => {
    loginPage = new LoginPage();
  });

  it('when empty username - form should be invalid', () => {
    loginPage.navigateTo();
    loginPage.fillCredentials({ username: '', password: 'password' });
    expect(loginPage.getLoginFormClasses()).toContain('ng-invalid');
  });

  it('when empty password - form should be invalid', () => {
    loginPage.navigateTo();
    loginPage.fillCredentials({ username: 'admin', password: '' });
    expect(loginPage.getLoginFormClasses()).toContain('ng-invalid');
  });

  it('when login failed — should show error message', () => {
    loginPage.navigateTo();
    loginPage.fillCredentials({ username: 'wrongUsername', password: 'password' });
    expect(loginPage.getErrorMessage()).toEqual('Incorect username or password.');
  });

  it('when login is successful — should redirect to dashboard page', () => {
    loginPage.navigateTo();
    loginPage.fillCredentials();
    expect(browser.getTitle()).toEqual('Dashboard');
  });
});
