import React, { Component} from 'react';
import { connect } from 'react-redux';

import {userActions} from "../../store/actions/userActions";

import { withStyles } from '@material-ui/core/styles';
import { Paper, Typography } from '@material-ui/core'

const styles = theme => ({

})

class Profile extends Component {

    state = {
        name : 'Tom'
    };

    componentDidMount() {
        this.props.userDetails();
    }

    render(){

        const { classes } = this.props;

        return(

            <Paper className={classes.root} elevation={1}>
                <Typography variant="h5" component="h3">
                    This is a sheet of paper.
                </Typography>
                <Typography component="p">
                    Paper can be used to build surface or other elements for your application.
                </Typography>
            </Paper>

        )
    }

}

const mapStateToProps = (state) => {
     console.log(state)
     return {

     }
}

const mapDispatchToProps = (dispatch) => {
    return {
        userDetails: () => dispatch(userActions.userDetails())
    }
}


export default withStyles(styles)(connect(mapStateToProps, mapDispatchToProps)(Profile));