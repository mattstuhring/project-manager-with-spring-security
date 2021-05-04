import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import setToken from "../userUtil/setToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async dispatch => {
  try {
    await axios.post("/api/v1/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const login = loginRequest => async dispatch => {
  try {
    // POST -> Login request
    const res = await axios.post("/api/v1/users/login", loginRequest);

    // Extract token from res.data
    const { token } = res.data;

    // Store token in Local Storage
    localStorage.setItem("jwtToken", token);

    // Set token in headers
    setToken(token);

    // Decode token for React
    const decoded = jwt_decode(token);

    // Dispatch to userReducer
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const logout = () => dispatch => {
  localStorage.removeItem("jwtToken");
  setToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {}
  });
};
