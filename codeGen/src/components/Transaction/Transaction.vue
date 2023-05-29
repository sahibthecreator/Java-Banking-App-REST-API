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
        <input class="amountInput" placeholder="0.00" type="number" v-model="amount" step="any"/>
      </div>
      <div class="inputContainer">
        <span>Transfer from</span>
        <input placeholder="Select" v-model="accountFrom"/>
      </div>
      <div class="inputContainer">
        <span>Transfer to</span>
        <input placeholder="Select" v-model="accountTo"/>
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
      accountTo: ""
    }
  },
  methods: {
    async make_transaction() {
      console.log(this.amount, this.accountFrom, this.accountTo);
    }
  }
}; 
</script>


<style>


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
  padding: 50px;
  overflow: auto;
}

.actionContent > span {
  font-size: 20px;
}

.actionContent > button {
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

.amount > span {
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


.inputContainer > span {
  font-size: 16px;
  font-weight: 600;
}

.inputContainer > input {
  font-size: 20px;
  font-weight: 400;
  border: none;
  background: transparent;
  color: var(--gray-light);
}

.inputContainer > input::placeholder {
  color: var(--gray-light);
}


.actionContent > .tos {
  font-size: 14px;
  font-weight: 200;
  text-align: center;
  color: var(--gray-light);
}

</style>