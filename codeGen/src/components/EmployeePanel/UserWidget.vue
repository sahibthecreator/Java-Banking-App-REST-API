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
            <input type="text" v-model="user.firstName" @input="editBtnContent = 'Save'">
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
        <button class="button btn-warning" @click="updateUser()">{{ editBtnContent }}</button>
        <button class="button btn-danger" @click="deleteUser()">{{ accounts == 0 ? 'Delete' : 'Deactivate' }}</button>
      </div>
    </div>
  </div>

  <div class="card" v-if="statusPopupCardEnabled">
    <div class="header">
      <!-- Loading -->
      <div class="spinnerContainer" v-if="loading">
        <div class="spinner"></div>
        <div class="loader">
          <div class="words">
            <span class="word">Updating user</span>
          </div>
        </div>
      </div>
      <!-- /Loading -->

      <div class="image" v-if="!loading">
        <img src="@/assets/SuccessIcon.png" class="success icon" />
      </div>
      <div class="content" v-if="!loading">
        <span class="title">{{ statusTitle }}</span>
        <span v-if="ownEmailChanged()" class="title">{{ statusSubTitle }}</span>

      </div>
      <div class="actions">
        <button type="button" class="history" @click="statusPopupCardEnabled = false"
          v-if="!loading && !ownEmailChanged()">Close</button>
        <button type="button" class="history" @click="goToLogin" v-if="!loading && ownEmailChanged()">Login</button>
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
      expanded: false,
      statusPopupCardEnabled: false,
      statusTitle: "",
      statusSubTitle: "",
      errorMsg: "",
      loading: false,
      initialEmail: "",
      editBtnContent: "Edit",
      delay: ms => new Promise(res => setTimeout(res, ms))
    };

  },
  mounted() {
    this.getAllAccounts();
    this.initialEmail = this.user.email;
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
    async updateUser() {
      console.table(this.user);
      let request = {
        userId: this.user.id,
        userData: this.user
      }
      try {
        await this.$store.dispatch('updateUser', request);
        this.loading = true;
        this.statusPopupCardEnabled = true;
        this.statusTitle = `User updated successfully`;
        if (this.ownEmailChanged()) {
          this.statusTitle = `User updated successfully`;
          this.statusSubTitle = `You need to login again because of changing your email`;
        }
        await this.delay(500);
        this.loading = false;
      } catch (error) {
        alert(error.message);
      }

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
    goToLogin() {
      this.$store.dispatch('logout');
      this.$router.push('/login')
    },
    ownEmailChanged() {
      return this.initialEmail.localeCompare(this.user.email) && this.user.id == this.$store.state.userId
    },
    setEditBtnContentToSave() {
      if (!editBtnContent == 'Save')
        this.editBtnContent = 'Save'; ƒ
    }
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

  &:hover {
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


.card {
  overflow: hidden;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: left;
  border-radius: 0.5rem;
  max-width: 30rem;

  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  background-color: #fff;
  z-index: 30;
}


.header {
  padding: 1.25rem 1rem 1rem 1rem;
}

.image {
  display: flex;
  margin-left: auto;
  margin-right: auto;
  background-color: #e2feee;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  width: 4rem;
  height: 4rem;
  border-radius: 9999px;
  animation: animate .6s linear alternate-reverse infinite;
  transition: .6s ease;

  img {
    height: 80%;
    width: 80%;
  }
}


.content {
  margin-top: 0.75rem;
  text-align: center;
  gap: 10px;
  white-space: normal;
}

.title {
  color: #121212;
  font-size: 1rem;
  font-weight: 500;
  padding: 0 5px;
  line-height: 1.5rem;

  &:nth-child(2n) {
    color: #858585;
    font-size: 0.9rem;

  }
}

.message {
  margin-top: 0.5rem;
  color: #595b5f;
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.actions {
  margin: 0.75rem 1rem;
}

.history {
  display: inline-flex;
  padding: 0.5rem 1rem;
  margin-top: 10%;
  background-color: #3f50ea;
  color: #ffffff;
  font-size: 1rem;
  line-height: 1.5rem;
  font-weight: 500;
  justify-content: center;
  width: 100%;
  border-radius: 0.375rem;
  border: none;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);

  &:hover {
    filter: contrast(1.2);
  }
}



@keyframes animate {
  from {
    transform: scale(1);
  }

  to {
    transform: scale(1.09);
  }
}

.errorMsg {
  font-size: smaller;
  margin-bottom: 0;
}

.spinnerContainer {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.spinner {
  width: 56px;
  height: 56px;
  display: grid;
  border: 4px solid #0000;
  border-radius: 50%;
  border-right-color: #299fff;
  animation: tri-spinner 1s infinite linear;
}

.spinner::before,
.spinner::after {
  content: "";
  grid-area: 1/1;
  margin: 2px;
  border: inherit;
  border-radius: 50%;
  animation: tri-spinner 2s infinite;
}

.spinner::after {
  margin: 8px;
  animation-duration: 3s;
}

@keyframes tri-spinner {
  100% {
    transform: rotate(1turn);
  }
}

.loader {
  color: #4a4a4a;
  font-family: "Poppins", sans-serif;
  font-weight: 500;
  font-size: 23px;
  -webkit-box-sizing: content-box;
  box-sizing: content-box;
  height: 40px;
  padding: 10px 10px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  border-radius: 8px;
}

.words {
  overflow: hidden;
}

.word {
  display: block;
  height: 100%;
  color: #299fff;
  text-align: center;
  animation: cycle-words 3s infinite;
}

@keyframes cycle-words {
  0% {
    -webkit-transform: translateY(50%);
    transform: translateY(50%);
  }

  5% {
    -webkit-transform: translateY(0%);
    transform: translateY(0%);
  }

  30% {
    -webkit-transform: translateY(0%);
    transform: translateY(0%);
  }

  45% {
    -webkit-transform: translateY(-105%);
    transform: translateY(-105%);
  }

  65% {
    -webkit-transform: translateY(-105%);
    transform: translateY(-105%);
  }

  75% {
    -webkit-transform: translateY(-200%);
    transform: translateY(-200%);
  }


  90% {
    -webkit-transform: translateY(-200%);
    transform: translateY(-200%);
  }

  100% {
    -webkit-transform: translateY(-300%);
    transform: translateY(-300%);
  }
}
</style>
