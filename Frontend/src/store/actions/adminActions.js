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
    DEACTIVATE_USER_FAILURE, DELETE_USER_SUCCESS, DELETE_USER_BEGIN, DELETE_USER_FAILURE

} from '../config'

import { adminServices } from '../../services/api/adminServices';

export const adminActions = {
    getUsers,
    getUserGroups,
    activateUser,
    deactivateUser,
    deleteUser
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
                user => {
                    dispatch({ type: ACTIVATE_USER_SUCCESS, user })
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
                user => {
                    dispatch({ type: DEACTIVATE_USER_SUCCESS, user })
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