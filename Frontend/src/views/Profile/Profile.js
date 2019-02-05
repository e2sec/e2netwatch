import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';

import { userActions } from '../../store/actions/userActions';
import { helpersActions } from '../../store/actions/helpersActions';

import TabContainer from '../../components/TabContainer/TabContainer'

import { withStyles } from '@material-ui/core/styles';
import { Grid, Paper, TextField, FormControl, InputLabel, MenuItem, Select, Switch, Typography, Button, Tabs, Tab, Avatar, Dialog,  DialogContent,  DialogTitle} from '@material-ui/core'
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
    }
});


class Profile extends Component {

    state = {
        tabValue: 0,
        isEditProfileOpened: false,
        isChangePassOpened: false,
        userProfileState: {
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
        this.props.getTimezones();
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if (this.props.userProfile.data !== prevProps.userProfile.data) {
            this.setState({ userProfileState: this.props.userProfile.data});
        }

        if (this.props.userPreferences.data !== prevProps.userPreferences.data) {
            this.setState({ userTimezoneState: this.props.userPreferences.data.timezone});
        }
    }

    // edit userProfile
    openEditProfile = () => {
        this.setState({ isEditProfileOpened: true });
    };

    closeEditProfile = () => {
        this.setState({
            isEditProfileOpened: false,
            userProfileState: this.props.userProfile.data
        });
    };

    editProfile = (e) => {
        this.setState({
            userProfileState: {
                ...this.state.userProfileState,
                [e.target.id]: e.target.value
            }
        })
    };

    updateUser = (e) => {
        e.preventDefault();
        this.props.updateUser(this.state.userProfileState);
        this.closeEditProfile();
    };


    // change pass
    openChangePass = () => {
        this.setState({ isChangePassOpened: true });
    };

    closeChangePass = () => {
        this.setState({ isChangePassOpened: false });
    };

    editPass = (e) => {
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
        this.closeChangePass();
    }

    // update userProfile preferences
    updateUserPreferences = () => {
        this.props.updateUserPreferences(this.state.userTimezoneState)
    };

    editTimezone = (e) => {
        this.setState({ userTimezoneState: e.target.value });
    };

    // tab changing
    handleTabChange = (event, tabValue) => {
        this.setState({ tabValue });
    };


    render(){

        const { classes, userProfile, timezones, userPreferences } = this.props;
        const { tabValue, userProfileState, userTimezoneState } = this.state;

        if (userProfile.loading === true || timezones.loading === true || userPreferences.loading === true || userProfile.data.hasOwnProperty('userStatus') === false) return null

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
                                        <Typography color="inherit" variant="subtitle1">Administrator</Typography>
                                        <Typography color="inherit" variant="h5">{userProfile.data.username}</Typography>
                                    </div>

                                </Toolbar>

                                <div className={classes.gutter}>

                                    <Typography variant="subtitle1" className={classes.profileLabel}>Email</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.data.email}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>First name</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.data.firstName}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>Last name</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.data.lastName}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>Status</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.data.userStatus.name}</Typography>

                                    <Button color="primary"
                                            size="large"
                                            className={`${classes.button} ${classes.linkButton}`}
                                            disableRipple={true}
                                            onClick={this.openChangePass}
                                    >
                                        Change Password
                                    </Button>
                                    <br/>
                                    <Button variant="contained"
                                            color="primary"
                                            size="large"
                                            className={classes.button}
                                            onClick={this.openEditProfile}
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

                                <div className={classes.gutter}>

                                    {tabValue === 0 &&
                                    <TabContainer>

                                        <FormControl>
                                            <InputLabel htmlFor="timezone">Timezone</InputLabel>
                                            <Select
                                                value={userTimezoneState}
                                                autoWidth={true}
                                                onChange={this.editTimezone}
                                                inputProps={{
                                                    name: 'timezone',
                                                    id: 'timezone',
                                                }}
                                            >
                                                {timezones.data && timezones.data.map( (timezone, index) => (
                                                        <MenuItem key={index} value={timezone.id}>{timezone.name}</MenuItem>
                                                    )
                                                )}
                                            </Select>
                                        </FormControl>
                                        <br/>
                                        <br/>
                                        <Button variant="contained" color="primary" onClick={this.updateUserPreferences}>
                                            Save
                                        </Button>
                                    </TabContainer>
                                    }
                                    {tabValue === 1 && <TabContainer>Item Two</TabContainer>}
                                    {tabValue === 2 && <TabContainer>Item Three</TabContainer>}

                                </div>
                            </Paper>
                        </Grid>
                    </Grid>


                    <Dialog
                        open={this.state.isEditProfileOpened}
                        onClose={this.closeEditProfile}
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
                                    value={userProfileState.email}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.editProfile}
                                />
                                <TextField
                                    id="firstName"
                                    label="First Name"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={userProfileState.firstName}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.editProfile}
                                />
                                <TextField
                                    id="lastName"
                                    label="Last Name"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={userProfileState.lastName}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.editProfile}
                                />
                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Save
                                    </Button>
                                    <Button onClick={this.closeEditProfile} variant="contained" color="primary">
                                        Cancel
                                    </Button>
                                </div>
                            </form>
                        </DialogContent>

                    </Dialog>

                    <Dialog
                        open={this.state.isChangePassOpened}
                        onClose={this.closeChangePass}
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
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    inputProps={{minLength:"8"}}
                                    onChange={this.editPass}
                                />

                                <TextField
                                    id="repeatedPassword"
                                    label="Repeat new password"
                                    type="password"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    inputProps={{minLength:"8"}}
                                    onChange={this.editPass}
                                />

                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Save
                                    </Button>
                                    <Button onClick={this.closeChangePass} variant="contained" color="primary">
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
        userProfile: state.user.profile,
        userPreferences: state.user.preferences,
        timezones: state.helpers.timezones
    }
}

const mapDispatchToProps = (dispatch) => {

    return {

        getUser: () => dispatch(userActions.getUser()),
        updateUser: (userProfile) => dispatch(userActions.updateUser(userProfile)),

        getUserPreferences: () => dispatch(userActions.getUserPreferences()),
        updateUserPreferences: (userPreferences) => dispatch(userActions.updateUserPreferences(userPreferences)),

        changePassword: (pass) => dispatch(userActions.changePassword(pass)),

        getTimezones: () => dispatch(helpersActions.getTimezones()),
    }
}


export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Profile));