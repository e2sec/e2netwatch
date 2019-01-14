import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { userActions } from '../../../store/actions/userActions';
import { helpers } from './../../../helpers/helpers'

import M from 'materialize-css/dist/js/materialize';
import logo from '../../../assets/images/logo-white.png';





const Header = (props) => {

    document.addEventListener('DOMContentLoaded', function() {
        const elems = document.querySelectorAll('.dropdown-trigger');
        M.Dropdown.init(elems)
    });


    helpers.isTokenExpired();


    return(
        <header>
            <Link to='/' className="brand-logo"><img src={logo} alt="logo" className="responsive-img"/></Link>

            <div className="user">
                <button className="btn btn-floating dropdown-trigger" data-target="profile-dropdown">uu</button>
                <span>User User</span>
            </div>

            <ul id="profile-dropdown" className="dropdown-content">
                <li><Link to="/profile"><i className="material-icons">account_circle</i>Profile</Link></li>
                <li><Link to="/dashboard" onClick={props.logout}><i className="material-icons">logout</i>Logout</Link></li>
            </ul>
        </header>
    )
}

const mapDispatchToProps = (dispatch) => {
    return{
        logout: () => dispatch(userActions.logout())
    }
}

export default connect(null, mapDispatchToProps)(Header);
