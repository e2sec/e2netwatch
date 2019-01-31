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

    actions: {
        getUsers() {
            return apiActions.get('/um/users')
        },
        getUserProfile() {
            return apiActions.get('/um/users/profile')
        },
        updateUserProfile(data) {
            return apiActions.put('um/users', data)
        },
        changeUserPass(data) {
            return apiActions.put('um/users/resetPassword', data)
        },
    }
};



