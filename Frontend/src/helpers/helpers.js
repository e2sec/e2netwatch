import { createBrowserHistory } from "history";
import moment from 'moment';


export const helpers = {
    browserHistory,
    isTokenExpired,
    localStorageSave,
    getToken,
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
    }, 100000)

}

function localStorageSave(items) {

    for (let key in items) {
        if (items.hasOwnProperty(key)) {
            localStorage.setItem(key, items[key])
        }
    }
}

function getToken (){

    if("user" in localStorage){
        return localStorage.getItem('token');
    } else {
        return "";
    }

}
