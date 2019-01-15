import { userServices } from '../../services/api/userServices';
import { helpers } from './../../helpers/helpers';

export const userActions = {
    login,
    logout,
    userDetails
}


function login (user) {
    return (dispatch, getState) => {

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

function logout () {
    userServices.logout();
    return { type: 'LOGOUT_SUCCESS' }
}

function userDetails () {
    return (dispatch, getState) => {

        userServices.userDetails()

    }
}

