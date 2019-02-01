import { userServices } from '../../services/api/userServices';
import { helpers } from './../../helpers/helpers';

export const userActions = {
    login,
    logout,
    getUsers,
    getUserProfile,
    updateUserProfile,
    changeUserPass,
    getUserPreferences,
    updateUserPreferences,
    getTimezones,
}


function login(user) {
    return (dispatch) => {

        dispatch({ type: 'LOGIN_REQUEST'});

        userServices.login(user)
            .then(
                user => {
                    dispatch({ type: 'LOGIN_SUCCESS' })
                    helpers.browserHistory().push('/');
                },
                error => {
                    dispatch({ type: 'LOGIN_ERROR', error })
                }
            );

    }
}

function logout() {
    userServices.logout();
    return { type: 'LOGOUT_SUCCESS' }
}

function getUsers() {
    return (dispatch) => {

        userServices.getUsers()
            .then(
                users => {
                    dispatch({ type: 'GET_USERS', users })
                },
                error => {
                    dispatch({ type: 'GET_USERS', error })
                }
            )

    }
}

function getUserProfile() {
    return (dispatch) => {

        userServices.getUserProfile()
            .then(
                userProfile => {
                    dispatch({ type: 'GET_USER_PROFILE', userProfile })
                },
                error => {
                    dispatch({ type: 'GET_USER_PROFILE_ERROR', error })
                }
            )

    }
}

function updateUserProfile(userProfile) {
    return (dispatch) => {

        userServices.updateUserProfile(userProfile)
            .then(
                userProfile => {
                    dispatch({ type: 'UPDATE_USER_PROFILE', userProfile })
                },
                error => {
                    dispatch({ type: 'UPDATE_USER_PROFILE_ERROR', error })
                }
            )

    }
}

function changeUserPass(pass) {
    return (dispatch) => {

        userServices.changeUserPass(pass)
            .then(
                () => {
                    dispatch({ type: 'CHANGE_USER_PASS'})
                },
                error => {
                    dispatch({ type: 'CHANGE_USER_PASS_ERROR', error })
                }
            )

    }
}

function getUserPreferences() {
    return (dispatch) => {

        userServices.getUserPreferences()
            .then(
                userPreferences => {
                    dispatch({ type: 'GET_USER_PREFERENCES', userPreferences})
                },
                error => {
                    dispatch({ type: 'GET_USER_PREFERENCES_ERROR', error })
                }
            )

    }
}

function updateUserPreferences(userPreferences) {
    return (dispatch) => {

        userServices.updateUserPreferences(userPreferences)
            .then(
                userPreferences => {
                    dispatch({ type: 'UPDATE_USER_PREFERENCES', userPreferences })
                },
                error => {
                    dispatch({ type: 'UPDATE_USER_PREFERENCES_ERROR', error })
                }
            )

    }
}

function getTimezones() {
    return (dispatch) => {

        userServices.getTimezones()
            .then(
                timezones => {
                    dispatch({ type: 'GET_TIMEZONES', timezones})
                },
                error => {
                    dispatch({ type: 'GET_TIMEZONES_ERROR', error })
                }
            )

    }
}
