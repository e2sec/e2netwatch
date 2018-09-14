import { browser } from 'protractor';

export class UserProfilePage {
    navigateTo() {
        return browser.get('/user-profile');
    }

    getTitle() {
        return browser.getTitle();
    }
}
