import { DashboardPage } from './dashboard.po';
import { LoginPage } from '../login/login.po';

describe('Dashboard page', () => {
    let dashboardPage: DashboardPage;
    let loginpage: LoginPage;

    beforeEach(() => {
        dashboardPage = new DashboardPage();
        loginpage = new LoginPage();
    });

    it('should load dashboard page', () => {
        dashboardPage.navigateTo();
        loginpage.navigateTo();
        loginpage.fillCredentials();
        expect(dashboardPage.getTitle()).toEqual('Dashboard');
    });
});
