import api from './config';

export const userServices = {
    getUser,
    updateUser,
    changePassword,
    getUserPreferences,
    updateUserPreferences,
};


function getUser() {

    return api.user.getUser()
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

function updateUser(user) {

    return api.user.updateUser(user)
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

function changePassword(pass) {

    return api.user.changePassword(pass)
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

    return api.user.getUserPreferences()
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

    return api.user.updateUserPreferences(userPreferences)
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

