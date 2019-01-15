import React from 'react';
import { NavLink } from 'react-router-dom';

const Sidebar = () => {
    return(
        <div className='sidebar'>
            <ul>
                <li><NavLink exact to='/'><i className="material-icons">dashboard</i>Dashboard</NavLink></li>
                <li><NavLink to='/menu-item-1'><i className="material-icons">menu</i>Menu item 1</NavLink></li>
                <li><NavLink to='/menu-item-2'><i className="material-icons">menu</i>Menu item 2</NavLink></li>
                <li><NavLink to='/menu-item-3'><i className="material-icons">menu</i>Menu item 3</NavLink></li>
                <li><NavLink to='/menu-item-4'><i className="material-icons">menu</i>Menu item 4</NavLink></li>
            </ul>
        </div>
    )
}

export default Sidebar;
