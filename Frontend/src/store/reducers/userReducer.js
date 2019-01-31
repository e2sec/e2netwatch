

const initState = {
    profile: "",
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
            return {};
        case 'GET_USER_PROFILE':
            return {
                ...state,
                profile: action.profile,
            }
        case 'GET_USER_PROFILE_ERROR':
            return {};
        case 'UPDATE_USER_PROFILE':
            return {
                ...state,
                profile: {
                    ...state.profile,
                    email: action.profile.email,
                    firstName: action.profile.firstName,
                    lastName: action.profile.lastName,
                },
            };
        case 'UPDATE_USER_PROFILE_ERROR':
            return {};
        case 'CHANGE_USER_PASS':
            return state;
        case 'CHANGE_USER_PASS_ERROR':
            return {};
        default:
            return state;
    }

};

export default userReducer;