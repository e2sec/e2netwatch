import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';

import { userActions } from '../../store/actions/userActions';
import { adminActions } from '../../store/actions/adminActions';
import { helpersActions } from '../../store/actions/helpersActions';

import TabContainer from '../../components/TabContainer/TabContainer'

import { withStyles } from '@material-ui/core/styles';
import {
    Grid,
    Paper,
    TextField,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Switch,
    Typography,
    Button,
    Tabs,
    Tab,
    Avatar,
    Dialog,
    DialogContent,
    DialogTitle,
} from '@material-ui/core'
import Toolbar from "@material-ui/core/es/Toolbar/Toolbar";

import profilePlaceholder from '../../assets/images/profile-placeholder.png';
import Heading from "../../components/Heading/Heading";

const styles = theme => ({
    toolbar: {
        backgroundColor: theme.palette.primary.main,
        paddingTop: theme.spacing.unit * 1.5,
        paddingBottom: theme.spacing.unit * 1.5,
        color: "white"
    },
    avatar: {
        marginRight: theme.spacing.unit * 3,
        height: 64,
        width: 64
    },
    paper: {
        height: '100%',
    },
    profileDetail: {
        borderBottom: '1px solid rgba(0, 0, 0, 0.42)',
        marginBottom: 8,
        paddingBottom: 4,
    },
    profileLabel: {
        color: 'rgba(0, 0, 0, 0.38)'
    },
    switchBase: {
        color: "gray",
        '&$switchChecked': {
            color: "green",
            '& + $switchBar': {
                backgroundColor: 'green',
            },
        },
    },
    switchBar: {
        borderRadius: 13,
        width: 50,
        height: 24,
        marginTop: -12,
        marginLeft: -19,
        border: 'solid 1px',
    },
    switchIcon: {
        width: 22,
        height: 22,
        color: 'white'
    },
    switchChecked: {
        width: 72,
        '& + $switchBar': {
            opacity: 1,
            border: 'none',
        },
    },
    button: {
        margin: theme.spacing.unit,
        marginLeft: 0
    },
    tabsRoot: {
        backgroundColor: theme.palette.primary.main,
        color: "white"
    },
    tabsIndicator: {
        backgroundColor: 'white',
        height: 3
    },
    gutter: {
        padding: theme.spacing.unit * 3
    },
    dialogTitle: {
        borderBottom: `1px solid ${theme.palette.divider}`,
        marginBottom: theme.spacing.unit * 2
    },
    linkButton: {
        paddingLeft: 0,
        paddingBottom: 0,
        textTransform: 'inherit',
        '&:hover': {
            background: 'transparent',
            textDecoration: 'underline'
        }
    },
    dialogButtons: {
        marginTop: theme.spacing.unit * 4,
        display: 'flex',
        justifyContent: 'flex-end',
        '& > button': {
            marginLeft: theme.spacing.unit
        }
    },
    listItem: {
        color: 'rgba(0, 0, 0, 0.87)',
        width: 'auto',
        height: '24px',
        overflow: 'hidden',
        fontSize: '1rem',
        boxSizing: 'content-box',
        fontWeight: '400',
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        lineHeight: '1.5em',
        whiteSpace: 'nowrap',
        paddingLeft: theme.spacing.unit * 2,
        paddingRight: theme.spacing.unit * 2,
        paddingTop: '11px',
        paddingBottom: '11px',
        cursor: 'pointer',
        '&:focus': {
            backgroundColor: 'rgba(0, 0, 0, 0.14)',
            outline: 'none'
        },
        '&:hover': {
            backgroundColor: 'rgba(0, 0, 0, 0.08)',
        }
    }
});


class Profile extends Component {

    state = {
        tabValue: 0,
        isUserEditOpened: false,
        isPassChangeOpened: false,
        userState: {
            email: "",
            firstName: "",
            lastName: "",
        },
        passState: {
            password: "",
            repeatedPassword: ""
        },
        userTimezoneState: "",
    };

    componentDidMount() {
        this.props.getUser();
        this.props.getUserPreferences();
        this.props.getUserGroups();
        this.props.getTimezones();
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if (this.props.user.data !== prevProps.user.data) {
            this.setState({ userState: this.props.user.data});
        }

        if (this.props.userPreferences.data !== prevProps.userPreferences.data) {
            this.setState({ userTimezoneState: this.props.userPreferences.data.timezone});
        }
    }

    // edit user
    openUserEdit = () => {
        this.setState({
            isUserEditOpened: true,
            userState: this.props.user.data
        });
    };

    closeUserEdit = () => {
        this.setState({isUserEditOpened: false });
    };

    handleUserEdit = (e) => {
        this.setState({
            userState: {
                ...this.state.userState,
                [e.target.id]: e.target.value
            }
        })
    };

    updateUser = (e) => {
        e.preventDefault();
        this.props.updateUser(this.state.userState);
        this.closeUserEdit();
    };


    // change pass
    openPassChange = () => {
        this.setState({ isPassChangeOpened: true });
    };

    closePassChange = () => {
        this.setState({ isPassChangeOpened: false });
    };

    handlePassEdit = (e) => {
        this.setState({
            passState: {
                ...this.state.passState,
                [e.target.id]: e.target.value
            }
        })
    };

    changePassword = (e) => {
        e.preventDefault();
        this.props.changePassword(this.state.passState)
        this.closePassChange();
    }

    // update user preferences
    updateUserPreferences = () => {
        this.props.updateUserPreferences(this.state.userTimezoneState)
    };

    handlePreferencesChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    };

    // tab changing
    handleTabChange = (event, tabValue) => {
        this.setState({ tabValue });
    };


    render(){

        const { classes, user, timezones, userPreferences, userGroups } = this.props;
        const { tabValue, userState, userTimezoneState } = this.state;

        if (user.loading === true || timezones.loading === true || userPreferences.loading === true) return null

        else {
            return(
                <Fragment>

                    <Heading title="Profile"/>

                    <Grid container spacing={24}>
                        <Grid item xs={4}>
                            <Paper className={classes.paper}>

                                <Toolbar className={classes.toolbar}>
                                    <Avatar  src={profilePlaceholder} className={classes.avatar} />
                                    <div>
                                        <Typography color="inherit" variant="subtitle1">
                                            {userGroups.data.map( (userGroup) => {
                                                    return  userGroup.id === user.data.userGroupId ? userGroup.name : null
                                                }
                                            )}
                                        </Typography>
                                        <Typography color="inherit" variant="h5">{user.data.username}</Typography>
                                    </div>

                                </Toolbar>

                                <div className={classes.gutter}>

                                    <Typography variant="subtitle1" className={classes.profileLabel}>Email</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{user.data.email}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>First name</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{user.data.firstName}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>Last name</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{user.data.lastName}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>Status</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{user.data.userStatusId === 1 ? "Active" : "Not active"}</Typography>

                                    <Button color="primary"
                                            size="large"
                                            className={`${classes.button} ${classes.linkButton}`}
                                            disableRipple={true}
                                            onClick={this.openPassChange}
                                    >
                                        Change Password
                                    </Button>
                                    <br/>
                                    <Button variant="contained"
                                            color="primary"
                                            size="large"
                                            className={classes.button}
                                            onClick={this.openUserEdit}
                                    >
                                        Edit Profile
                                    </Button>

                                    {/*<FormControlLabel
                                    control={
                                        <Switch
                                            value="checkedC"
                                            classes={{
                                                switchBase: classes.switchBase,
                                                bar: classes.switchBar,
                                                icon: classes.switchIcon,
                                                checked: classes.switchChecked
                                            }}
                                        />
                                    }
                                />*/}

                                </div>

                            </Paper>
                        </Grid>
                        <Grid item xs={8}>
                            <Paper className={classes.paper}>
                                <Tabs classes={{ root: classes.tabsRoot, indicator: classes.tabsIndicator }}
                                      value={tabValue}
                                      onChange={this.handleTabChange}>

                                    <Tab label="Profile Preferences"/>
                                    <Tab label="Item Two" />
                                    <Tab label="Item Three" />

                                </Tabs>



                                {tabValue === 0 &&
                                <TabContainer>
                                    <div className={classes.gutter}>
                                        <FormControl>
                                            <InputLabel htmlFor="timezone">Timezone</InputLabel>
                                            <Select
                                                value={userTimezoneState}
                                                name="userTimezoneState"
                                                autoWidth={true}
                                                onChange={this.handlePreferencesChange}
                                            >
                                                {timezones.data && timezones.data.map( (timezone, index) => (
                                                        <li className={classes.listItem} key={index} value={timezone.id}>{timezone.name}</li>
                                                    )
                                                )}
                                            </Select>
                                        </FormControl>
                                        <br/>
                                        <br/>
                                        <Button variant="contained" color="primary" onClick={this.updateUserPreferences}>
                                            Save
                                        </Button>
                                    </div>
                                </TabContainer>}

                                {tabValue === 1 && <TabContainer>Item Two</TabContainer>}
                                {tabValue === 2 && <TabContainer>Item Three</TabContainer>}


                            </Paper>
                        </Grid>
                    </Grid>


                    <Dialog
                        open={this.state.isUserEditOpened}
                        onClose={this.closeUserEdit}
                        aria-labelledby="form-dialog-title"
                        maxWidth="xs"
                    >
                        <DialogTitle id="form-dialog-title" className={classes.dialogTitle}>Edit profile</DialogTitle>
                        <DialogContent>
                            <form onSubmit={this.updateUser}>
                                <TextField
                                    id="email"
                                    label="Email"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={userState.email}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <TextField
                                    id="firstName"
                                    label="First Name"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={userState.firstName}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <TextField
                                    id="lastName"
                                    label="Last Name"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={userState.lastName}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Save
                                    </Button>
                                    <Button onClick={this.closeUserEdit} variant="contained" color="primary">
                                        Cancel
                                    </Button>
                                </div>
                            </form>
                        </DialogContent>

                    </Dialog>

                    <Dialog
                        open={this.state.isPassChangeOpened}
                        onClose={this.closePassChange}
                        aria-labelledby="form-dialog-title"
                        maxWidth="xs"
                    >
                        <DialogTitle id="form-dialog-title" className={classes.dialogTitle}>Change password</DialogTitle>
                        <DialogContent>
                            <form onSubmit={this.changePassword}>
                                <TextField
                                    id="password"
                                    label="New password"
                                    type="password"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    inputProps={{
                                        minLength:"8",
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handlePassEdit}
                                />

                                <TextField
                                    id="repeatedPassword"
                                    label="Repeat new password"
                                    type="password"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    inputProps={{
                                        minLength:"8",
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handlePassEdit}
                                />

                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Save
                                    </Button>
                                    <Button onClick={this.closePassChange} variant="contained" color="primary">
                                        Cancel
                                    </Button>
                                </div>
                            </form>
                        </DialogContent>
                    </Dialog>

                </Fragment>

            )
        }
    }

}

const mapStateToProps = (state) => {

    return {
        user: state.user.user,
        userPreferences: state.user.preferences,
        userGroups: state.admin.userGroups,
        timezones: state.helpers.timezones,
    }
}

const mapDispatchToProps = (dispatch) => {

    return {

        getUser: () => dispatch(userActions.getUser()),
        updateUser: (user) => dispatch(userActions.updateUser(user)),
        getUserPreferences: () => dispatch(userActions.getUserPreferences()),
        updateUserPreferences: (userPreferences) => dispatch(userActions.updateUserPreferences(userPreferences)),
        changePassword: (pass) => dispatch(userActions.changePassword(pass)),
        getUserGroups: () => dispatch(adminActions.getUserGroups()),
        getTimezones: () => dispatch(helpersActions.getTimezones()),
    }
}


export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Profile));