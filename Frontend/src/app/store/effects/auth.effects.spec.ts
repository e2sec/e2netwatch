import { AuthEffects } from './auth.effects';
import { Actions } from '@ngrx/effects';
import { cold } from 'jasmine-marbles';
import { AuthActionTypes, LoginSuccess, LoginFailure } from '../actions/auth.actions';
import { throwError, of } from 'rxjs';

describe('AuthEffect', () => {

    it('should login', () => {
        const source = cold('a', { a: { type: AuthActionTypes.LOGIN, payLoad: { username: 'admin', password: 'admin' } } });
        const authService = createAuthServiceStub({ username: 'admin', password: 'admin' });
        const router = createRouterStub();
        const effects = new AuthEffects(new Actions(source), authService, router);
        const expected = cold('a', { a: new LoginSuccess({ token: 'someToken' }) });
        expect(effects.Login).toBeObservable(expected);
    });

    it('should fail to login', () => {
        const source = cold('a', { a: { type: AuthActionTypes.LOGIN, payLoad: { username: 'admin', password: 'admin' } } });
        const authService = createAuthServiceStub({ username: 'wrongUsername', password: 'admin' });
        const router = createRouterStub();
        const effects = new AuthEffects(new Actions(source), authService, router);
        const expected = cold('a', { a: new LoginFailure({ error: 'error' }) });
        expect(effects.Login).toBeObservable(expected);
    });

    it('should store token to localstorage', () => {
        const source = cold('a', { a: { type: AuthActionTypes.LOGIN_SUCCESS, payload: { token: 'testToken' } } });
        const authService = createAuthServiceStub({ username: 'admin', password: 'admin' });
        const router = createRouterStub();
        const effects = new AuthEffects(new Actions(source), authService, router);
        effects.LoginSuccess.subscribe(() => {
            expect(localStorage.getItem('token')).toEqual('testToken');
        });
    });

    it('should remove token from localstorage', () => {
        localStorage.setItem('token', 'testToken');
        expect(localStorage.getItem('token')).toEqual('testToken');
        const source = cold('a', { a: { type: AuthActionTypes.LOGOUT } });
        const authService = createAuthServiceStub({ username: 'admin', password: 'admin' });
        const router = createRouterStub();
        const effects = new AuthEffects(new Actions(source), authService, router);
        effects.Logout.subscribe(() => {
            expect(localStorage.getItem('token')).toBeNull();
        });
    });


    function createAuthServiceStub(payLoad: any) {
        const service = jasmine.createSpyObj('service', ['login']);
        const response = { token: 'someToken' };
        const serviceResponse = payLoad.username !== 'admin' ? throwError('error') : of(response);

        service.login.and.returnValue(serviceResponse);

        return service;
    }

    function createRouterStub() {
        const service = jasmine.createSpyObj('router', ['navigateByUrl']);
        return service;
    }
});
