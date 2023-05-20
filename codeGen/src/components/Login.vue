<script setup>
import Navigation from '@/components/Navigation.vue'
</script>

<template>
<div id="header">
    <navigation />
  </div>

  <div class="wrapper">
    <div class="section" id="left"></div>
    <div class="section right" id="right">
      <div class="content mx-auto">
        <div class="login">
          <div class="logoWrapper">
            <img src="@/assets/Logo.svg" alt="logo" id="logo" />
            <span id="logoTitle">WAVR</span>
          </div>
          <div class="selectionType py-4">
            <button :class="{ 'selected': isLogin }" @click="isLogin = true">Login</button>
            <button :class="{ 'selected': !isLogin }" @click="isLogin = false">Register</button>
          </div>
          <div class="loginWrapper" v-if="isLogin">
            <input type="text" v-model="username" placeholder="Email" class="text-dark" />
            <input type="password" v-model="password" placeholder="Password" class="text-dark" />
            <button type="button" @click="($event) => login()">Log in</button>
          </div>
          <div class="loginWrapper" v-else>
            <input type="text" v-model="firstName" placeholder="First Name" class="text-dark" />
            <input type="text" v-model="lastName" placeholder="Last Name" class="text-dark" />
            <input type="text" v-model="username" placeholder="Email" class="text-dark" />
            <input type="password" v-model="password" placeholder="Password" class="text-dark" />
            <input type="text" v-model="bsn" placeholder="BSN" class="text-dark" />
            <input type="text" v-model="bsn" placeholder="Date of Birth" class="text-dark" />
            
            <button type="button" @click="($event) => login()">Register</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '../stores/user';

export default {
  setup() {
    return {
      store: useUserStore(),
    };
  },

  name: 'login',
  data() {
    return {
      isLogin: true,
      username: '',
      password: '',
    };
  },
  methods: {
    login() {
      this.store
        .login(this.username, this.password)
        .then(() => {
          console.log('logged in ' + this.username);
        })
        .catch((error) => {
          this.errorMessage = error;
        });
    },
  },
};
</script>

<style>
.wrapper {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.content {
  display: flex;
  flex-direction: column;
  gap: 40px;
  margin-top: 20%;
}

#left {
  background-image: url('https://images.unsplash.com/photo-1572798793517-03198986ca9e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80');
  background-repeat: no-repeat;
  /* background-attachment: fixed; */
  background-position: center;
  background-size: cover;
  grid-column: 1;
}

#right {
  grid-column: 2;
  display: flex;
}

.logoWrapper {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

#logo {
  width: 40px;
}

#logoTitle {
  font-size: 24px;
}

.loginWrapper {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.loginWrapper>input {
  background-color: var(--white);
  height: 40px;
  padding: 10px;
  border: none;
  border-radius: 7px;
  border: 1px var(--gray-light) solid;
  color: var(--white);
}

::placeholder {
  color: var(--gray-dark);
}

.loginWrapper>button {
  height: 50px;
  border: none;
  border-radius: 7px;
  background-color: var(--gray-black);
  color: var(--gray-light);
}

.loginWrapper>button:hover {
  filter: opacity(0.9);
}

.selectionType {
  width: 100%;
}

.selectionType>button {
  width: 50%;
  padding: 10px;
  border: none;
  background: transparent;
  color: var(--gray-black);
  border-bottom: 1px var(--white) solid;
}

.selectionType>.selected {
  border-bottom: 2px var(--gray-dark) solid;
}
</style>
