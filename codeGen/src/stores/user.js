import { defineStore } from 'pinia';
import axios from '../axios-auth';


export const useUserStore = defineStore('counter', {
  state: () => ({
      jwt: '',
      username: '',
  }),
  getters: {
    isAuthenticated: (state) => {
      state.jwt !== ''
      }
  },
  actions: {
    autoLogin() {
      if (localStorage["jwt"]) {
        this.jwt = localStorage["jwt"];
        axios.defaults.headers.common['Authorisation'] = "Bearer " + res.data.jwt;
      }
    },
    login(username, password) {
      return new Promise((resolve, reject) => {
        axios.post('/auth/login', {
          username: username,
          password: password
        }).then(res => {
          localStorage["jwt"] = res.data.jwt;
          this.jwt = res.data.jwt;
          this.username = res.data.username;
          this.expireAt = res.data.expireAt;

          axios.defaults.headers.common['Authorisation'] = "Bearer " + res.data.jwt;
          console.log('logged in as ' + this.username);

          resolve();
        }).catch(error => {
          console.log(error);
          reject();
        })
      })
    }
  }
  }
)