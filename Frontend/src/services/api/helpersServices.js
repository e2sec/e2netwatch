import api from './config';

export const helpersServices = {
    getTimezones
};


function getTimezones() {

    return api.helpers.getTimezones()
        .then((res) => {
            return res.data
        }).catch( error => {
            if (error.response) {

            }else if (error.request) {

            } else {

            }
            return Promise.reject(error.message);
        });
}