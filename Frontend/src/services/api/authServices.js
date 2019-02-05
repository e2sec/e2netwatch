import api from './config';
import moment from 'moment';
import { helpers } from './../../helpers/helpers'

export const authServices = {
    login,
    logout,
};

function login (user) {

    return api.auth.login(user)
        .then(res => {

            const startTime = moment().format(),
                endTime = moment().add( res.data.expireTimeInS,'s').format();


            if (res.data.token) {

                const data = {
                    'user': user.username,
                    'role': res.data.role,
                    'token': res.data.token,
                    'startTime': startTime,
                    'endTime': endTime,
                };

                helpers.localStorageSave(data);

            }

            return user;
        }).catch( error => {
            if (error.response) {

                if(error.response.status === 401) {
                    logout();
                    window.location.reload(true);
                }

            } else if (error.request) {

            } else {

            }

            return Promise.reject(error.message);
        });
};


function logout() {
    api.auth.logout();
}

