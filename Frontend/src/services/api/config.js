import axios from 'axios'
import {helpers} from "../../helpers/helpers";

const token = helpers.getToken();

const apiAuth = axios.create({
    baseURL: 'http://localhost:8080/api/',
});

const apiActions = axios.create({
    baseURL: 'http://localhost:8080/api/',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
    }
});


export default {

    auth: {
        login(data) {
            return apiAuth.post('/auth/login', data)
        },
        logout() {
            localStorage.clear();
        },
    },

    user: {
        getUser() {
            return apiActions.get('/um/users/profile')
        },
        updateUser(data) {
            return apiActions.put('um/users', data)
        },
        changePassword(data) {
            return apiActions.put('um/users/resetPassword', data)
        },
        getUserPreferences() {
            return apiActions.get('/um/profilepreferences/current')
        },
        updateUserPreferences(preferences) {
            return apiActions.put('/um/profilepreferences', {id: 1, timezone: preferences})
        },
    },

    admin: {
        getUsers() {
            return apiActions.get('/um/users')
        },
        getUserGroups() {
            return apiActions.get('/um/usergroups')
        },
        activateUser(userId) {
            return apiActions.get('/um/users/activate/' + userId)
        },
        deactivateUser(userId) {
            return apiActions.get('/um/users/deactivate/' + userId)
        },
        deleteUser(userId) {
            return apiActions.delete('/um/users/' + userId)
        },
        updateUser(user) {
            return apiActions.put('/um/users/update', user)
        },
        getGlobalPreferences() {
            return apiActions.get('/um/profilepreferences/global')
        },
        updateGlobalPreferences(preferences) {
            return apiActions.put('/um/profilepreferences/global', {id: 1, timezone: preferences})
        }
    },

    helpers: {
        getTimezones() {
            return apiActions.get('/um/timezones')
        }
    }
};



