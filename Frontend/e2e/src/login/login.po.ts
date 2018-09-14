import { browser, by, element } from 'protractor';

export class LoginPage {
    private credentials = {
        username: 'admin',
        password: 'admin'
    };

    navigateTo() {
        return browser.get('/login');
    }

    getLoginFormClasses() {
        return element(by.className('login-form')).getAttribute('class');
    }

    getErrorMessage() {
        return element(by.id('errorMessage')).getText();
    }

    fillCredentials(credentias: any = this.credentials) {
        element(by.css('[name="username"]')).sendKeys(credentias.username);
        element(by.css('[name="password"]')).sendKeys(credentias.password);
        element(by.css('.btn-e2n-primary')).click();
    }
}
