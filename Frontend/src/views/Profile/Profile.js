import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';

import { userActions } from '../../store/actions/userActions';

import TabContainer from '../../components/TabContainer/TabContainer'

import { withStyles } from '@material-ui/core/styles';
import { Grid, Paper, TextField, FormControl, FormControlLabel, InputLabel, MenuItem, Select, Switch, Typography, Button, Tabs, Tab, Fab, Avatar, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from '@material-ui/core'
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
        marginTop: theme.spacing.unit * 3,
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
        dataToUpdateProfile: {
            email: "",
            firstName: "",
            lastName: ""
        },
        pass: {
            password: "",
            repeatedPassword: ""
        },
        userTimezone: "",
    };

    componentDidMount() {
        this.props.getUserProfile();
        this.props.getUserPreferences();
        this.props.getTimezones();
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if (this.props.userProfile !== prevProps.userProfile) {
            this.setState({ dataToUpdateProfile: this.props.userProfile});
        }

        if (this.props.userPreferences !== prevProps.userPreferences) {
            this.setState({ userTimezone: this.props.userPreferences.timezone});
        }
    }

    // edit userProfile
    openEditProfile = () => {
        this.setState({ isEditProfileOpened: true });
    };

    closeEditProfile = () => {
        this.setState({
            isEditProfileOpened: false,
            dataToUpdateProfile: this.props.userProfile
        });
    };

    editUserProfile = (e) => {
        this.setState({
            dataToUpdateProfile: {
                ...this.state.dataToUpdateProfile,
                [e.target.id]: e.target.value
            }
        })
    };

    updateUserProfile = (e) => {
        e.preventDefault();
        this.props.updateUserProfile(this.state.dataToUpdateProfile);
        this.closeEditProfile();
    }


    // change pass
    openChangePass = () => {
        this.setState({ isChangePassOpened: true });
    };

    closeChangePass = (e) => {
        this.setState({
            isChangePassOpened: false,
            stateUser: this.props.user
        });
    };

    editPass = (e) => {
        this.setState({
            pass: {
                ...this.state.pass,
                [e.target.id]: e.target.value
            }
        })
    };

    changeUserPass = (e) => {
        e.preventDefault();
        this.props.changeUserPass(this.state.pass)
        this.closeChangePass();
    }

    // update userProfile preferences
    updateUserPreferences = () => {
        this.props.updateUserPreferences(this.state.userTimezone)
    };

    handleTimezoneChange = (e) => {
        this.setState({ userTimezone: e.target.value });
    };

    // tab changing
    handleTabChange = (event, tabValue) => {
        this.setState({ tabValue });
    };


    render(){

        const { classes, userProfile, timezones } = this.props;
        const { tabValue, dataToUpdateProfile, userTimezone } = this.state;

        if (!this.props.userProfile || !this.props.timezones || !this.props.userPreferences) return null;

        else {

            console.log(this.props.userPreferences)
            console.log(this.props.timezones)
            console.log(this.state.userTimezone)

            return(
                <Fragment>

                    <Heading title="Profile"/>

                    <Grid container spacing={24}>
                        <Grid item xs={4}>
                            <Paper className={classes.paper}>

                                <Toolbar className={classes.toolbar}>
                                    <Avatar  src={profilePlaceholder} className={classes.avatar} />
                                    <div>
                                        <Typography color="inherit" variant="subtitle1">{userProfile.userGroups[0].name}</Typography>
                                        <Typography color="inherit" variant="h5">{userProfile.username}</Typography>
                                    </div>

                                </Toolbar>

                                <div className={classes.gutter}>

                                    <Typography variant="subtitle1" className={classes.profileLabel}>Email</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.email}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>First name</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.firstName}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>Last name</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.lastName}</Typography>
                                    <Typography variant="subtitle1" className={classes.profileLabel}>Status</Typography>
                                    <Typography variant="body1" className={classes.profileDetail}>{userProfile.userStatus.name}</Typography>

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

                                        <FormControl className={classes.formControl}>
                                            <InputLabel htmlFor="timezone">Timezone</InputLabel>
                                            <Select
                                                value={userTimezone}
                                                autoWidth={true}
                                                onChange={this.handleTimezoneChange}
                                                inputProps={{
                                                    name: 'timezone',
                                                    id: 'timezone',
                                                }}
                                            >
                                                { timezones.map( timezone => (
                                                    <MenuItem key={timezone.id} value={timezone.id}>{timezone.name}</MenuItem>
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
                            <form onSubmit={this.updateUserProfile}>
                                <TextField
                                    id="email"
                                    label="Email"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={dataToUpdateProfile.email}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.editUserProfile}
                                />
                                <TextField
                                    id="firstName"
                                    label="First Name"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={dataToUpdateProfile.firstName}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.editUserProfile}
                                />
                                <TextField
                                    id="lastName"
                                    label="Last Name"
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    value={dataToUpdateProfile.lastName}
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.editUserProfile}
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
                            <form onSubmit={this.changeUserPass}>
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
                                    inputProps={{minlength:"8"}}
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
                                    inputProps={{minlength:"8"}}
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
        userProfile: state.user.userProfile,
        userPreferences: state.user.userPreferences,
        timezones: state.user.timezones
    }
}

const mapDispatchToProps = (dispatch) => {

    return {

        getUserProfile: () => dispatch(userActions.getUserProfile()),
        updateUserProfile: (userProfile) => dispatch(userActions.updateUserProfile(userProfile)),

        getUserPreferences: () => dispatch(userActions.getUserPreferences()),
        updateUserPreferences: (userPreferences) => dispatch(userActions.updateUserPreferences(userPreferences)),

        changeUserPass: (pass) => dispatch(userActions.changeUserPass(pass)),

        getTimezones: () => dispatch(userActions.getTimezones()),
    }
}


export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Profile));