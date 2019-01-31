import { userServices } from '../../services/api/userServices';
import { helpers } from './../../helpers/helpers';

export const userActions = {
    login,
    logout,
    getUsers,
    getUserProfile,
    updateUserProfile,
    changeUserPass
}


function login(user) {
    return (dispatch) => {

        dispatch({ type: 'LOGIN_REQUEST', user: user.username });

        userServices.login(user)
            .then(
                user => {
                    dispatch({ type: 'LOGIN_SUCCESS', user: user.username })
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
                profile => {
                    dispatch({ type: 'GET_USER_PROFILE', profile })
                },
                error => {
                    dispatch({ type: 'GET_USER_PROFILE_ERROR', error })
                }
            )

    }
}

function updateUserProfile(profile) {
    return (dispatch) => {

        userServices.updateUserProfile(profile)
            .then(
                profile => {
                    dispatch({ type: 'UPDATE_USER_PROFILE', profile })
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

