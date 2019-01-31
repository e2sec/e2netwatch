import React, { Component } from 'react';
import { Typography } from "@material-ui/core";
import { helpers } from "../../helpers/helpers";
import { Redirect } from "react-router-dom";

const Authorization = (allowedRoles) => (WrappedComponent) => {
    return class WithAuthorization extends Component {


        render() {

            const role = helpers.getRole();

            if(role === null){
                return <Redirect to={{ pathname: '/login', state: { from: this.props.location } }} />
            } else if (allowedRoles.includes(role)) {
                return <WrappedComponent {...this.props} />
            } else {
                return <Typography>You don't have access</Typography>
            }

        }
    }
}

export default Authorization;