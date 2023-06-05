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
  <div id="line"></div>
  <div class="transactionPage" id="transactionPage">
    <div class="actionContent">
      <span>Deposit funds into an account</span>
      <div class="titleContainer">
        <img src="@/assets/Transaction/Transfer_icon.svg" />
        <span>Bank transfer</span>
      </div>
      <span>Amount</span>
      <div class="amount">
        <span>€</span>
        <input class="amountInput" placeholder="0.00" type="number" v-model="amount" step="any" />
      </div>
      <div class="inputContainer" @click="toggleDropdown">
        <span>Transfer from</span>
        <input placeholder="Select" readonly v-model="accountFrom.iban" />
        <i class="dropdown-icon" :class="{ 'active': showDropdown }"></i>
        <div class="dropdown-accounts" v-show="showDropdown">
          <ul>
            <li v-for="option in accountOptions" :key="option" @click="selectAccount(option)"
              :class="{ optionDisabled: option.iban == accountTo }">
              <div>
                <p class="m-0">{{ option.iban }}</p>
                <p class="m-0">{{ option.typeOfAccount }}</p>
              </div>
              <p>€{{ option.balance }}</p>
            </li>
          </ul>
        </div>
      </div>
      <div class="inputContainer" @click="toggleDropdownToAccount">
        <span>Transfer to</span>
        <input placeholder="Select" v-model="accountTo" />
        <i class="dropdown-icon" :class="{ 'active': showDropdownToAccount }"></i>
        <div class="dropdown-accounts-to" v-show="showDropdownToAccount">
          <ul>
            <li v-if="accountFrom.typeOfAccount !== 'SAVINGS'" @click="selectOtherAccount">Other account</li>
            <li v-for="option in filteredAccountOptions" :key="option" @click="selectAccountTo(option)"
              :class="{ optionDisabled: option.iban == accountFrom.iban }">
              <div>
                <p class="m-0">{{ option.iban }}</p>
                <p class="m-0">{{ option.typeOfAccount }}</p>
              </div>
              <p>€{{ option.balance }}</p>
            </li>
          </ul>
        </div>
      </div>
      <div class="inputContainer">
        <input placeholder="Description" v-model="description" />
      </div>
      <b-button variant="dark_primary" v-on:click="performTransaction" id="transferBtn">Transfer</b-button>
      <p class="text-danger errorMsg">{{ errorMsg }}</p>
      <span class="tos">By clicking transfer, I authorize WAVR to initiate the transaction detailed above</span>
    </div>
  </div>

  <div class="card" v-if="statusPopupCardEnabled">
    <div class="header">
      <!-- Loading -->
      <div class="spinnerContainer" v-if="loading">
        <div class="spinner"></div>
        <div class="loader">
          <div class="words">
            <span class="word">Verifying credentials</span>
            <span class="word">Sending to recipient</span>
            <span class="word">Receiving transaction</span>
          </div>
        </div>
      </div>
      <!-- /Loading -->

      <div class="image" v-if="!loading">
        <img src="@/assets/SuccessIcon.png" class="success icon" />
      </div>
      <div class="content" v-if="!loading">
        <span class="title">{{ statusTitle }}</span>
      </div>
      <div class="actions">
        <button type="button" class="history" @click="this.$router.push('/dashboard')" v-if="!loading">Return to
          dashboard</button>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: 'Transaction',
  data() {
    return {
      amount: "",
      accountFrom: "",
      accountTo: this.$route.query.receiverIban? this.$route.query.receiverIban : "",
      description: "",
      showDropdown: false,
      showDropdownToAccount: false,
      accountOptions: this.$store.state.accounts,
      statusPopupCardEnabled: false,
      statusTitle: "",
      errorMsg: "",
      loading: false,
      delay: ms => new Promise(res => setTimeout(res, ms))
    }
  },
  mounted() {
  },
  computed: {
    filteredAccountOptions() {
      const filteredOptions = this.accountOptions.filter(option => option.iban != this.accountFrom);
      return filteredOptions;
    },

  },
  methods: {
    async performTransaction() {
      try {
        const transactionData = {
          fromAccount: this.accountFrom.iban,
          toAccount: this.accountTo,
          amount: this.amount,
          performingUser: this.$store.getters.getUserId,
          description: this.description,
          typeOfTransaction: "TRANSFER"
        };
        await this.$store.dispatch('performTransaction', transactionData);
        this.loading = true;
        this.statusPopupCardEnabled = true;
        this.statusTitle = `You successfully sent €${this.amount} to ${this.accountTo}`;
        document.getElementById("transactionPage").style.filter = "blur(5px)";
        document.getElementById("transactionPage").style.cursor = "default";
        document.getElementById("transferBtn").disabled = true;
        await this.delay(2000);
        this.loading = false;
      } catch (error) {
        this.errorMsg = error;
      }

    },
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
      this.showDropdownToAccount = false;
    },
    selectAccount(option) {
      if (option.iban != this.accountTo) {
        this.accountFrom = option;
        this.toggleDropdown;
      }
    },
    toggleDropdownToAccount() {
      this.showDropdownToAccount = !this.showDropdownToAccount;
      this.showDropdown = false;
    },
    selectAccountTo(option) {
      if (option.iban != this.accountFrom.iban) {
        this.accountTo = option.iban;
        this.toggleDropdownToAccount;
      }
    },
    selectOtherAccount() {
      this.accountTo = "NL";
      const inputElement = document.querySelector(".inputContainer input");
      inputElement.focus();
    }
  }
}; 
</script>


<style lang="scss" scoped>
#nav {
  height: 100px;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 150px;
}


.transactionPage {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.actionContent {
  width: 430px;
  height: fit-content;
  display: flex;
  flex-direction: column;
  gap: 2.5vh;
  padding: 2%;
  padding-top: 0;
  overflow: auto;
}

.actionContent>span {
  font-size: 20px;
}

.actionContent>button {
  background: var(--blue-dark);
}


.titleContainer {
  display: flex;
  flex-direction: row;
  gap: 20px;
  font-size: 36px;
}


.amount {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-top: -20px;
}

.amount>span {
  font-size: 42px;
  color: var(--gray-dark);
}

.amountInput {
  font-weight: bold;
  font-size: 64px;
  background: transparent;
  border: none;
  width: 100%;
}


/* remove number arrows for amountInput*/
.amountInput::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.amountInput[type=number] {
  -moz-appearance: textfield;
}

.inputContainer {
  font-size: 16px;
  padding: 20px;
  border-radius: 7px;
  border: 1px var(--gray-dark) solid;
  font-weight: 500;
  display: flex;
  flex-direction: column;
  gap: 10px;
}


.inputContainer>span {
  font-size: 16px;
  font-weight: 600;
}

.inputContainer>input {
  font-size: 20px;
  font-weight: 400;
  border: none;
  background: transparent;
  color: var(--gray-light);

  &:focus {
    outline: none;
  }
}

.inputContainer>input::placeholder {
  color: var(--gray-light);
}


.actionContent>.tos {
  font-size: 14px;
  font-weight: 200;
  text-align: center;
  width: 100%;
  color: var(--gray-light);
}

.dropdown-icon {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  width: 30px;
  height: 30px;
  background-image: url("https://icons.veryicon.com/png/o/miscellaneous/decon/dropdown-1.png");
  background-repeat: no-repeat;
  background-size: cover;
  cursor: pointer;
  transition: all 0.5s;
}

.dropdown-icon.active {
  transform: translateY(-50%) rotate(180deg);
}

.dropdown-accounts,
.dropdown-accounts-to {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  z-index: 2;
  max-height: 200px;
  overflow-y: auto;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }

  li {
    padding: 8px 16px;
    display: flex;
    justify-content: space-between;
    cursor: pointer;

    &:hover {
      background-color: #f5f5f5;
    }
  }
}

::-webkit-scrollbar {
  width: 0px;
  transform: translateY(-10px);
}

/* Track */
::-webkit-scrollbar-track {
  border: 1px var(--gray-black) solid;
  margin: 0px;
  border-radius: 30px;
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: var(--gray-dark);
  border-radius: 10px;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: var(--gray-black);
}

.optionDisabled {
  opacity: 0.5;
  cursor: not-allowed !important;

  &:hover {
    background-color: #ffffff !important;
  }
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
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
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
}

.title {
  color: #066e29;
  font-size: 1rem;
  font-weight: 600;
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
  background-color: #145c40;
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