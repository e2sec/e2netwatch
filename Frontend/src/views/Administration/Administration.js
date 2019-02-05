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
            paddingRight: theme.spacing.unit * 3
        }
    },
    tableWrapper: {
        overflowX: 'auto',
        padding: theme.spacing.unit * 3,
        paddingBottom: 0
    },
    actionCell: {
        paddingRight: '0px !important',
    },
    tableButton: {
        marginRight: theme.spacing.unit * 1.5
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
    }
});

class Administration extends Component {

    state = {
        tabValue: 0,
        isUserStatusDialogOpened: false,
        isDeleteDialogOpened: false,
        isEditUserDialogOpened: false,
        usersState: [],
        selectedUser: {
            username: "",
            email: "",
            firstName: "",
            lastName:"",
            userGroups: "",
            userStatus: {}
        },
        page: 0,
        rowsPerPage: 5,
    };


    componentDidMount() {
        this.props.getUsers()
        this.props.getUserGroups()
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if (this.props.users.data !== prevProps.users.data) {
          this.setState({ usersState: this.props.users.data});
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

        this.state.selectedUser.userStatus.name === "Active" ?
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
        this.props.deleteUser(this.state.selectedUser.id)
        this.closeDeleteDialog();
    }

    openEditUserDialog = (userId) => (e) => {

        let selectedUser = this.props.users.data.filter( user => {
            return user.id === userId
        })

        this.setState({
            isEditUserDialogOpened: true,
            selectedUser: selectedUser[0]
        });
    }

    closeEditUserDialog = () => {
        this.setState({ isEditUserDialogOpened: false });
    };

    editUser = (e) => {
        this.setState({
            selectedUser: {
                ...this.state.selectedUser,
                [e.target.id]: e.target.value
            }
        })
    };

    render() {

        const { classes, users, userGroups } = this.props;
        const { tabValue, rowsPerPage, page, usersState, selectedUser, isUserStatusDialogOpened, isDeleteDialogOpened, isEditUserDialogOpened } = this.state;


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
                            <Tab label=" Global Preferences" />
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
                                        {usersState && usersState.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(user => (
                                            <TableRow key={user.id}>
                                                <TableCell>{user.firstName + " " + user.lastName}</TableCell>
                                                <TableCell>{user.username}</TableCell>
                                                <TableCell>{user.email}</TableCell>
                                                <TableCell>Administrator</TableCell>
                                                <TableCell>{user.userStatus.name}</TableCell>
                                                <TableCell align="right" className={classes.actionCell}>
                                                    <Button variant="contained" color="primary"
                                                            className={classes.tableButton}
                                                            onClick={this.openUserStatusDialog(user.id)}>
                                                        {user.userStatus.name === "Active" ? "Deactivate" : "Activate"}
                                                    </Button>
                                                    <IconButton onClick={this.openEditUserDialog(user.id)} aria-label="Edit">
                                                        <EditIcon />
                                                    </IconButton>
                                                    <IconButton onClick={this.openDeleteDialog(user.id)} aria-label="Delete">
                                                        <DeleteIcon/>
                                                    </IconButton>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                        {emptyRows > 0 && (
                                            <TableRow style={{ height: 48 * emptyRows }}>
                                                <TableCell colSpan={5} />
                                            </TableRow>
                                        )}
                                    </TableBody>
                                    <TableFooter>
                                        <TableRow>
                                            <TablePagination
                                                rowsPerPageOptions={[5, 10, 25]}
                                                colSpan={6}
                                                count={usersState.length}
                                                rowsPerPage={rowsPerPage}
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


                        {tabValue === 1 && <TabContainer>Item Two</TabContainer>}
                        {tabValue === 2 && <TabContainer>Item Three</TabContainer>}
                    </Paper>


                    <Dialog
                        open={isEditUserDialogOpened}
                        onClose={this.closeEditUserDialog}
                        aria-labelledby="form-dialog-title"
                        maxWidth="xs"
                    >
                        <DialogTitle id="form-dialog-title" className={classes.dialogTitle}>Edit user</DialogTitle>
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
                                    onChange={this.editUser}
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
                                    onChange={this.editUser}
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
                                    onChange={this.editUser}
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
                                    onChange={this.editUser}
                                />
                                <FormControl className={classes.formControl}>
                                    <InputLabel htmlFor="userGroup">User group</InputLabel>
                                    <Select
                                        value="Administrators"
                                        autoWidth={true}
                                        onChange={this.editUser}
                                        inputProps={{
                                            name: 'userGroup',
                                            id: 'userGroup',
                                        }}
                                    >
                                        {userGroups.data && userGroups.data.map( (userGroup) => (
                                                <MenuItem key={userGroup.id} value={userGroup.id}>{userGroup.name}</MenuItem>
                                            )
                                        )}
                                    </Select>
                                </FormControl>
                                <div className={classes.dialogButtons}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Save
                                    </Button>
                                    <Button onClick={this.closeEditUserDialog} variant="contained" color="primary">
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
                                Are you sure that you want to {selectedUser.userStatus.name === "Active" ? "deactivate" : "activate"} {selectedUser.username}?
                            </DialogContentText>
                            <div className={classes.dialogButtons}>
                                <Button onClick={this.changeUserStatus} variant="contained" color="primary">
                                    {selectedUser.userStatus.name === "Active" ? "Deactivate" : "Activate"}
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
        userGroups: state.admin.userGroups
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        getUsers: () => dispatch(adminActions.getUsers()),
        getUserGroups: () => dispatch(adminActions.getUserGroups()),
        activateUser: (userId) => dispatch(adminActions.activateUser(userId)),
        deactivateUser: (userId) => dispatch(adminActions.deactivateUser(userId)),
        deleteUser: (userId) => dispatch(adminActions.deleteUser(userId)),
    }
}

export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Administration));
