import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

import { withStyles } from '@material-ui/core/styles';
import { Drawer, List, ListItem, ListItemIcon, ListItemText, Divider} from '@material-ui/core';
import DashboardIcon from '@material-ui/icons/Dashboard';
import ListIcon from '@material-ui/icons/List';


import logo from '../../../assets/images/logo.png';


const styles = theme => ({
    drawer: {
        [theme.breakpoints.up('md')]: {
            width: '20%',
            flexShrink: 0,
        },
    },
    drawerPaper: {
        width: '20%',
    },
    logo: {
        '& > img': {
            height: 60,
            display: 'block',
            margin: '8px auto'
        }
    },
    list: {
        paddingTop: theme.spacing.unit * 3,
    },
    listItem: {
        paddingLeft: theme.spacing.unit * 3,
    },
    linkText: {
        padding: 0,
    }
});




class Sidebar extends Component{

    state = {
        isDesktop: false
    };

    componentDidMount() {
        this.getViewportWidth();
        window.addEventListener("resize", this.getViewportWidth);
    }

    componentWillUnmount() {
        window.removeEventListener("resize", this.getViewportWidth);
    }

    getViewportWidth = () => {
        this.setState({
            isDesktop: window.innerWidth > 968
        });
    }

    handleMenuClick = () => {
        this.props.handleDrawerToggle();
    };

    render() {

        const { isDesktop } = this.state;
        const { classes, isOpen } = this.props;

        return(

            <nav className={classes.drawer}>

                <Drawer
                    variant={isDesktop ? "permanent" : "temporary"}
                    open={isOpen}
                    onClose={this.handleMenuClick}
                    classes={{
                        paper: classes.drawerPaper,
                    }}
                >
                    <div className={classes.logo}>
                        <img src={logo} alt="logo"/>
                    </div>
                    <Divider/>


                    <List className={classes.list}>

                        <ListItem component={NavLink} to='/' className={classes.listItem}>
                            <ListItemIcon><DashboardIcon/></ListItemIcon>
                            <ListItemText primary="Dashboard" className={classes.linkText}/>
                        </ListItem>
                        <ListItem component={NavLink} to='/menu-item-1' className={classes.listItem}>
                            <ListItemIcon><ListIcon/></ListItemIcon>
                            <ListItemText primary="Menu Item 1" className={classes.linkText}/>
                        </ListItem>
                        <ListItem component={NavLink} to='/menu-item-2' className={classes.listItem}>
                            <ListItemIcon><ListIcon/></ListItemIcon>
                            <ListItemText primary="Menu Item 2" className={classes.linkText}/>
                        </ListItem>
                        <ListItem component={NavLink} to='/menu-item-3' className={classes.listItem}>
                            <ListItemIcon><ListIcon/></ListItemIcon>
                            <ListItemText primary="Menu Item 3" className={classes.linkText}/>
                        </ListItem>

                    </List>


                </Drawer>

            </nav>

        )
    }
}

export default withStyles(styles)(Sidebar);
