<script setup>
import Navigation from '@/components/Navigation.vue';
import UserWidget from './UserWidget.vue';
</script>

<template>
  <div class="header">
    <navigation />
  </div>

  <div class="employeePage">
    <div class="pageGrid">
      <div class="userList d-flex">
        <UserWidget v-for="(user, index) in users" :user="user"  />
      </div>

      <div class="employeeTransaction">
        <h2>Create Transaction</h2>
        <div class="transaction"></div>
        <h2>Account requests</h2>
        <div class="accountRequests"></div>
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
      user: {
        firstName: 'Yvan',
        lastName: 'Roes',
      },
    };
  },
  mounted() {
    this.getAllUsers();
  },

  methods: {
    async getAllUsers() {
      try {
        let userList = await this.$store.dispatch('getUsers');
        this.users = userList;
      } catch (error) {
        console.log(error);
        if (this.users == null) {
          console.log('Connection refused');
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.employeePage {
  background: url('@/assets/dashboard/bg.jpg');
  background-repeat: no-repeat;
  background-clip: border-box;
  background-position-x: center;
  background-size: cover;
  height: 100vh;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;

  .pageGrid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: auto;
    grid-gap: 100px;

    .userList {
      background: var(--white);
      width: 40vw;
      padding: 20px;
      height: 80vh;
      margin-top: auto;
      margin-bottom: auto;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 20px;
      overflow: auto;
      white-space: nowrap;
      box-shadow: 0 0 30px #14141417;
      border-radius: 10px;
    }

    .employeeTransaction {
      background: var(--white);
      width: 40vw;
      padding: 40px;
      height: 80vh;
      margin-top: auto;
      margin-bottom: auto;
      display: flex;
      flex-direction: column;
      gap: 20px;
      overflow: auto;
      white-space: nowrap;
      box-shadow: 0 0 30px #14141417;
      border-radius: 10px;

      .transaction {
        background: var(--white);
        width: 100%;
        padding: 40px;
        height: 30%;
        box-shadow: 0 0 30px #14141417;
        border-radius: 10px;
      }

      .accountRequests {
        background: var(--white);
        width: 100%;
        height: 50%;
        padding: 40px;
        box-shadow: 0 0 30px #14141417;
        border-radius: 10px;
      }
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
