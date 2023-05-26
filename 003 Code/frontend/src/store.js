import { combineReducers, configureStore, createSlice } from '@reduxjs/toolkit';
import { persistReducer, persistStore } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

const REGISTER = 'REGISTER';

const User = createSlice({
  name: 'User',
  initialState: {
    isLogin: false,
  },
  reducers: {
    R_login(state) {
      state.isLogin = true;
    },
    R_logout(state) {
      state.isLogin = false;
    },
  },
});

const persistConfig = {
  key: 'root',
  storage,
};

const rootReducer = combineReducers({
  User: User.reducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
});

const persistor = persistStore(store);

export { store, persistor, REGISTER };

export const { R_login, R_logout } = User.actions;

