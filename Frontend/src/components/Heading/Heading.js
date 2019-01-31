import React from 'react';
import { Typography } from "@material-ui/core";

import { withStyles } from '@material-ui/core/styles';

const styles = () => ({
    root: {
        position: 'fixed',
        top: 0,
        zIndex: 1100,
        height: 76,
        display: 'flex',
        alignItems: 'center',
        color: 'white'
    }
});

const Heading = (props) => {


    const { classes, title } = props;

    return(
        <Typography variant="h5" className={classes.root}>
            {title}
        </Typography>
    )

}

export default withStyles(styles)(Heading);
