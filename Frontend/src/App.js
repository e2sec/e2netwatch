import React, {Component, Fragment} from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Login from './views/Login/Login';
import Header from './views/Layout/Header/Header';
import PrivateRoute from './components/PrivateRoute/PrivateRoute'
import Sidebar from './views/Layout/Sidebar/Sidebar';
import Dashboard from './views/Dashboard/Dashboard';
import Profile from './views/Profile/Profile';
import Page404 from './views/Page404/Page404';

class App extends Component {
  render() {
    return (
        <BrowserRouter>
          <div className="App container">

            <Switch>

            <Route path='/login' component={Login}/>

            <Fragment>

                <Header/>
                <Sidebar/>

                <div className="content">
                  <Switch>
                      <PrivateRoute exact path='/' component={Dashboard}/>
                      <PrivateRoute path='/profile' component={Profile}/>
                      <PrivateRoute component={Page404}/>
                  </Switch>
                </div>

            </Fragment>


            </Switch>


          </div>
        </BrowserRouter>
    );
  }
}

export default App;
