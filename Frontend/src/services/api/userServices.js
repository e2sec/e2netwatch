import api from './api';
import moment from 'moment';
import { helpers } from './../../helpers/helpers'

export const userServices = {
    login,
    logout,
    getUsers,
    getUserProfile,
    updateUserProfile,
    changeUserPass,
    getUserPreferences,
    updateUserPreferences,
    getTimezones
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

function getUserProfile() {

    return api.actions.getUserProfile()
        .then(res => {
            return res.data;
        }).catch( error => {

            console.log(error)
            if (error.response) {

            }else if (error.request) {

            } else {

            }

            return Promise.reject(error.message);
        });
}

function getUsers() {

    return api.actions.getUsers()
        .then(res => {
            return res.data;
        }).catch( error => {

            console.log(error)
            if (error.response) {

            }else if (error.request) {

            } else {

            }

            return Promise.reject(error.message);
        });
}

function updateUserProfile(userProfile) {

    return api.actions.updateUserProfile(userProfile)
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

function changeUserPass(pass) {

    return api.actions.changeUserPass(pass)
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

function getUserPreferences() {

    return api.actions.getUserPreferences()
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

function updateUserPreferences(userPreferences) {

    return api.actions.updateUserPreferences(userPreferences)
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

function getTimezones() {

    return api.actions.getTimezones()
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