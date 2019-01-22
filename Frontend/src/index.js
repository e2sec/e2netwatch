import React from 'react';
import ReactDOM from 'react-dom';

import './index.css';

import * as serviceWorker from './serviceWorker';

import { createStore, applyMiddleware } from "redux";
import { Provider } from 'react-redux';
import thunk from 'redux-thunk'
import rootReducer from './store/reducers/rootReducer';

import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

import App from './App';


const store = createStore(rootReducer, applyMiddleware(thunk));

const theme = createMuiTheme({
    typography: {
        useNextVariants: true,
    },
    overrides: {
        MuiFormControl: {
           marginDense: {
               marginTop: 10,
               marginBottom: 8,
           }
        },
        MuiFormLabel: {
            root: {
                fontSize: '1.1rem'
            }
        },
        MuiInputLabel: {
            shrink: {
                transform: 'translate(0, -5px) scale(0.9)',
            },
        },
        MuiSelect: {
            select: {
                '&:focus': {
                    background: 'none'
                }
            }
        }
    }
});

console.log(theme)

ReactDOM.render(
    <Provider store={store}>
        <MuiThemeProvider theme={theme}>
            <App />
        </MuiThemeProvider>
    </Provider>,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
