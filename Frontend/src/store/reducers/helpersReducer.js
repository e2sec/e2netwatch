import {

    GET_TIMEZONES_BEGIN,
    GET_TIMEZONES_SUCCESS,
    GET_TIMEZONES_FAILURE,

} from '../config'


const initState = {
    timezones: {
        loading: false,
        error: null,
        data: [{}, {}]
    }
}


const helpersReducer = (state = initState, action ) => {
    switch(action.type){

        case GET_TIMEZONES_BEGIN:
            return {
                ...state,
                timezones: {
                    ...state,
                    loading: true
                }
            };
        case GET_TIMEZONES_SUCCESS:
            return {
                ...state,
                timezones: {
                    ...state,
                    loading: false,
                    data: action.timezones.content
                }
            };
        case GET_TIMEZONES_FAILURE:
            return {
                ...state,
                timezones: {
                    ...state,
                    loading: false,
                    error: action.error,
                    data: []
                }
            };

        default:
            return state;
    }

};

export default helpersReducer;