import {

    GET_TIMEZONES_BEGIN,
    GET_TIMEZONES_SUCCESS,
    GET_TIMEZONES_FAILURE,

} from '../config'

import { helpersServices } from "../../services/api/helpersServices";

export const helpersActions = {
    getTimezones,
};

function getTimezones() {
    return (dispatch) => {

        dispatch({ type: GET_TIMEZONES_BEGIN })

        helpersServices.getTimezones()
            .then(
                timezones => {
                    dispatch({ type: GET_TIMEZONES_SUCCESS, timezones })
                },
                error => {
                    dispatch({ type: GET_TIMEZONES_FAILURE, error })
                }
            )

    }
}