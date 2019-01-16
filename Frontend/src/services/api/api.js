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
        'Authorization': 'Bearer '+ token,
    }
});


export default {

    auth: {
        login(data) {
            return apiAuth.post('/auth/login/', data)
        },
        logout() {
            localStorage.clear();
        },
    },

    actions: {
        userDetails(data) {
            return apiActions.post('/auth/login/', data)
        },
    }
};
