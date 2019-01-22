import {

    GET_USER_BEGIN,
    GET_USER_SUCCESS,
    GET_USER_FAILURE,
    GET_USER_PREFERENCES_BEGIN,
    GET_USER_PREFERENCES_SUCCESS,
    GET_USER_PREFERENCES_FAILURE,

    UPDATE_USER_BEGIN,
    UPDATE_USER_SUCCESS,
    UPDATE_USER_FAILURE,
    UPDATE_USER_PREFERENCES_BEGIN,
    UPDATE_USER_PREFERENCES_SUCCESS,
    UPDATE_USER_PREFERENCES_FAILURE,

    CHANGE_PASSWORD_BEGIN,
    CHANGE_PASSWORD_SUCCESS,
    CHANGE_PASSWORD_FAILURE

} from '../config'

import { userServices } from '../../services/api/userServices';

export const userActions = {
    getUser,
    updateUser,
    changePassword,
    getUserPreferences,
    updateUserPreferences,
};

function getUser() {
    return (dispatch) => {

        dispatch({ type: GET_USER_BEGIN })

        userServices.getUser()
            .then(
                user => {
                    dispatch({ type: GET_USER_SUCCESS, user })
                },
                error => {
                    dispatch({ type: GET_USER_FAILURE, error })
                }
            )

    }
}

function updateUser(user) {
    return (dispatch) => {

        dispatch({ type: UPDATE_USER_BEGIN })

        userServices.updateUser(user)
            .then(
                user => {
                    dispatch({ type: UPDATE_USER_SUCCESS, user })
                },
                error => {
                    dispatch({ type: UPDATE_USER_FAILURE, error })
                }
            )

    }
}

function changePassword(pass) {
    return (dispatch) => {

        dispatch({ type: CHANGE_PASSWORD_BEGIN })

        userServices.changePassword(pass)
            .then(
                () => {
                    dispatch({ type: CHANGE_PASSWORD_SUCCESS })
                },
                error => {
                    dispatch({ type: CHANGE_PASSWORD_FAILURE, error })
                }
            )

    }
}

function getUserPreferences() {
    return (dispatch) => {

        dispatch({ type: GET_USER_PREFERENCES_BEGIN })

        userServices.getUserPreferences()
            .then(
                userPreferences => {
                    dispatch({ type: GET_USER_PREFERENCES_SUCCESS, userPreferences})
                },
                error => {
                    dispatch({ type: GET_USER_PREFERENCES_FAILURE, error })
                }
            )

    }
}

function updateUserPreferences(userPreferences) {
    return (dispatch) => {

        dispatch({ type: UPDATE_USER_PREFERENCES_BEGIN })

        userServices.updateUserPreferences(userPreferences)
            .then(
                userPreferences => {
                    dispatch({ type: UPDATE_USER_PREFERENCES_SUCCESS, userPreferences })
                },
                error => {
                    dispatch({ type: UPDATE_USER_PREFERENCES_FAILURE, error })
                }
            )

    }
}

