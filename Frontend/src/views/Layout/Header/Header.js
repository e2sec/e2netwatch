import React, { Component, Fragment } from 'react';

import { connect } from 'react-redux';
import { userActions } from '../../../store/actions/userActions';
import { helpers } from './../../../helpers/helpers'

import { Link } from 'react-router-dom';


import { withStyles } from '@material-ui/core/styles';
import {AppBar, Toolbar, Typography, IconButton, Menu, MenuItem, ListItemText, ListItemIcon } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import More from '@material-ui/icons/MoreVert';
import Account from '@material-ui/icons/AccountCircle';
import Logout from '@material-ui/icons/ExitToApp';


const styles = theme => ({
    appBar: {
        marginLeft: '20%',
        [theme.breakpoints.up('md')]: {
            width: `calc(100% - 20%)`,
        },
    },
    toolbar: {
        paddingTop: theme.spacing.unit,
        paddingBottom: theme.spacing.unit,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
        [theme.breakpoints.up('md')]: {
            display: 'none',
        },
    },
    profileButton: {
        marginRight: -12,
        marginLeft: 20,
    },
    grow: {
        flexGrow: 1,
    },
});


class Header extends Component {


    state = {
        anchorEl: null,
    };

    componentDidMount() {
        helpers.isTokenExpired()
    };

    openDropdown = event => {
        this.setState({
            anchorEl: event.currentTarget
        });
    };

    closeDropdown = () => {
        this.setState({
            anchorEl: null
        });
    };

    handleMenuClick = () => {
        this.props.handleDrawerToggle();
    };

    logout = () => {
        this.props.logout();
    };

    render() {

        const { anchorEl } = this.state;
        const { classes } = this.props;

        return(

            <AppBar position="fixed" className={classes.appBar}>
                <Toolbar className={classes.toolbar}>
                    <IconButton className={classes.menuButton} color="inherit" onClick={this.handleMenuClick}>
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h5" color="inherit" className={classes.grow}>
                        Dashboard
                    </Typography>
                    <Typography variant="h5" color="inherit">
                        Tomislav Bobinac
                    </Typography>
                    <Fragment>
                        <IconButton color="inherit"
                                    className={classes.profileButton}
                                    aria-owns={anchorEl ? 'dropdown' : undefined}
                                    aria-haspopup="true"
                                    onClick={this.openDropdown}
                        >
                            <More />
                        </IconButton>
                        <Menu id="dropdown"
                              anchorEl={anchorEl}
                              open={Boolean(anchorEl)}
                              onClose={this.closeDropdown}
                        >
                            <MenuItem  component={Link} to="/profile" onClick={this.closeDropdown}>
                                <ListItemIcon>
                                    <Account />
                                </ListItemIcon>
                                <ListItemText primary="Profile" />
                            </MenuItem>
                            <MenuItem component={Link} to="/dashboard" onClick={this.logout}>
                                <ListItemIcon>
                                    <Logout />
                                </ListItemIcon>
                                <ListItemText primary="Logout" />
                            </MenuItem>
                        </Menu>
                    </Fragment>
                </Toolbar>
            </AppBar>
        )
    }
}


const mapDispatchToProps = (dispatch) => {
    return{
        logout: () => dispatch(userActions.logout())
    }
}

export default withStyles(styles)(connect(null, mapDispatchToProps)(Header));
