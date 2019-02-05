import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';

import { authActions}  from '../../../store/actions/authActions';
import { helpers } from './../../../helpers/helpers'

import { withStyles } from '@material-ui/core/styles';
import {AppBar, Toolbar, Typography, IconButton, Menu, MenuItem, ListItemText, ListItemIcon } from '@material-ui/core';

import MenuIcon from '@material-ui/icons/Menu';

import NotificationIcon from '@material-ui/icons/NotificationsNone';
import AccountIcon from '@material-ui/icons/AccountCircle';
import LogoutIcon from '@material-ui/icons/ExitToApp';
import AdministrationIcon from '@material-ui/icons/Settings';

import Fab from "@material-ui/core/es/Fab/Fab";


const styles = theme => ({
    appBar: {
        marginLeft: '20%',
        [theme.breakpoints.up('md')]: {
            width: `calc(100% - 20%)`,
        },
    },
    toolbar: {
        paddingTop: theme.spacing.unit * 1.5,
        paddingBottom: theme.spacing.unit * 1.5,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
        [theme.breakpoints.up('md')]: {
            display: 'none',
        },
    },
    profileButton: {
        marginLeft: theme.spacing.unit * 1.5,
        width: 52,
        height: 52,
        fontSize: 20,
        letterSpacing: 1,
        background: 'rgba(38, 166, 91, 1)',
        '&:hover':{
            background: 'rgba(38, 166, 91, .8)',
        }
    },
    grow: {
        flexGrow: 1,
    },
    notification: {
        fontSize: 28
    }
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
                    <div className={classes.grow}></div>

                    <Fragment>
                        <IconButton color="inherit">
                            <NotificationIcon className={classes.notification} />
                        </IconButton>
                        <Fab
                             color="inherit"
                             className={classes.profileButton}
                             aria-owns={anchorEl ? 'dropdown' : undefined}
                             aria-haspopup="true"
                             onClick={this.openDropdown}
                        >
                            UU
                        </Fab>

                        <Menu id="dropdown"
                              anchorEl={anchorEl}
                              open={Boolean(anchorEl)}
                              onClose={this.closeDropdown}
                        >
                            <MenuItem component={Link} to="/profile" onClick={this.closeDropdown}>
                                <ListItemIcon>
                                    <AccountIcon />
                                </ListItemIcon>
                                <ListItemText primary="Profile" />
                            </MenuItem>

                            {
                                helpers.getRole() === 'ROLE_ADMIN' ? (
                                    <MenuItem component={Link} to="/administration" onClick={this.closeDropdown}>
                                        <ListItemIcon>
                                            <AdministrationIcon />
                                        </ListItemIcon>
                                        <ListItemText primary="Administrator" />
                                    </MenuItem>) : ''
                            }

                            <MenuItem component={Link} to="/login" onClick={this.logout}>
                                <ListItemIcon>
                                    <LogoutIcon />
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
        logout: () => dispatch(authActions.logout())
    }
}

export default withStyles(styles)(connect(null, mapDispatchToProps)(Header));
