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
    profile: {
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
                profile: {
                    ...state.profile,
                    loading: true
                }
            };
        case GET_USER_SUCCESS:
            return {
                ...state,
                profile: {
                    ...state.profile,
                    loading: false,
                    data: action.user,
                }
            };
        case GET_USER_FAILURE:
            return {
                ...state,
                profile: {
                    loading: false,
                    error: action.error,
                    data: {}
                }
            };


        case UPDATE_USER_BEGIN:
            return {
                ...state,
                profile: {
                    ...state.profile,
                    updating: true
                }
            };
        case UPDATE_USER_SUCCESS:
            return {
                ...state,
                profile: {
                    ...state.profile,
                    updating: false,
                    data: action.user,
                }
            };
        case UPDATE_USER_FAILURE:
            return {
                ...state,
                profile: {
                    ...state.profile,
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


        /*case 'UPDATE_USER_PROFILE':
            return {
                ...state,
                userProfile: {
                    ...state.userProfile,
                    email: action.userProfile.email,
                    firstName: action.userProfile.firstName,
                    lastName: action.userProfile.lastName,
                },
            };
        case 'UPDATE_USER_PROFILE_ERROR':
            return state;
        case 'CHANGE_USER_PASS':
            return state;
        case 'CHANGE_USER_PASS_ERROR':
            return state;
        case 'GET_USER_PREFERENCES':
            return {
                ...state,
                userPreferences: action.userPreferences
            };
        case 'GET_USER_PREFERENCES_ERROR':
            return state;
        case 'UPDATE_USER_PREFERENCES':
            return state;
        case 'UPDATE_USER_PREFERENCES_ERROR':
            return state;






        case 'GET_USERS_ERROR':
            return state;
        case 'GET_TIMEZONES':
            return {
                ...state,
                timezones: action.timezones.content
            };
        case 'GET_TIMEZONES_ERROR':
            return state;*/
        default:
            return state;
    }

};

export default userReducer;