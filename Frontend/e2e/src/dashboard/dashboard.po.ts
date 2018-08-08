import { browser } from 'protractor';

export class DashboardPage {

    navigateTo() {
        return browser.get('/');
    }

    getTitle() {
        return browser.getTitle();
    }
}
