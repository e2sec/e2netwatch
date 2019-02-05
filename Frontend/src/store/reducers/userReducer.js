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




const initState = {
    user: {
        loading: false,
        error: null,
        data: {},
        updating: false,
    },
    preferences: {
        loading: false,
        error: null,
        data: {},
        updating: false,
    },
    password: {
        loading: false,
        error: null,
    }
}


const userReducer = (state = initState, action ) => {

    switch(action.type){

        case GET_USER_BEGIN:
            return {
                ...state,
                user: {
                    ...state.user,
                    loading: true
                }
            };
        case GET_USER_SUCCESS:
            return {
                ...state,
                user: {
                    ...state.user,
                    loading: false,
                    data: {
                        ...action.user,
                        userGroupId: action.user.userGroup.id,
                        userStatusId: action.user.userStatus.id,
                    }
                }
            };
        case GET_USER_FAILURE:
            return {
                ...state,
                user: {
                    loading: false,
                    error: action.error,
                    data: {}
                }
            };


        case UPDATE_USER_BEGIN:
            return {
                ...state,
                user: {
                    ...state.user,
                    updating: true
                }
            };
        case UPDATE_USER_SUCCESS:
            return {
                ...state,
                user: {
                    ...state.user,
                    updating: false,
                    data: {
                        ...action.user,
                        userGroupId: action.user.userGroup.id,
                        userStatusId: action.user.userStatus.id,
                    }
                }
            };
        case UPDATE_USER_FAILURE:
            return {
                ...state,
                user: {
                    ...state.user,
                    updating: false,
                    error: action.error,
                }
            };


        case GET_USER_PREFERENCES_BEGIN:
            return {
                ...state,
                preferences: {
                    ...state.preferences,
                    loading: true
                }
            };
        case GET_USER_PREFERENCES_SUCCESS:
            return {
                ...state,
                preferences: {
                    ...state.preferences,
                    loading: false,
                    data: action.userPreferences,
                }
            };
        case GET_USER_PREFERENCES_FAILURE:
            return {
                ...state,
                preferences: {
                    loading: false,
                    error: action.error,
                    data: {}
                }
            };


        case UPDATE_USER_PREFERENCES_BEGIN:
            return {
                ...state,
                preferences: {
                    ...state.preferences,
                    updating: true
                }
            };
        case UPDATE_USER_PREFERENCES_SUCCESS:
            return {
                ...state,
                preferences: {
                    ...state.preferences,
                    updating: false,
                    data: action.userPreferences,
                }
            };
        case UPDATE_USER_PREFERENCES_FAILURE:
            return {
                ...state,
                preferences: {
                    ...state.preferences,
                    updating: false,
                    error: action.error,
                },
            };


        case CHANGE_PASSWORD_BEGIN:
            return {
                ...state,
                password: {
                    loading: true,
                    error: null
                }
            };
        case CHANGE_PASSWORD_SUCCESS:
            return {
                ...state,
                password: {
                    loading: false,
                    error: null,
                }
            };
        case CHANGE_PASSWORD_FAILURE:
            return {
                ...state,
                password: {
                    loading: false,
                    error: action.error
                }
            };


        default:
            return state;
    }

};

export default userReducer;