<script setup>
import AccountIcon from '@/components/Dashboard/AccountIcon.vue';
</script>

<template>
  <div id="nav">
    <b-navbar-brand to="/">
      <img src="@/assets/Logo.svg" alt="logo" id="logo" />
      <span id="logoTitle">WAVR</span>
    </b-navbar-brand>
    <b-nav id="list">
      <b-nav-item to="/">Home</b-nav-item>
      <b-nav-item v-if="token" to="/dashboard">Dashboard</b-nav-item>
    </b-nav>

    <b-navbar-nav class="ml-auto">
      <b-nav-item to="/login" id="login" v-if="!token">Login / register</b-nav-item>
      <b-nav-item v-else>
        <b-dropdown id="dropdown-offset" offset="-w" size="lg" variant="link" toggle-class="text-decoration-none"
          no-caret>
          <template #button-content>
            <AccountIcon accountName="Root"></AccountIcon>
          </template>
          <b-dropdown-item to="/dashboard/profile">Profile</b-dropdown-item>
          <b-dropdown-item to="/dashboard">Dashboard</b-dropdown-item>
          <b-dropdown-item to="/dashboard/employeePanel">DB management</b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-item>
            <b-button variant="danger" class="w-100 p-1" @click="logout">Logout</b-button></b-dropdown-item>
        </b-dropdown>
      </b-nav-item>
    </b-navbar-nav>
  </div>
  <div id="line"></div>
</template>

<script>
export default {
  name: 'Navigation',
  data() {
    return {
      token: this.$store.getters.getCurrentUserToken,
      user: null,
    };
  },
  methods: {
    logout() {
      this.$store.dispatch('logout');
    },
    mounted() {
      this.getUser();
    },
    methods: {
      async getUser() {
        try {
          let user = await this.$store.dispatch('getUser', this.$store.getters.getUserId);
          this.user = user;
        } catch (error) {
          console.log(error);
          if (this.user == null) {
            this.$store.dispatch('logout');
          }
        }
      },
    },
  },
};
</script>

<style>
#line {
  width: 2000px;
  height: 1px;
  background-color: var(--gray-black);
}

#nav {
  height: 100px;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 150px;
}

#list {
  list-style: none;
  gap: 20px;
  left: 10px;
}

.nav-link {
  font: 500;
  color: var(--gray-dark);
}

.nav-link:hover {
  color: var(--black);
}

#login {
  border: 1px var(--gray-black) solid;
  border-radius: 7px;
  padding: 0.1rem 2rem;
  color: var(--gray-black);
}

#logoTitle {
  color: var(--gray-black);
}

#logo {
  width: 40px;
  aspect-ratio: 1 / 2;
  margin-right: 20px;
}
</style>
