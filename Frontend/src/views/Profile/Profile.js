import React, { Component} from 'react';
import { connect } from 'react-redux';

import M from 'materialize-css/dist/js/materialize';
import {userActions} from "../../store/actions/userActions";

class Profile extends Component {

    state = {
        name : 'Tom'
    };

    tabs = () => {
        const elems = document.querySelectorAll('.tabs');
        M.Tabs.init(elems)
    }

    componentDidMount() {
        this.tabs();
        this.props.userDetails();
    }

    render(){

        return(


            <div className="profile">
                {/*<span> { this.state.name } </span>*/}
                <div className="user-details">
                    <p className="user-avatar">UU</p>
                    <h2 className="user-name">User User</h2>
                    <h4 className="user-privilege">Administrator</h4>
                </div>

                <div>
                    <div>
                        <ul className="tabs">
                            <li className="tab"><a href="#account" className="active">Account</a></li>
                            <li className="tab"><a href="#notifications">Notifications</a></li>
                            <li className="tab"><a href="#test">Basic Title</a></li>
                        </ul>
                    </div>

                    <div id="account">

                        <div className="form-wrapper">
                            <p className="form-title">Account</p>
                            <form>
                                <div className="row">
                                    <div className="input-field col s6">
                                        <input id="first_name" type="text" className="validate"/>
                                        <label htmlFor="first_name">First Name</label>
                                    </div>
                                    <div className="input-field col s6">
                                        <input id="last_name" type="text" className="validate"/>
                                        <label htmlFor="last_name">Last Name</label>
                                    </div>
                                    <div className="input-field col s12">
                                        <input id="email" type="email" className="validate"/>
                                        <label htmlFor="email">Email</label>
                                    </div>
                                    <div className="input-field col s12">
                                        <input id="passwordNew" type="password" className="validate"/>
                                        <label htmlFor="passwordNew">New password</label>
                                    </div>
                                    <div className="input-field col s9">
                                        <input id="password" type="password" className="validate"/>
                                        <label htmlFor="password">Enter Current Password</label>
                                    </div>
                                    <div className="input-field col s3">
                                        <button className="btn btn-primary">Update account</button>
                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>
                    <div id="notifications">Test 2</div>
                    <div id="test">Test 3</div>
                </div>
            </div>
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


export default connect(mapStateToProps, mapDispatchToProps)(Profile);