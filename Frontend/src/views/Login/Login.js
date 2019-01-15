import React, { Component} from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import { userActions } from '../../store/actions/userActions';

import logo from '../../assets/images/logo.png';

class Login extends Component {

    state = {
        username: '',
        password: ''
    };


    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.login(this.state);
    }

    render(){

        const user = localStorage.getItem('user');

        const element = user ? (
            <Redirect to="/"/>
        ) : (
            <div className="login-page">
                <div className="login-header">
                    <img src={logo} alt="logo" className="responsive-img"/>
                </div>
                <form onSubmit={this.handleSubmit} className="login">
                    <div className="row">
                        <div className="input-field col s12">
                            <input id="username" type="text" className="validate" required onChange={this.handleChange} />
                            <label htmlFor="username">Username</label>
                        </div>
                        <div className="input-field col s12">
                            <input id="password" type="password" className="validate" required onChange={this.handleChange} />
                            <label htmlFor="password">Password</label>
                        </div>
                        <div className="input-field col s12">
                            <button className="btn btn-primary">Sign In</button>
                        </div>
                    </div>
                </form>
            </div>
        );

        return element
    }
}

const mapDispatchToProps = (dispatch) => {
    return{
        login: (user) => dispatch(userActions.login(user))
    }
}

export default connect(null, mapDispatchToProps)(Login);
