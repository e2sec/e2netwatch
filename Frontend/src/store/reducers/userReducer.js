

const initState = {
    userProfile: "",
    userPreferences: "",
    timezones: "",
    users: []
}


const userReducer = (state = initState, action ) => {
    switch(action.type){
        case 'GET_USERS':
            return {
                ...state,
                users: action.users
            };
        case 'GET_USERS_ERROR':
            return state;
        case 'GET_USER_PROFILE':
            return {
                ...state,
                userProfile: action.userProfile,
            }
        case 'GET_USER_PROFILE_ERROR':
            return state;
        case 'UPDATE_USER_PROFILE':
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
        case 'GET_TIMEZONES':
            return {
                ...state,
                timezones: action.timezones.content
            };
        case 'GET_TIMEZONES_ERROR':
            return state;
        default:
            return state;
    }

};

export default userReducer;