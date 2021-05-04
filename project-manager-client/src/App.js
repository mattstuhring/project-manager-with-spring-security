import "./App.css";
import Dashboard from "./component/Dashboard";
import Header from "./component/Layout/Header";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import AddProject from "./component/Project/AddProject";
import { Provider } from "react-redux";
import store from "./store";
import UpdateProject from "./component/Project/UpdateProject";
import ProjectBoard from "./component/ProjectBoard/ProjectBoard";
import AddProjectTask from "./component/ProjectBoard/ProjectTask/AddProjectTask";
import UpdateProjectTask from "./component/ProjectBoard/ProjectTask/UpdateProjectTask";
import Landing from "./component/Layout/Landing";
import Register from "./component/User/Register";
import Login from "./component/User/Login";
import jwt_decode from "jwt-decode";
import setToken from "./userUtil/setToken";
import { SET_CURRENT_USER } from "./action/types";
import { logout } from "./action/userActions";
import SecureRoute from "./userUtil/SecureRoute";

const jwtToken = localStorage.jwtToken;

if (jwtToken) {
  setToken(jwtToken);
  const decoded = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decoded
  });

  const currentTime = Date.now() / 1000;
  if (decoded.exp < currentTime) {
    store.dispatch(logout());
    window.location.href = "/";
  }
}

function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          {/* Public Routes */}
          <Header />
          <Route exact path="/" component={Landing} />
          <Route exact path="/register" component={Register} />
          <Route exact path="/login" component={Login} />

          {/* Private Routes */}
          <Switch>
            <SecureRoute exact path="/dashboard" component={Dashboard} />
            <SecureRoute exact path="/addProject" component={AddProject} />
            <SecureRoute
              exact
              path="/updateProject/:id"
              component={UpdateProject}
            />
            <SecureRoute
              exact
              path="/projectBoard/:id"
              component={ProjectBoard}
            />
            <SecureRoute
              exact
              path="/addProjectTask/:id"
              component={AddProjectTask}
            />
            <SecureRoute
              exact
              path="/updateProjectTask/:backlog_id/:pt_sequence"
              component={UpdateProjectTask}
            />
          </Switch>
        </div>
      </Router>
    </Provider>
  );
}

export default App;
