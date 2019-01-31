import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { helpers } from "../../helpers/helpers";

const PrivateRoute = ({ component: Component, ...rest }) => (

    <Route {...rest} render={props => (
        helpers.getToken()
            ? <Component {...props} />
            : <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
    )} />
)

export default PrivateRoute;