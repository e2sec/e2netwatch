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

    DELETE_USER_BEGIN,
    DELETE_USER_SUCCESS,
    DELETE_USER_FAILURE,

    ADMIN_UPDATE_USER_BEGIN,
    ADMIN_UPDATE_USER_SUCCESS,
    ADMIN_UPDATE_USER_FAILURE,
    GET_GLOBAL_PREFERENCES_BEGIN,
    GET_GLOBAL_PREFERENCES_SUCCESS,
    GET_GLOBAL_PREFERENCES_FAILURE,
    UPDATE_GLOBAL_PREFERENCES_BEGIN,
    UPDATE_GLOBAL_PREFERENCES_SUCCESS,
    UPDATE_GLOBAL_PREFERENCES_FAILURE

} from '../config'



function updateUser(users, userToUpdate){

    return users.map(user => {

        if(user.id === userToUpdate.id){
            user = userToUpdate;
            user.userGroupId = user.userGroup.id;
            user.userStatusId = user.userStatus.id;
        }

        return user;
    });
}


const initState = {
    users: {
        loading: false,
        error: null,
        data: [],
        updating: false
    },
    userGroups: {
        loading: false,
        error: null,
        data: []
    },
    globalPreferences: {
        loading: false,
        error: null,
        data: {},
        updating: false
    }
}


const adminReducer = (state = initState, action ) => {
    switch(action.type){
        case GET_USERS_BEGIN:
            return {
                ...state,
                users: {
                    ...state.users,
                    loading: true
                }
            };
        case GET_USERS_SUCCESS:
            return {
                ...state,
                users: {
                    ...state.users,
                    loading: false,
                    data: action.users.content.map( user => {
                        user.userGroupId = user.userGroup.id;
                        user.userStatusId = user.userStatus.id;
                        return user;
                    }),
                }
            };
        case GET_USERS_FAILURE:
            return {
                ...state,
                users: {
                    loading: false,
                    error: action.error,
                    data: []
                }
            };


        case GET_USER_GROUPS_BEGIN:
            return {
                ...state,
                userGroups: {
                    ...state.userGroups,
                    loading: true
                }
            };
        case GET_USER_GROUPS_SUCCESS:
            return {
                ...state,
                userGroups: {
                    ...state.userGroups,
                    loading: false,
                    data: action.userGroups,
                }
            };
        case GET_USER_GROUPS_FAILURE:
            return {
                ...state,
                userGroups: {
                    ...state.userGroups,
                    loading: false,
                    error: action.error,
                    data: []
                }
            };


        case ACTIVATE_USER_BEGIN:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: true
                }
            };
        case ACTIVATE_USER_SUCCESS:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    data : updateUser(state.users.data,action.user)
                }
            };
        case ACTIVATE_USER_FAILURE:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    error: action.error,

                }
            };


        case DEACTIVATE_USER_BEGIN:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: true
                }
            };
        case DEACTIVATE_USER_SUCCESS:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    data : updateUser(state.users.data,action.user)
                }
            };
        case DEACTIVATE_USER_FAILURE:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    error: action.error,

                }
            };


        case DELETE_USER_BEGIN:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: true
                }
            };
        case DELETE_USER_SUCCESS:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    data : state.users.data.filter(user => {
                        return user.id !== action.userId;
                    })
                }
            };
        case DELETE_USER_FAILURE:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    error: action.error,

                }
            };


        case ADMIN_UPDATE_USER_BEGIN:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: true
                }
            };
        case ADMIN_UPDATE_USER_SUCCESS:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    data : updateUser(state.users.data,action.user)
                }
            };
        case ADMIN_UPDATE_USER_FAILURE:
            return {
                ...state,
                users: {
                    ...state.users,
                    updating: false,
                    error: action.error,
                    data: []
                }
            };


        case GET_GLOBAL_PREFERENCES_BEGIN:
            return {
                ...state,
                globalPreferences: {
                    ...state.globalPreferences,
                    loading: true
                }
            };
        case GET_GLOBAL_PREFERENCES_SUCCESS:
            return {
                ...state,
                globalPreferences: {
                    ...state.globalPreferences,
                    loading: false,
                    data: action.globalPreferences
                }
            };
        case GET_GLOBAL_PREFERENCES_FAILURE:
            return {
                ...state,
                globalPreferences: {
                    ...state.globalPreferences,
                    loading: false,
                    error: action.error,
                    data: []
                }
            };


        case UPDATE_GLOBAL_PREFERENCES_BEGIN:
            return {
                ...state,
                globalPreferences: {
                    ...state.globalPreferences,
                    updating: true
                }
            };
        case UPDATE_GLOBAL_PREFERENCES_SUCCESS:
            return {
                ...state,
                globalPreferences: {
                    ...state.globalPreferences,
                    updating: false,
                    data: action.globalPreferences
                }
            };
        case UPDATE_GLOBAL_PREFERENCES_FAILURE:
            return {
                ...state,
                globalPreferences: {
                    ...state.globalPreferences,
                    updating: false,
                    error: action.error,
                }
            };

        default:
            return state;
    }

};

export default adminReducer;