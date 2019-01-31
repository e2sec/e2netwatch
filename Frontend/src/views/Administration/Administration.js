import React, { Component, Fragment } from 'react';

import { withStyles } from '@material-ui/core/styles';
import Heading from '../../components/Heading/Heading';
import TablePaginationActions from '../../components/TablePaginationActions/TablePaginationActions'
import { Paper, Tab, Tabs, Table, TableHead, TableBody, TableRow, TableCell, TableFooter, TablePagination } from '@material-ui/core';
import TabContainer from '../../components/TabContainer/TabContainer';
import {userActions} from "../../store/actions/userActions";
import connect from "react-redux/es/connect/connect";


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
    },
    tableWrapper: {
        overflowX: 'auto',
        padding: theme.spacing.unit * 3,
        paddingBottom: 0
    },
});

class Administration extends Component {

    state = {
        tabValue: 0,
        users: [],
        page: 0,
        rowsPerPage: 5,
    };


    componentDidMount() {
        this.props.getUsers()
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if (this.props.users !== prevProps.users) {
          this.setState({ users: this.props.users});
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

    render() {

        const { classes, users } = this.props;
        const { tabValue, rowsPerPage, page } = this.state;


        if (!this.props.users) return null;

        else {

            const emptyRows = rowsPerPage - Math.min(rowsPerPage, users.length - page * rowsPerPage);

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
                                            <TableCell>Action</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {users.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(user => (
                                            <TableRow key={user.id}>
                                                <TableCell component="th" scope="row">{user.firstName + " " + user.lastName}</TableCell>
                                                <TableCell>{user.username}</TableCell>
                                                <TableCell>{user.email}</TableCell>
                                                <TableCell>{user.userGroups[0].name}</TableCell>
                                                <TableCell>{user.userStatus.name}</TableCell>
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
                                                count={users.length}
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

                </Fragment>
            )
        }
    }
}

const mapStateToProps = (state) => {
    return {
        users: state.user.users.content
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        getUsers: () => dispatch(userActions.getUsers()),
    }
}

export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Administration));
