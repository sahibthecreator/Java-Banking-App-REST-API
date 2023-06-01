<script setup>
import Navigation from '@/components/Navigation.vue';
import RequestWidget from './RequestWidget.vue';
import UserWidget from './UserWidget.vue';
</script>

<template>
  <div class="header">
    <Navigation />
  </div>

  <div class="employeePage">
    <div class="pageGrid">
      <div class="userList d-flex">
        <UserWidget v-for="(user, index) in users" :user="user" />
      </div>

      <div class="rightPanel">
        <div class="actionPanel">
          <div class="actions">
            <b-button variant="dark_primary" @click="">Create transaction</b-button>
            <b-button variant="gray_dark">Create new bank account</b-button>
            <b-button variant="black" @click="">Maybe smth else here</b-button
            >
          </div>
        </div>
        <!-- <h2>Account requests</h2> -->
        <div class="accountRequests">
          <RequestWidget :request="request" />
          <RequestWidget :request="request" />
          <RequestWidget :request="request" />
          <RequestWidget :request="request" />
          <RequestWidget :request="request" />
          <RequestWidget :request="request" />
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
      user: {
        firstName: 'Yvan',
        lastName: 'Roes',
      },

      request: {
        user: this.user,
        //smth else idk??
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

    .rightPanel {
      height: 80vh;
      display: flex;
      flex-direction: column;
      gap: 100px;
      .actionPanel {
        background: var(--white);
        width: 100%;
        padding: 40px;
        height: 30%;
        box-shadow: 0 0 30px #14141417;
        border-radius: 10px;
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

      .accountRequests {
        background: var(--white);
        width: 100%;
        height: auto;
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
