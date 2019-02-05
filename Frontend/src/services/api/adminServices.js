import api from './config';

export const adminServices = {
    getUsers,
    getUserGroups,
    activateUser,
    deactivateUser,
    deleteUser
};

function getUsers() {

    return api.admin.getUsers()
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

function getUserGroups() {

    return api.admin.getUserGroups()
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

function activateUser(userId) {

    return api.admin.activateUser(userId)
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

function deactivateUser(userId) {

    return api.admin.deactivateUser(userId)
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

function deleteUser(userId) {

    return api.admin.deleteUser(userId)
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