import authReducer from './authReducer';
import userReducer from './userReducer';
import adminReducer from './adminReducer';
import helpersReducer from './helpersReducer';
import { combineReducers } from 'redux';

const rootReducer = combineReducers({
    auth: authReducer,
    user: userReducer,
    helpers: helpersReducer,
    admin: adminReducer,
})

export default rootReducer;