import { authServices } from '../../services/api/authServices';
import { helpers } from './../../helpers/helpers';

export const authActions = {
    login,
    logout,
};

function login(user) {
    return (dispatch) => {

        dispatch({ type: 'LOGIN_REQUEST'});

        authServices.login(user)
            .then(
                () => {
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
    authServices.logout();
    return { type: 'LOGOUT_SUCCESS' }
}

