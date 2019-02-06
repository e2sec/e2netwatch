import React, { Component, Fragment } from 'react';


import connect from "react-redux/es/connect/connect";
import { withStyles } from '@material-ui/core/styles';

import Heading from '../../components/Heading/Heading';
import TabContainer from '../../components/TabContainer/TabContainer';
import TablePaginationActions from '../../components/TablePaginationActions/TablePaginationActions'
import { adminActions } from "../../store/actions/adminActions";

import { Paper, Tab, Tabs, Table, TableHead, TableBody, TableRow, TableCell, TableFooter, TablePagination, Button, IconButton, Dialog, DialogContent, DialogContentText, DialogTitle, TextField, InputLabel, Select, MenuItem, FormControl } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import {helpersActions} from "../../store/actions/helpersActions";


const styles = theme => ({
    paper: {
        height: '100%',
    },
    tabsRoot :{
        backgroundColor: theme.palette.primary.main,
        color: "white"
    },
    tabsIndicator: {
        backgroundColor: 'white',
        height: 3
    },
    table: {
        minWidth: 500,
        '& th':{
            paddingRight: theme.spacing.unit * 3
        },
        '& td':{
            paddingRight: theme.spacing.unit * 3,
            textTransform: "capitalize"
        }
    },
    gutter: {
        padding: theme.spacing.unit * 3
    },
    tableWrapper: {
        overflowX: 'auto',
        paddingBottom: 0
    },
    tableButton: {
        marginRight: theme.spacing.unit * 1.5
    },
    lastCell: {
        borderBottom: "none",
        paddingTop: theme.spacing.unit * 4
    },
    dialogTitle: {
        borderBottom: `1px solid ${theme.palette.divider}`,
        marginBottom: theme.spacing.unit * 2
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

class Administration extends Component {

    state = {
        tabValue: 0,
        isUserStatusDialogOpened: false,
        isDeleteDialogOpened: false,
        isEditUserDialogOpened: false,
        isAddUserDialogOpened: false,
        usersState: [],
        selectedUser: {
            username: "",
            email: "",
            firstName: "",
            lastName:"",
            userGroupId: "",
            userStatusId: ""
        },
        timezoneState: "",
        page: 0,
        rowsPerPage: 5,
    };


    componentDidMount() {
        this.props.getUsers();
        this.props.getUserGroups();
        this.props.getGlobalPreferences();
        this.props.getTimezones();
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if (this.props.users.data !== prevProps.users.data) {
            this.setState({ usersState: this.props.users.data});
        }

        if (this.props.globalPreferences.data !== prevProps.globalPreferences.data) {
            this.setState({ timezoneState: this.props.globalPreferences.data.timezone});
        }
    }

    handleTabChange = (event, tabValue) => {
        this.setState({ tabValue });
    };

    handleChangePage = (event, page) => {
        this.setState({ page });
    };

    handleChangeRowsPerPage = event => {
        this.setState({ rowsPerPage: event.target.value });
    };

    openUserStatusDialog = (userId) => (e) => {

        let selectedUser = this.props.users.data.filter( user => {
            return user.id === userId
        });

        this.setState({
            isUserStatusDialogOpened: true,
            selectedUser: selectedUser[0]
        });
    }

    closeUserStatusDialog = () => {
        this.setState({ isUserStatusDialogOpened: false });
    };

    changeUserStatus = () => {

        this.state.selectedUser.userStatusId === 1 ?
            this.props.deactivateUser(this.state.selectedUser.id) :
            this.props.activateUser(this.state.selectedUser.id);

        this.closeUserStatusDialog();
    }

    openDeleteDialog = (userId) => (e) => {

        let selectedUser = this.props.users.data.filter( user => {
            return user.id === userId
        })

        this.setState({
            isDeleteDialogOpened: true,
            selectedUser: selectedUser[0]
        });
    }

    closeDeleteDialog = () => {
        this.setState({ isDeleteDialogOpened: false });
    };

    deleteUser = () => {
        this.props.deleteUser(this.state.selectedUser.id);
        this.closeDeleteDialog();
    }

    openUserEditDialog = (userId) => (e) => {

        let selectedUser = this.props.users.data.filter( user => {
            return user.id === userId
        })

        this.setState({
            isEditUserDialogOpened: true,
            selectedUser: selectedUser[0]
        });
    }

    closeUserEditDialog = () => {
        this.setState({ isEditUserDialogOpened: false });
    };

    handleUserEdit = (e) => {
        this.setState({
            selectedUser: {
                ...this.state.selectedUser,
                [e.target.id]: e.target.value
            }
        })
    };

    handleUserSelectChange = (e) => {
        this.setState({
            selectedUser: {
                ...this.state.selectedUser,
                [e.target.name]: e.target.value
            }
        })
    }

    openAddUserDialog = () => {
        this.setState({
            isAddUserDialogOpened: true,
            selectedUser: {
                username: "",
                email: "",
                firstName: "",
                lastName:"",
                userGroupId: 1,
                userStatusId: 1
            },
        });
    }

    closeAddUserDialog = () => {
        this.setState({
            isAddUserDialogOpened: false,
        });
    }

    handlePreferencesChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    };

    updateGlobalPreferences = () => {
        this.props.updateGlobalPreferences(this.state.timezoneState)
    };

    updateUser = (e) => {
        e.preventDefault();
        this.props.updateUser(this.state.selectedUser)
        this.closeUserEditDialog();
    }

    addUser = (e) => {
        e.preventDefault();
        this.props.addUser(this.state.selectedUser)
        this.closeAddUserDialog();
    }

    render() {

        const { classes, users, userGroups, timezones } = this.props;
        const { tabValue, rowsPerPage, page, usersState, selectedUser, isUserStatusDialogOpened, isDeleteDialogOpened, isEditUserDialogOpened, isAddUserDialogOpened, timezoneState} = this.state;

        if (users.loading === true) return null;

        else {

            const emptyRows = rowsPerPage - Math.min(rowsPerPage, usersState.length - page * rowsPerPage);

            return(
                <Fragment>

                    <Heading title="Administration"/>

                    <Paper className={classes.paper}>
                        <Tabs classes={{ root: classes.tabsRoot, indicator: classes.tabsIndicator }}
                              value={tabValue}
                              onChange={this.handleTabChange}>

                            <Tab label="Users"/>
                            <Tab label="Global Preferences" />
                            <Tab label="Item Three" />

                        </Tabs>



                        {tabValue === 0 &&
                        <TabContainer>

                            <div className={classes.tableWrapper}>
                                <Table className={classes.table}>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Name</TableCell>
                                            <TableCell>Username</TableCell>
                                            <TableCell>Email</TableCell>
                                            <TableCell>User group</TableCell>
                                            <TableCell>User status</TableCell>
                                            <TableCell></TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {users.data && users.data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(user => (
                                            <TableRow key={user.id}>
                                                <TableCell>{user.firstName + " " + user.lastName}</TableCell>
                                                <TableCell>{user.username}</TableCell>
                                                <TableCell>{user.email}</TableCell>
                                                <TableCell>
                                                    {userGroups.data.map( (userGroup) => {
                                                            return userGroup.id === user.userGroupId ? userGroup.name : null;
                                                        }
                                                    )}
                                                </TableCell>
                                                <TableCell>{user.userStatusId === 1 ? "Active" : "Not active"}</TableCell>
                                                <TableCell align="right">
                                                    <Button variant="contained" color="primary"
                                                            className={classes.tableButton}
                                                            onClick={this.openUserStatusDialog(user.id)}>
                                                        {user.userStatusId === 1 ? "Deactivate" : "Activate"}
                                                    </Button>
                                                    <IconButton onClick={this.openUserEditDialog(user.id)} aria-label="Edit">
                                                        <EditIcon />
                                                    </IconButton>
                                                    <IconButton onClick={this.openDeleteDialog(user.id)} aria-label="Delete">
                                                        <DeleteIcon/>
                                                    </IconButton>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                        <TableRow>
                                            <TableCell colSpan={6} className={classes.lastCell}>
                                                <Button onClick={this.openAddUserDialog} variant="contained" color="primary">
                                                    Add New User
                                                </Button>
                                            </TableCell>
                                        </TableRow>
                                        {emptyRows > 0 && (
                                            <TableRow style={{ height: 48 * emptyRows }}>
                                                <TableCell colSpan={6} />
                                            </TableRow>
                                        )}
                                    </TableBody>
                                    <TableFooter>
                                        <TableRow>
                                            <TablePagination
                                                rowsPerPageOptions={[5, 10, 25]}
                                                colSpan={6}
                                                count={usersState.length}
                                                rowsPerPage={parseInt(rowsPerPage, 10)}
                                                page={page}
                                                SelectProps={{
                                                    native: true,
                                                }}
                                                onChangePage={this.handleChangePage}
                                                onChangeRowsPerPage={this.handleChangeRowsPerPage}
                                                ActionsComponent={TablePaginationActions}
                                            />
                                        </TableRow>
                                    </TableFooter>
                                </Table>
                            </div>

                        </TabContainer>
                        }


                        {tabValue === 1 &&
                        <TabContainer>
                            <div className={classes.gutter}>
                                <FormControl>
                                    <InputLabel htmlFor="timezone">Timezone</InputLabel>
                                    <Select
                                        value={timezoneState}
                                        name="timezoneState"
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
                                <Button variant="contained" color="primary" onClick={this.updateGlobalPreferences}>
                                    Save
                                </Button>

                            </div>
                        </TabContainer>
                        }
                        {tabValue === 2 && <TabContainer>Item Three</TabContainer>}
                    </Paper>


                    <Dialog
                        open={isEditUserDialogOpened}
                        onClose={this.closeUserEditDialog}
                        aria-labelledby="form-dialog-title"
                        maxWidth="xs"
                    >
                        <DialogTitle id="form-dialog-title" className={classes.dialogTitle}>Edit {selectedUser.username}</DialogTitle>
                        <DialogContent>
                            <form onSubmit={this.updateUser}>
                                <TextField
                                    id="username"
                                    label="Username"
                                    value={selectedUser.username}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <TextField
                                    id="email"
                                    label="Email"
                                    value={selectedUser.email}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
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
                                    value={selectedUser.firstName}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
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
                                    value={selectedUser.lastName}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <FormControl margin="dense" fullWidth={true}>
                                    <InputLabel>User group</InputLabel>
                                    <Select
                                        value={selectedUser.userGroupId}
                                        name="userGroupId"
                                        onChange={this.handleUserSelectChange}
                                    >
                                        {userGroups.data && userGroups.data.map( (userGroup) => (
                                                <MenuItem key={userGroup.id} value={userGroup.id}>{userGroup.name}</MenuItem>
                                            )
                                        )}
                                    </Select>
                                </FormControl>

                                <FormControl margin="dense" fullWidth={true}>
                                    <InputLabel>User status</InputLabel>
                                    <Select
                                        value={selectedUser.userStatusId}
                                        name="userStatusId"
                                        onChange={this.handleUserSelectChange}
                                    >

                                        <MenuItem value="1">Active</MenuItem>
                                        <MenuItem value="2">Not active</MenuItem>


                                    </Select>
                                </FormControl>

                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Save
                                    </Button>
                                    <Button onClick={this.closeUserEditDialog} variant="contained" color="primary">
                                        Cancel
                                    </Button>
                                </div>
                            </form>
                        </DialogContent>
                    </Dialog>


                    <Dialog
                        open={isAddUserDialogOpened}
                        onClose={this.closeAddUserDialog}
                        aria-labelledby="form-dialog-title"
                        maxWidth="xs"
                    >
                        <DialogTitle id="form-dialog-title" className={classes.dialogTitle}>Add new user</DialogTitle>
                        <DialogContent>
                            <form onSubmit={this.addUser}>
                                <TextField
                                    id="username"
                                    label="Username"
                                    value={selectedUser.username}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <TextField
                                    id="email"
                                    label="Email"
                                    value={selectedUser.email}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
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
                                    value={selectedUser.firstName}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
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
                                    value={selectedUser.lastName}
                                    fullWidth={true}
                                    required={true}
                                    margin="dense"
                                    InputProps={{
                                        classes: {
                                            root: classes.input,
                                        }
                                    }}
                                    onChange={this.handleUserEdit}
                                />
                                <FormControl margin="dense" fullWidth={true}>
                                    <InputLabel>User group</InputLabel>
                                    <Select
                                        value={selectedUser.userGroupId}
                                        name="userGroupId"
                                        onChange={this.handleUserSelectChange}
                                    >
                                        {userGroups.data && userGroups.data.map( (userGroup) => (
                                                <MenuItem key={userGroup.id} value={userGroup.id}>{userGroup.name}</MenuItem>
                                            )
                                        )}
                                    </Select>
                                </FormControl>

                                <FormControl margin="dense" fullWidth={true}>
                                    <InputLabel>User status</InputLabel>
                                    <Select
                                        value={selectedUser.userStatusId}
                                        name="userStatusId"
                                        onChange={this.handleUserSelectChange}
                                    >

                                        <MenuItem value="1">Active</MenuItem>
                                        <MenuItem value="2">Not active</MenuItem>


                                    </Select>
                                </FormControl>

                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Add
                                    </Button>
                                    <Button onClick={this.closeAddUserDialog} variant="contained" color="primary">
                                        Cancel
                                    </Button>
                                </div>
                            </form>
                        </DialogContent>
                    </Dialog>


                    <Dialog
                        open={isUserStatusDialogOpened}
                        onClose={this.closeUserStatusDialog}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                    >
                        <DialogContent>
                            <DialogContentText>
                                Are you sure that you want to {selectedUser.userStatusId === 1 ? "deactivate" : "activate"} {selectedUser.username}?
                            </DialogContentText>
                            <div className={classes.dialogButtons}>
                                <Button onClick={this.changeUserStatus} variant="contained" color="primary">
                                    {selectedUser.userStatusId === 1 ? "Deactivate" : "Activate"}
                                </Button>
                                <Button onClick={this.closeUserStatusDialog} variant="contained" color="primary">
                                    Cancel
                                </Button>
                            </div>
                        </DialogContent>
                    </Dialog>

                    <Dialog
                        open={isDeleteDialogOpened}
                        onClose={this.closeDeleteDialog}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                    >
                        <DialogContent>
                            <DialogContentText>
                                Are you sure that you want to delete {selectedUser.username}?
                            </DialogContentText>
                            <div className={classes.dialogButtons}>
                                <Button onClick={this.deleteUser} variant="contained" color="primary">
                                    Delete
                                </Button>
                                <Button onClick={this.closeDeleteDialog} variant="contained"  color="primary">
                                    Cancel
                                </Button>
                            </div>
                        </DialogContent>
                    </Dialog>

                </Fragment>
            )
        }
    }
}

const mapStateToProps = (state) => {
    return {
        users: state.admin.users,
        userGroups: state.admin.userGroups,
        globalPreferences: state.admin.globalPreferences,
        timezones: state.helpers.timezones,
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        getUsers: () => dispatch(adminActions.getUsers()),
        getUserGroups: () => dispatch(adminActions.getUserGroups()),
        activateUser: (userId) => dispatch(adminActions.activateUser(userId)),
        deactivateUser: (userId) => dispatch(adminActions.deactivateUser(userId)),
        deleteUser: (userId) => dispatch(adminActions.deleteUser(userId)),
        updateUser: (user) => dispatch(adminActions.updateUser(user)),
        addUser: (user) => dispatch(adminActions.addUser(user)),
        getGlobalPreferences: () => dispatch(adminActions.getGlobalPreferences()),
        updateGlobalPreferences: (globalPreferences) => dispatch(adminActions.updateGlobalPreferences(globalPreferences)),
        getTimezones: () => dispatch(helpersActions.getTimezones()),
    }
}

export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Administration));
