<script setup>
import AccountIcon from '@/components/Dashboard/AccountIcon.vue';
</script>

<template>
  <div class="userWidget">
    <div class="d-flex userCard">
      <AccountIcon :accountName="user.firstName" class="icon"></AccountIcon>
      <div class="userInfo">
        <p class="my-auto ml-3">{{ user.firstName }} {{ user.lastName }}</p>
        <div class="infoList ml-3">
          <span>email: {{ user.email }} | </span>
          <span>bsn: {{ user.bsn }} | </span>
          <span>{{ user.active ? "Active" : "Not Active" }} | </span>
          <span>accounts: {{ accounts?.length || 0}}  </span>
        </div>
      </div>

      <div class="userControls">
        <button class="button btn-warning" @click="updateUser()">Edit</button>
        <button class="button btn-danger" @click="deleteUser()">Delete</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserWidget',
  props: {
    user: Object,
  },
  data() {
    return {
      accounts: null,
    }
  },
  mounted() {
    this.getAllAccounts();
  },
  methods: {
    updateUser() {
      console.log('update dialog here');
    },
    deleteUser() {
      if (confirm('Are you sure?')) {
        console.log("Delete user");
      } else {
        console.log("User not deleted");
      }
    },
    async getAllAccounts() {
      try {
        let accounts = await this.$store.dispatch('getAccountsByUserId', this.user.id);
        this.accounts = accounts;
        console.log(accounts);
      } catch (error) {
        console.log(error);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.userWidget {
  height: fit-content;
  width: 95%;
  background-color: var(--white);
  padding: 15px 15px;
  border-radius: 15px;
  display: flex;
  flex-direction: row;
  align-items: center;
  box-shadow: 0 0 30px #14141417;

  .icon {
    aspect-ratio: 1;
  }

  .userCard {
    display: flex;
    flex-direction: row;
    width: 100%;

    .userInfo {
      .infoList {
        color: var(--gray-light);
        display: flex;
        flex-wrap: wrap;
        width: 80%;
        gap: 5px;

        // span {
        //   margin-block: 10px;
        //   padding-inline: 10px;
        //   border-radius: 35px;
        //   background: var(--gray-black);
        // }
      }
    }

    .userControls {
      width: fit-content;
      display: flex;
      flex-direction: column;
      align-items: end;
      gap: 10px;

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
}
</style>
