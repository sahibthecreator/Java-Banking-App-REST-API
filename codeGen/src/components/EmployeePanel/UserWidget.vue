<script setup>
import AccountIcon from '@/components/Dashboard/AccountIcon.vue';
</script>

<template>
  <div class="userWidget" v-if="!expanded" @click="expanded = true">
    <div class="d-flex userCard">
      <AccountIcon :accountName="user.firstName" class="icon"></AccountIcon>
      <div class="userInfo">
        <p class="my-auto ml-3">{{ user.firstName }} {{ user.lastName }}</p>
        <div class="infoList ml-3">
          <span>email: {{ user.email }} | </span>
          <span>bsn: {{ user.bsn }} | </span>
          <span>{{ user.active ? 'Active' : 'Not Active' }} | </span>
          <span>accounts: {{ accounts }} </span>
        </div>
      </div>

     
    </div>
  </div>
  <div class="expanded userWidget" v-else>
    <button type="button" class="dismiss" @click="expanded = false">×</button>
    <div class="d-flex userCard">
      <AccountIcon :accountName="user.firstName" class="icon"></AccountIcon>
      <div class="userInfo">
        <div class="detailCard">
          <label>Id</label>
          <span type="text"> {{ user.id }}</span>
        </div>
        <div class="detailCards">
          <div class="detailCard">
            <label>First Name</label>
            <input type="text" v-model="user.firstName">
          </div>
          <div class="detailCard">
            <label>Last Name</label>
            <input type="text" v-model="user.lastName">
          </div>
        </div>
        <div class="detailCard">
          <label>Email</label>
          <input type="text" v-model="user.email">
        </div>
        <div class="detailCard">
          <label>BSN</label>
          <input type="text" v-model="user.bsn">
        </div>
        <div class="detailCard">
          <label>Active</label>
          <span type="text">{{ user.active ? 'Yes' : 'No' }} </span>
        </div>
        <div class="detailCard">
          <label>Accounts</label>
          <span type="text">{{ accounts }}</span>
        </div>
        <div class="detailCard">
          <label>Total Balance</label>
          <span type="text">€ {{ totalBalance }}</span>
        </div>
      </div>

      <div class="userControls">
        <button class="button btn-warning" @click="updateUser()">Edit</button>
        <button class="button btn-danger" @click="deleteUser()">{{ accounts == 0 ? 'Delete' : 'Deactivate' }}</button>
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
      totalBalance: 0,
      expanded: true,
    };
  },
  mounted() {
    this.getAllAccounts();
    console.log(this.user);
  },
  watch: {
    user: {
      immediate: true,
      handler() {
        this.getAllAccounts();
      },
    },
  },
  methods: {
    updateUser() {
      console.log('update dialog here');
    },
    async deleteUser() {
      if (confirm('Are you sure?')) {
        try {
          await this.$store.dispatch('deleteUser', this.user.id);
          alert('User was deletet/deactivated');
        } catch (error) {
          alert(error.message);
        }
      }
    },
    async getAllAccounts() {
      try {
        let accounts = await this.$store.dispatch(
          'getAccountsByUserId',
          this.user.id
        );
        this.accounts = accounts.length;

        let value = accounts?.reduce((sum, account) => sum + account.balance, 0)
        let val = (value / 1).toFixed(2).replace('.', ',');
        this.totalBalance = val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
      } catch (error) {
        this.accounts = 0;
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
  cursor: pointer;
  &:hover{
    filter: contrast(1.1);
  }

  .dismiss {
    position: absolute;
    right: 3%;
    top: 1%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0.5rem 1rem;
    background-color: transparent;
    color: black;
    border: 0px;
    font-size: 1.2rem;
    font-weight: 300;
    width: 40px;
    height: 40px;
    border-radius: 10px;
    transition: .3s ease;
    z-index: 100;
  }

  .dismiss:hover {
    background-color: #e03131;
    color: #fff;
  }

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

  &.expanded {
    height: fit-content;

    .userCard {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 5%;


      .userInfo {
        .detailCards {
          display: flex;
          justify-content: space-between;
          width: 100%;

          .detailCard {
            width: 48%;
          }
        }

        .detailCard {
          display: flex;
          flex-direction: column;
          width: 100%;

          input,
          span {
            border: none;
            color: var(--gray-light);
            background: var(--white);
            box-shadow: 0 0 30px #1414140a;
            border-radius: 7px;
            border: 1px solid rgba(0, 0, 0, 0.125);
            padding: 4px;

            &:focus {
              outline: none;
            }
          }

          span {
            cursor: not-allowed;
          }

          label {
            font-size: small;
            margin-bottom: 1%;
            margin-top: 2%;
            color: var(--gray-light);
          }
        }

        .infoList {
          width: 100%;
          flex-direction: column;
          color: var(--gray-light);
          display: flex;
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
        margin: 10% 0 5% 0;
        width: fit-content;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 85%;

        button {
          height: fit-content;
          padding-block: 5px;
          width: 47%;
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
}
</style>
