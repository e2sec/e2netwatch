import { Action } from '@ngrx/store';
import { UserProfile } from '../../models/user-profile';

export enum UserProfileActionTypes {
    LOAD_PROFILE = '[PROFILE] Load',
    UPDATE_PROFILE = '[PROFILE] Update',
    LOAD_SUCCESS = '[PROFILE] Load Success',
    LOAD_FAILURE = '[PROFILE] Load Failure',
}

export class LoadProfile implements Action {
    readonly type = UserProfileActionTypes.LOAD_PROFILE;

    constructor(public payload: UserProfile) { }
}

export class UpdateProfile implements Action {
    readonly type = UserProfileActionTypes.UPDATE_PROFILE;

    constructor(public payload: UserProfile) { }
}

export class LoadSuccess implements Action {
    readonly type = UserProfileActionTypes.LOAD_SUCCESS;

    constructor(public payload: any) { }
}

export class LoadFailure implements Action {
    readonly type = UserProfileActionTypes.LOAD_FAILURE;

    constructor(public payload: any) { }
}

export type ProfileActions =
     | LoadProfile
     | UpdateProfile
     | LoadSuccess
     | LoadFailure;

