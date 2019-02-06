import api from './config';

export const adminServices = {
    getUsers,
    getUserGroups,
    activateUser,
    deactivateUser,
    deleteUser,
    updateUser,
    addUser,
    getGlobalPreferences,
    updateGlobalPreferences
};

function getUsers() {

    return api.admin.getUsers()
        .then(res => {
            return res.data;
        }).catch( error => {

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

            if (error.response) {

            }else if (error.request) {

            } else {

            }

            return Promise.reject(error.message);
        });
}

function updateUser(user) {

    return api.admin.updateUser(user)
        .then(res => {
            return res.data;
        }).catch( error => {

            if (error.response) {

            }else if (error.request) {

            } else {

            }

            return Promise.reject(error.message);
        });
}

function addUser(user) {

    return api.admin.addUser(user)
        .then(res => {
            return res.data;
        }).catch( error => {

            if (error.response) {

            }else if (error.request) {

            } else {

            }

            return Promise.reject(error.message);
        });
}

function getGlobalPreferences() {

    return api.admin.getGlobalPreferences()
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

function updateGlobalPreferences(preferences) {

    return api.admin.updateGlobalPreferences(preferences)
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