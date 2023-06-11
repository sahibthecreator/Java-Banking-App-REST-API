<script setup>
import Navigation from '@/components/Navigation.vue';
import RequestWidget from './RequestWidget.vue';
import UserWidget from './UserWidget.vue';
import AccountWidgetExpandable from './AccountWidgetExpandable.vue';

</script>

<template>
  <div class="header">
    <Navigation />
  </div>

  <div class="employeePanel">
    <div class="pageGrid">
      <div class="userList d-flex" :class="{ 'expanded': userPanelExpanded }" v-if="currentTab == 0">
        <div class="expandBtn" @click="userPanelExpanded = !userPanelExpanded">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
            class="bi bi-arrows-angle-expand" viewBox="0 0 16 16">
            <path fill-rule="evenodd"
              d="M5.828 10.172a.5.5 0 0 0-.707 0l-4.096 4.096V11.5a.5.5 0 0 0-1 0v3.975a.5.5 0 0 0 .5.5H4.5a.5.5 0 0 0 0-1H1.732l4.096-4.096a.5.5 0 0 0 0-.707zm4.344-4.344a.5.5 0 0 0 .707 0l4.096-4.096V4.5a.5.5 0 1 0 1 0V.525a.5.5 0 0 0-.5-.5H11.5a.5.5 0 0 0 0 1h2.768l-4.096 4.096a.5.5 0 0 0 0 .707z" />
          </svg>
        </div>

        <div class="roleFilterBtns">
          <div class="options">
            <option v-bind:id="currentTab == 0 ? 'selected' : ''" @click="currentTab = 0">Users</option>
            <option v-bind:id="currentTab == 1 ? 'selected' : ''" @click="currentTab = 1">Accounts</option>
          </div>
          <div class="userFilterBtns">
            <div>
              <b-button variant="dark_primary" :class="{ 'selected': roleFilter == 'USER' }"
                v-on:click="setRoleFilter('USER')">User</b-button>

              <b-button variant="dark_primary" :class="{ 'selected': roleFilter == 'CUSTOMER' }"
                v-on:click="setRoleFilter('CUSTOMER')">Customer</b-button>
              <b-button variant="dark_primary" :class="{ 'selected': roleFilter == 'EMPLOYEE' }"
                v-on:click="setRoleFilter('EMPLOYEE')">Employee</b-button>
            </div>
            <b-input v-if="userPanelExpanded" placeholder="enter username" v-model="search" @input="searchUsers" />
          </div>
        </div>

        <UserWidget v-for="(user, index) in filteredUsers" :user="user" />
      </div>

      <!----------------- Accounts Panel ------------------->
      <div class="userList accountsList d-flex" :class="{ 'expanded': userPanelExpanded }" v-if="currentTab == 1">
        <div class="expandBtn" @click="userPanelExpanded = !userPanelExpanded">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
            class="bi bi-arrows-angle-expand" viewBox="0 0 16 16">
            <path fill-rule="evenodd"
              d="M5.828 10.172a.5.5 0 0 0-.707 0l-4.096 4.096V11.5a.5.5 0 0 0-1 0v3.975a.5.5 0 0 0 .5.5H4.5a.5.5 0 0 0 0-1H1.732l4.096-4.096a.5.5 0 0 0 0-.707zm4.344-4.344a.5.5 0 0 0 .707 0l4.096-4.096V4.5a.5.5 0 1 0 1 0V.525a.5.5 0 0 0-.5-.5H11.5a.5.5 0 0 0 0 1h2.768l-4.096 4.096a.5.5 0 0 0 0 .707z" />
          </svg>
        </div>

        <div class="roleFilterBtns">
          <div class="options">
            <option v-bind:id="currentTab == 0 ? 'selected' : ''" @click="currentTab = 0">Users</option>
            <option v-bind:id="currentTab == 1 ? 'selected' : ''" @click="currentTab = 1">Accounts
            </option>
          </div>
          <b-input v-if="userPanelExpanded" placeholder="enter IBAN" v-model="searchAccount" @input="searchAccounts" />
        </div>
        <AccountWidgetExpandable v-for="(account, index) in filteredAccounts" :account="account"
          :user="mapAccountToUser(account)" />
      </div>

      <div class="rightPanel">
        <div class="actionPanel">
          <div class="actions">
            <b-button variant="dark_primary" @click="this.$router.push('/dashboard/employeePanel/transaction')">Create
              transaction</b-button>
            <b-button variant="gray_dark" @click="this.$router.push('/dashboard/employeePanel/createAccount')">Create new
              bank account</b-button>
            <b-button variant="black" @click="">Maybe smth else here</b-button>
          </div>
        </div>
        <!-- <h2>Account requests</h2> -->
        <div class='accountRequestsWrapper'>
          <h1>Account Requests</h1>
          <div class="accountRequests">
            <RequestWidget v-for="(request, index) in requests" :request="request" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EmployeePanel',
  data() {
    return {
      users: null,
      accounts: null,
      filteredUsers: null,
      filteredAccounts: null,
      requests: null,
      roleFilter: null,
      userPanelExpanded: false,
      accountsPanelEnabled: false,
      search: "",
      searchAccount: "",
      currentTab: 0
    };
  },
  mounted() {
    this.checkUser();
    this.getAllUsers();
    this.initAllAccounts();
    this.initAllRequests();
  },

  methods: {
    async checkUser() {
      try {
        let user = await this.$store.dispatch(
          'getUser',
          this.$store.getters.getUserId
        );
        if (user.role != "EMPLOYEE") {
          this.$router.push('404');
        }
      } catch (err) {
        console.log('gtfo')
      }
    },

    async getAllUsers() {
      try {
        let userList = await this.$store.dispatch('getUsers');
        this.users = userList;
        this.filteredUsers = userList;
      } catch (error) {
        console.log(error);
        if (this.users == null) {
          this.$store.dispatch('logout');
          this.$router.push('/login')
        }
      }
    },
    async initAllAccounts() {
      try {
        let accounts = await this.$store.dispatch('getAccounts');
        this.accounts = accounts;
        this.filteredAccounts = accounts;
      } catch (error) {
        console.log(error);
        if (this.users == null) {
          this.$store.dispatch('logout');
          this.$router.push('/login')
        }
      }
    },
    async initAllRequests() {
      try {
        let requests = await this.$store.dispatch('getAllRequests');
        this.requests = requests;
      } catch (error) {
        console.log(error);
      }
    },
    setRoleFilter(role) {
      this.roleFilter = role;
      this.filteredUsers = this.users.filter(u => u.role === role);
      console.log(this.users);
    },
    searchUsers() {
      this.roleFilter = null;
      const searchResults = this.users.filter(user => {
        const firstNameMatch = user.firstName.toLowerCase().includes(this.search.toLowerCase());
        const lastNameMatch = user.lastName.toLowerCase().includes(this.search.toLowerCase());
        return firstNameMatch || lastNameMatch;
      });

      this.filteredUsers = searchResults;
    },
    mapAccountToUser(account) {
      const user = this.users.find(u => u.id === account.userId);
      return user;
    },
    searchAccounts() {
      const searchResults = this.accounts.filter(a => a.iban.toLowerCase().includes(this.searchAccount.toLowerCase()));
      this.filteredAccounts = searchResults;
    },
  },

};
</script>

<style lang="scss" scoped>
.employeePanel {
  width: 90vw;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;
  flex-direction: row;
  align-items: center;
  justify-content: center;

  .pageGrid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: auto;
    grid-gap: 100px;

    .userList {
      width: 40vw;
      height: 80vh;
      margin-top: auto;
      margin-bottom: auto;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 20px;
      overflow: auto;
      white-space: nowrap;
      padding: 10px 35px;
      background: var(--white);
      box-shadow: 0 0 30px #1414140a;
      border-radius: 10px;
      border: 1px solid rgba(0, 0, 0, 0.125);
      transition: all 0.5s;

      &.expanded {
        position: absolute;
        z-index: 10;
        width: 100%;

        .roleFilterBtns {
          width: 60%;
        }
      }

      .expandBtn {
        position: absolute;
        right: 1rem;
        top: 1rem;
        cursor: pointer;

        &:hover {
          opacity: 0.6;
        }
      }
    }

    .options {
      display: flex;
      flex-direction: row;
      padding: 5px;
      gap: 10px;
      background: #ebebeb;
      width: fit-content;
      border-radius: 7px;
      margin-bottom: 1rem;

      option {
        padding: 5px 15px;
        border-radius: 4px;
        color: var(--gray-light);
        cursor: pointer;
      }

      #selected {
        color: var(--gray-dark);
        background: var(--white);
        box-shadow: 0 0 30px #14141417;
        font-weight: 500;
      }
    }

    .rightPanel {
      border-radius: 10px;
      height: 80vh;
      display: flex;
      flex-direction: column;
      gap: 100px;

      .actionPanel {
        border-radius: 10px;
        border: 1px solid rgba(0, 0, 0, 0.125);
        background: var(--white);
        width: 100%;
        padding: 40px;
        height: 30%;
        box-shadow: 0 0 30px #14141417;
        display: flex;
        flex-direction: row;
        gap: 20px;
        align-items: center;
        justify-content: center;

        .actions {
          display: flex;
          flex: row;
          gap: 20px;

          button {
            height: fit-content;
            padding-block: 5px;
            width: 130px;
            border-radius: 7px;
            background-color: var(--gray-black);
            border: none;
            color: white;
            cursor: pointer;
            box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.103);
            position: relative;
            overflow: hidden;
            transition-duration: 0.3s;

            &:active {
              transform: translate(5px, 5px);
              transition-duration: 0.3s;
            }
          }
        }
      }

      .accountRequestsWrapper {
        height: 43%;
      }

      .accountRequests {
        border-radius: 10px;
        border: 1px solid rgba(0, 0, 0, 0.125);
        background: var(--white);
        width: 100%;
        height: 100%;
        padding: 40px;
        box-shadow: 0 0 30px #14141417;
        border-radius: 10px;
        overflow: auto;
        white-space: nowrap;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 20px;
      }
    }
  }
}

.roleFilterBtns {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: auto;
  gap: 5%;

  .userFilterBtns {
    width: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    gap: 0.7rem;

    div {
      width: fit-content;
      display: flex;
      gap: 5%;
    }
  }

  .btn {
    &:focus {
      box-shadow: none;
    }

    &.selected {
      opacity: 0.8;
      pointer-events: none;
    }
  }
}

*::-webkit-scrollbar {
  width: 25px;
}

*::-webkit-scrollbar-track {
  border-radius: 30px;
  margin: 35px;
  background: transparent;
  /* change me to blue to match the background */
}

*::-webkit-scrollbar-thumb {
  border-radius: 35px;
  background: var(--gray-dark);
  border: 5px var(--white) solid;
  /* change border color to blue to match the background */
}

*::-webkit-scrollbar-thumb:hover {
  background: var(--gray-black);
}
</style>
