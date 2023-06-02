<script setup>
import AccountIcon from '@/components/Dashboard/AccountIcon.vue';
</script>

<template>
  <div class="requestWidget">
    <AccountIcon :accountName="request.fullName" class="icon" />
    <div class="d-flex user">
      <span class="userInfo"><b>{{ request.fullName }}</b></span>
      <span class="userInfo">Requested on: {{ request.requestDate }}</span>
    </div>
    <div class="requestControls">
      <button class="button btn-black" @click="detailsPanelEnabled = true">View</button>
    </div>
  </div>

  <div class="blurdBg" v-if="detailsPanelEnabled">
    <div class="card">
      <!-- Loading -->
      <div class="spinnerContainer" v-if="loading">
        <div class="spinner"></div>
        <div class="loader">
          <div class="words">
            <span class="word">Accepting Request</span>
            <span class="word">Creating a bank account</span>
          </div>
        </div>
      </div>
      <!-- /Loading -->
      <div v-if="!loading">
        <button type="button" class="dismiss" @click="detailsPanelEnabled = false">×</button>
        <div class="content">
          <p class="heading" :class="{ 'mt-4': request.status != 'pending' }">Account Request
            {{ request.status == 'pending'
              ? ''
              : request.status }}
          </p>
          <p class="para" v-if="request.status == 'pending'">
            {{ request.fullName }} requested for a <b>{{ request.accountType }}</b> Account
            <br>
            <b>User Id:</b> {{ request.userId }}
          </p>
          <div class="actionBtns" v-if="request.status == 'pending'">
            <button class="btn approve" @click="approveRequest">Approve</button>
            <button class="btn deny" @click="denyRequest">Deny</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<!-------------------------------------------------------------------
                            JavaScript
-------------------------------------------------------------------->
<script>
export default {
  name: 'RequestWidget',
  props: {
    request: Object,
  },
  data() {
    return {
      detailsPanelEnabled: false,
      loading: false,
      delay: (ms) => new Promise((res) => setTimeout(res, ms)),
    };
  },
  mounted() { },
  methods: {
    async approveRequest() {
      try {
        await this.$store.dispatch('approveRequest', this.request.id);
        this.loading = true;
        await this.delay(1500);
        this.loading = false;
        await this.delay(1);
        document.getElementsByClassName("heading")[0].innerHTML = "Account Request Approved";
        document.getElementsByClassName("heading")[0].classList.add("mt-4")
        document.getElementsByClassName("para")[0].innerHTML = "";
        document.getElementsByClassName("actionBtns")[0].innerHTML = "";
      } catch (error) {
        console.log(error);
      }
    },
    async denyRequest() {
      try {
        await this.$store.dispatch('denyRequest', this.request.id);
        document.getElementsByClassName("heading")[0].innerHTML = "Account Request Denied";
        document.getElementsByClassName("heading")[0].classList.add("mt-4")
        document.getElementsByClassName("para")[0].innerHTML = "";
        document.getElementsByClassName("actionBtns")[0].innerHTML = "";
      } catch (error) {
        console.log(error);
      }
    },
  },
};
</script>

<!-------------------------------------------------------------------
                                CSS
-------------------------------------------------------------------->
<style lang="scss" scoped>
.requestWidget {
  height: fit-content;
  width: 95%;
  background-color: var(--white);
  padding: 15px 15px;
  border-radius: 15px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  box-shadow: 0 0 30px #14141417;

  .icon {
    width: 57px;
    height: 57px;
    aspect-ratio: 1;
  }

  .user {
    width: fit-content;
    display: flex;
    flex-direction: column;
    gap: 5px;

    .userInfo {
      color: var(--gray-black);
    }

    .userInfo:nth-child(2) {
      color: var(--gray-light);
    }
  }

  .requestControls {

    margin-left: auto;
    display: flex;
    flex-direction: column;
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

.blurdBg {
  width: 100vw;
  height: 100vh;
  position: fixed;
  background-color: #626262b4;
  z-index: 10;
  left: 0;
  top: 0;
}

.card {
  position: fixed;
  top: 70%;
  left: 50%;
  transform: translate(-50%, -100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 10px rgba(0, 0, 0, 0.1);
  padding: 32px;
  white-space: normal;
  overflow: hidden;
  border-radius: 10px;
  background: #fcfcfc;
  border: 2px solid #ffffff;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.320, 1);
  z-index: 20;

  .actionBtns {
    width: 100%;
    display: flex;
    justify-content: space-between;

  }

  .dismiss {
    position: absolute;
    right: -5%;
    top: -10%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0.5rem 1rem;
    background-color: transparent;
    color: black;
    border: 0px;
    font-size: 1rem;
    font-weight: 300;
    width: 30px;
    height: 30px;
    border-radius: 7px;
    transition: .3s ease;
  }

  .dismiss:hover {
    background-color: #e03131;
    color: #fff;
  }

}

.content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 20px;
  color: #1b1b1b;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.320, 1);
}

.content .heading {
  font-weight: 700;
  font-size: 32px;
}

.content .para {
  line-height: 1.5;
}

.content .btn {
  color: #e8e8e8;
  text-decoration: none;
  padding: 10px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  background: #1b1b1b;
  border-radius: 5px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
  transition: all 0.2s;
  min-width: 45%;

  &.deny {
    background: #dd3e4c;
  }

  &.approve {
    background: #205197;
  }
}

.card:hover {
  box-shadow: 0 20px 20px rgba(0, 0, 0, 0.3);
}

.content .btn:hover {
  outline: 2px solid #e8e8e8;
  background: transparent;
  color: #1b1b1b;
}

.content .btn:active {
  box-shadow: none;
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
  content: '';
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
  font-family: 'Poppins', sans-serif;
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
