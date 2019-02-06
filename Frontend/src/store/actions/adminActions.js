import {

    GET_USERS_BEGIN,
    GET_USERS_SUCCESS,
    GET_USERS_FAILURE,

    GET_USER_GROUPS_BEGIN,
    GET_USER_GROUPS_SUCCESS,
    GET_USER_GROUPS_FAILURE,

    ACTIVATE_USER_BEGIN,
    ACTIVATE_USER_SUCCESS,
    ACTIVATE_USER_FAILURE,

    DEACTIVATE_USER_BEGIN,
    DEACTIVATE_USER_SUCCESS,
    DEACTIVATE_USER_FAILURE,
    DELETE_USER_SUCCESS,
    DELETE_USER_BEGIN,
    DELETE_USER_FAILURE,
    ADMIN_UPDATE_USER_BEGIN,
    ADMIN_UPDATE_USER_SUCCESS,
    ADMIN_UPDATE_USER_FAILURE,
    GET_GLOBAL_PREFERENCES_BEGIN,
    GET_GLOBAL_PREFERENCES_SUCCESS,
    GET_GLOBAL_PREFERENCES_FAILURE,
    UPDATE_GLOBAL_PREFERENCES_BEGIN,
    UPDATE_GLOBAL_PREFERENCES_SUCCESS,
    UPDATE_GLOBAL_PREFERENCES_FAILURE, ADD_USER_BEGIN, ADD_USER_SUCCESS, ADD_USER_FAILURE

} from '../config'

import { adminServices } from '../../services/api/adminServices';

export const adminActions = {
    getUsers,
    getUserGroups,
    activateUser,
    deactivateUser,
    deleteUser,
    updateUser,
    addUser,
    getGlobalPreferences,
    updateGlobalPreferences
};

function getUsers() {
    return (dispatch) => {

        dispatch({ type: GET_USERS_BEGIN })

        adminServices.getUsers()
            .then(
                users => {
                    dispatch({ type: GET_USERS_SUCCESS, users })
                },
                error => {
                    dispatch({ type: GET_USERS_FAILURE, error })
                }
            )

    }
}

function getUserGroups() {
    return (dispatch) => {

        dispatch({ type: GET_USER_GROUPS_BEGIN })

        adminServices.getUserGroups()
            .then(
                userGroups => {
                    dispatch({ type: GET_USER_GROUPS_SUCCESS, userGroups })
                },
                error => {
                    dispatch({ type: GET_USER_GROUPS_FAILURE, error })
                }
            )

    }
}


function activateUser(userId) {
    return (dispatch) => {

        dispatch({ type: ACTIVATE_USER_BEGIN })

        adminServices.activateUser(userId)
            .then(
                () => {
                    dispatch({ type: ACTIVATE_USER_SUCCESS, userId })
                },
                error => {
                    dispatch({ type: ACTIVATE_USER_FAILURE, error })
                }
            )

    }
}


function deactivateUser(userId) {
    return (dispatch) => {

        dispatch({ type: DEACTIVATE_USER_BEGIN })

        adminServices.deactivateUser(userId)
            .then(
                () => {
                    dispatch({ type: DEACTIVATE_USER_SUCCESS, userId })
                },
                error => {
                    dispatch({ type: DEACTIVATE_USER_FAILURE, error })
                }
            )

    }
}


function deleteUser(userId) {
    return (dispatch) => {

        dispatch({ type: DELETE_USER_BEGIN })

        adminServices.deleteUser(userId)
            .then(
                () => {
                    dispatch({ type: DELETE_USER_SUCCESS, userId })
                },
                error => {
                    dispatch({ type: DELETE_USER_FAILURE, error })
                }
            )

    }
}

function updateUser(user) {
    return (dispatch) => {

        dispatch({ type: ADMIN_UPDATE_USER_BEGIN })

        adminServices.updateUser(user)
            .then(
                user => {
                    dispatch({ type: ADMIN_UPDATE_USER_SUCCESS, user })
                },
                error => {
                    dispatch({ type: ADMIN_UPDATE_USER_FAILURE, error })
                }
            )

    }
}


function addUser(user) {
    return (dispatch) => {

        dispatch({ type: ADD_USER_BEGIN })

        adminServices.addUser(user)
            .then(
                user => {
                    dispatch({ type: ADD_USER_SUCCESS, user })
                },
                error => {
                    dispatch({ type: ADD_USER_FAILURE, error })
                }
            )

    }
}


function getGlobalPreferences() {
    return (dispatch) => {

        dispatch({ type: GET_GLOBAL_PREFERENCES_BEGIN })

        adminServices.getGlobalPreferences()
            .then(
                globalPreferences => {
                    dispatch({ type: GET_GLOBAL_PREFERENCES_SUCCESS, globalPreferences })
                },
                error => {
                    dispatch({ type: GET_GLOBAL_PREFERENCES_FAILURE, error })
                }
            )

    }
}

function updateGlobalPreferences(globalPreferences) {
    return (dispatch) => {

        dispatch({ type: UPDATE_GLOBAL_PREFERENCES_BEGIN })

        adminServices.updateGlobalPreferences(globalPreferences)
            .then(
                globalPreferences => {
                    dispatch({ type: UPDATE_GLOBAL_PREFERENCES_SUCCESS, globalPreferences })
                },
                error => {
                    dispatch({ type: UPDATE_GLOBAL_PREFERENCES_FAILURE, error })
                }
            )

    }
}