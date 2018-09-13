import { cold } from 'jasmine-marbles';
import { ProfileEffects } from './profile.effects';
import { Observable, of, throwError } from 'rxjs';
import { Actions } from '@ngrx/effects';
import { UserProfileActionTypes, LoadSuccess, LoadFailure } from '../actions/profile.actions';

describe('ProfileEffect', () => {
    it('should load profile data', () => {
        const source = cold('a', { a: { type: UserProfileActionTypes.LOAD_PROFILE } });
        const service = createApiServiceStub({ content: {} }, false);
        const effects = new ProfileEffects(new Actions(source), service);
        const expected = cold('a', { a: new LoadSuccess({ profile: {} }) });
        expect(effects.LoadProfile).toBeObservable(expected);
    });

    it('should fail to load profile data', () => {
        const source = cold('a', { a: { type: UserProfileActionTypes.LOAD_PROFILE } });
        const service = createApiServiceStub({}, true);
        const effects = new ProfileEffects(new Actions(source), service);
        const expected = cold('a', { a: new LoadFailure({ error: {} }) });
        expect(effects.LoadProfile).toBeObservable(expected);
    });

    function createApiServiceStub(response: any, isError) {
        const service = jasmine.createSpyObj('service', ['get']);

        const serviceResponse = isError ? throwError(response) : of(response);

        service.get.and.returnValue(serviceResponse);

        return service;
    }
});
