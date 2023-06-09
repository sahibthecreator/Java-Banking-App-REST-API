<script setup></script>

<template>
  <div id="nav">
    <b-navbar-brand to="/">
      <img src="@/assets/Logo.svg" alt="logo" id="logo" />
      <span id="logoTitle">WAVR</span>
    </b-navbar-brand>

    <b-navbar-nav class="ml-auto">
      <b-nav-item to="/dashboard"><img src="@/assets/Transaction/x.svg" /></b-nav-item>
    </b-navbar-nav>
  </div>
  <div class="requestPage">
    <div class="requestPanel">
      <span>Request an account</span>
      <div class="inputRequestContainer">
        <span>Account type</span>
        <b-form-select v-model="selected" :options="options"></b-form-select>
      </div>
      <b-button variant="dark_primary" v-on:click="requestAccountBtn()">Request account</b-button>
      <p class="text-danger errorMsg">{{ errorMsg }}</p>
      <span class="tos">By clicking request account, I authorize WAVR to initiate an account
        request and accept the Terms of Services</span>
    </div>
  </div>

  <!-- popup -->
  <div class="card" v-if="responsePopupEnabled">
    <div class="header">
      <!-- Loading -->
      <div class="spinnerContainer" v-if="loading">
        <div class="spinner"></div>
        <div class="loader">
          <div class="words">
            <span class="word">Sending Request</span>
            <span class="word">1 more second</span>
          </div>
        </div>
      </div>
      <!-- /Loading -->

      <div class="image" v-if="!loading">
        <img src="@/assets/SuccessIcon.png" class="success icon" />
      </div>
      <div class="content" v-if="!loading">
        <span class="title">Account request sent successfully</span>
      </div>
      <div class="actions">
        <b-button variant="dark_primary" class="history" v-on:click="this.$router.push('/dashboard')"
          v-if="!loading">Return to dashboard</b-button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'RequestAccount',
  data() {
    return {
      selected: 'CURRENT',
      options: [
        { value: 'CURRENT', text: 'Current' },
        { value: 'SAVINGS', text: 'Savings' },
      ],
      responsePopupEnabled: false,
      loading: false,
      delay: (ms) => new Promise((res) => setTimeout(res, ms)),
      errorMsg: '',
    };
  },
  methods: {
    async requestAccountBtn() {
      try {
        const transactionData = {
          userId: this.$store.state.userId,
          accountType: this.selected,
          absoluteLimit: 0,
        };
        await this.$store.dispatch('requestAccount', transactionData);
        this.responsePopupEnabled = true;
        this.loading = true;
        await this.delay(1500);
        this.loading = false;
      } catch (error) {
        this.errorMsg = error;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#nav {
  position: absolute;
  z-index: 10;
  background-color: rgba(255, 255, 255, 0.066);
  width: 100%;
  height: 100px;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 150px;
}

.requestPage {
  background: url('@/assets/dashboard/bg.jpg');
  background-repeat: no-repeat;
  background-clip: border-box;
  background-position-x: center;
  background-size: cover;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 0;
}

.requestPanel {
  background: var(--white);
  min-width: fit-content;
  max-width: 40vw;
  min-height: fit-content;
  border-radius: 35px;
  background-color: var(--white);
  margin: auto;
  display: flex;
  gap: 40px;
  flex-direction: column;
  padding: 30px;
  align-items: center;
}

.requestPanel>span:nth-child(1) {
  font-size: 24px;
}

.tos {
  text-align: center;
  width: 60%;
  font-size: 12px;
  color: var(--gray-light);
}

.inputRequestContainer {
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 7px;
  border: 1px var(--gray-dark) solid;
  font-weight: 500;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 350px;
  width: 350px;
  min-width: 300px;
  margin: 0px auto;
}

.inputRequestContainer>span {
  font-size: 16px;
  font-weight: 500;
}

.inputRequestContainer>input {
  font-size: 20px;
  font-weight: 400;
  border: none;
  background: transparent;
  color: var(--gray-light);
}

.inputRequestContainer>select {
  font-size: 20px;
  font-weight: 400;
  border: none;
  background: transparent;
  color: var(--gray-dark);
  box-shadow: none;
}

.requestPanel>button {
  max-width: 350px;
  width: 350px;
  min-width: 300px;
}

.inputRequestContainer>input::placeholder {
  color: var(--gray-light);
}

.card {
  overflow: hidden;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: left;
  border-radius: 0.5rem;
  max-width: 290px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
  background-color: #fff;
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
  animation: animate 0.6s linear alternate-reverse infinite;
  transition: 0.6s ease;

  img {
    height: 80%;
    width: 80%;
  }
}

.content {
  margin-top: 0.75rem;
  text-align: center;
}

.title {
  color: #000000;
  font-size: 1rem;
  font-weight: 500;
  line-height: 1.5rem;
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
  background-color: #263b8f;
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
