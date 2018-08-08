import { browser, by, element } from 'protractor';

export class LoginPage {
    private credentials = {
        username: 'e2nw',
        password: 'test'
    };

    navigateTo() {
        return browser.get('/login');
    }

    fillCredentials(credentias: any = this.credentials) {
        element(by.css('[name="username"]')).sendKeys(credentias.username);
        element(by.css('[name="password"]')).sendKeys(credentias.password);
        element(by.css('.btn-e2n-primary')).click();
    }
}
