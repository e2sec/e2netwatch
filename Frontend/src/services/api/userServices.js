import axios from 'axios';
import moment from 'moment';
import { helpers } from './../../helpers/helpers'

export const userServices = {
    login,
    logout,
    userDetails,
};

function login (user) {

    return axios.post('http://localhost:8080/api/auth/login', user)
        .then(res => {

            const startTime = moment().format(),
                  endTime = moment().add( res.data.expireTimeInS,'s').format();


            if (res.data.token) {

                const data = {
                    'user': user.username,
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
    localStorage.clear();
}

function userDetails() {

    const token = helpers.getToken();

    return axios.get('http://localhost:8080/api/um/users',
        {headers: {'Content-Type': 'application/json', 'Authorization': 'Bearer '+ token }})
        .then(res => {

            console.log(res.data)

        }).catch( error => {
            if (error.response) {


            }

            return Promise.reject(error.message);
        });
}