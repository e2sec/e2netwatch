import { createBrowserHistory } from "history";
import moment from 'moment';


export const helpers = {
    browserHistory,
    isTokenExpired,
    localStorageSave,
    getToken,
    getUser,
    getRole
}


function browserHistory (){
    return createBrowserHistory({forceRefresh: true});
}

function isTokenExpired(){
    const tokenExpireDate = localStorage.getItem('endTime');
    tokenInterval(tokenExpireDate);
}

function tokenInterval(tokenExpireDate) {

    if(moment().isAfter(tokenExpireDate)){
        localStorage.clear();
        window.location.reload(true);

    }

    setTimeout( function(){
        tokenInterval(tokenExpireDate)
    }, 5000)

}

function localStorageSave(items) {

    for (let key in items) {
        if (items.hasOwnProperty(key)) {
            localStorage.setItem(key, items[key])
        }
    }
}

function getToken (){
    return localStorage.getItem('token');
}

function getUser (){
    return localStorage.getItem('user');
}

function getRole (){
    return localStorage.getItem('role');
}
