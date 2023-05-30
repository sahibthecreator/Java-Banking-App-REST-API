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
  <div class="transactionPage">
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
            <li v-for="option in accountOptions" :key="option" @click="selectAccount(option)">
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
            <li v-for="option in filteredAccountOptions" :key="option" @click="selectAccountTo(option)">
              <div>
                <p class="m-0">{{ option.iban }}</p>
                <p class="m-0">{{ option.typeOfAccount }}</p>
              </div>
              <p>€{{ option.balance }}</p>
            </li>
          </ul>
        </div>
      </div>

      <b-button variant="dark_primary" v-on:click="make_transaction">Transfer</b-button>
      <span class="tos">By clicking transfer, I authorize WAVR to initiate the transaction detailed above</span>
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
      accountTo: "",
      showDropdown: false,
      showDropdownToAccount: false,
      accountOptions: this.$store.state.accounts
    }
  },
  computed: {
    filteredAccountOptions() {
      const filteredOptions = this.accountOptions.filter(option => option.iban != this.accountFrom);
      return filteredOptions;
    }
  },
  methods: {
    async make_transaction() {
      console.log(this.amount, this.accountFrom, this.accountTo);
    },
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
      this.showDropdownToAccount = false;
    },
    selectAccount(option) {
      this.accountFrom = option;
      this.toggleDropdown;
    },
    toggleDropdownToAccount() {
      this.showDropdownToAccount = !this.showDropdownToAccount;
      this.showDropdown = false;
    },
    selectAccountTo(option) {
      this.accountTo = option.iban;
      this.toggleDropdownToAccount;
    },
    selectOtherAccount() {
      this.accountTo = "NL";
      const inputElement = document.querySelector(".inputContainer input");
      inputElement.focus();
    }
  }
}; 
</script>


<style lang="scss">
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
</style>