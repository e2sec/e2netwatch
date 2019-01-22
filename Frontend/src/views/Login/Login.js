import React, { Component} from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import {authActions} from "../../store/actions/authActions";
import { helpers } from './../../helpers/helpers'

import { withStyles } from '@material-ui/core/styles';
import { TextField, Button } from '@material-ui/core'

import logo from '../../assets/images/logo.png';

const styles = theme => ({
    root: {
        width: '100vw',
        minHeight: '100vh',
        height: '100%',
        background: '#375f96',
    },
    header: {
        background: '#f5f5f5',
        paddingTop: theme.spacing.unit * 10,
        paddingBottom: theme.spacing.unit * 10,
    },
    logo: {
        maxWidth: '180px',
        display: 'block',
        margin: 'auto',
    },
    form: {
        position: 'relative',
        maxWidth: '420px',
        minWidth: '360px',
        margin: 'auto',
        background: '#fff',
        padding: theme.spacing.unit * 5,
        top: -theme.spacing.unit * 6,
        boxShadow: '0 0 0 1px rgba(61,70,79,.05), 0 1px 3px 0 rgba(61,70,79,.15)',
        borderRadius: '5px',
    },
    button: {
        marginTop: theme.spacing.unit * 2,
    }
})



class Login extends Component {

    state = {
        username: '',
        password: ''
    };


    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.login(this.state);

    }

    render(){

        const token = helpers.getToken();
        const { classes } = this.props;

        const element = token ? (
            <Redirect to="/"/>
        ) : (
            <div className={classes.root}>

                <div className={classes.header}>
                    <img src={logo} alt="logo" className={classes.logo}/>
                </div>

                <form onSubmit={this.handleSubmit} className={classes.form}>
                    <TextField
                        id="username"
                        label="Username"
                        fullWidth={true}
                        margin="dense"
                        onChange={this.handleChange}
                    />

                    <TextField
                        id="password"
                        label="Password"
                        type="password"
                        fullWidth={true}
                        margin="dense"
                        onChange={this.handleChange}
                    />

                    <Button type="submit"
                            variant="contained"
                            color="primary"
                            size="large"
                            className={classes.button}>
                        Sign in
                    </Button>
                </form>

            </div>
        );

        return element
    }
}


const mapDispatchToProps = (dispatch) => {
    return{
        login: (user) => dispatch(authActions.login(user))
    }
}

export default withStyles(styles)(connect(null, mapDispatchToProps)(Login));
