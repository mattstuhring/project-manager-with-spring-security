import { SET_CURRENT_USER } from "../action/types";

const initialState = {
  user: {},
  validToken: false
};

const isActionPayload = payload => {
  if (payload) {
    return true;
  } else {
    return false;
  }
};

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_CURRENT_USER:
      return {
        ...state,
        validToken: isActionPayload(action.payload),
        user: action.payload
      };

    default:
      return state;
  }
};

export default userReducer;
