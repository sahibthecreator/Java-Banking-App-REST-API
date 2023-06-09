<script setup>
import AccountWidget from '../AccountWidget.vue';
import CurrentAccountPanel from '../CurrentAccountPanel.vue';
import TransactionWidget from '../TransactionWidget.vue';
</script>

<template>
  <div class="dashboardPanel">
    <div class="infoCards">
      <div class="card">
        <div class="header">
          <span v-if="!currentAccount">Total Balance</span>
          <span v-else>Balance</span>
          <b-icon-currency-euro></b-icon-currency-euro>
        </div>
        <span class="contentBig" v-if="!currentAccount">
          €
          {{
            accounts ?
            formatPrice(
              accounts?.reduce((sum, account) => sum + account.balance, 0)
            )
            : "0"
          }}
        </span>
        <span class="contentBig" v-else>
          € {{ formatPrice(currentAccount.balance) }}
        </span>
        <span class="contentSmall"> +20.1% from last month</span>
      </div>
      <div class="card">
        <div class="header">
          <span v-if="!currentAccount">Remaining daily limit</span>
          <span v-else>IBAN</span>
          <b-icon-clock-history></b-icon-clock-history>
        </div>
        <span class="contentBig" v-if="!currentAccount">
          € {{ remainingDayLimit ? remainingDayLimit : "0" }}</span>
        <span class="contentBig" v-else> {{ currentAccount.iban }}</span>
        <span class="contentSmall" v-if="!currentAccount">
          <span id="timer-text">... remaining</span></span>
        <span class="contentSmall" v-else>
          {{ currentAccount.typeOfAccount }}
        </span>
      </div>
      <div class="card">
        <div class="header">
          <span v-if="!currentAccount">Transaction limit</span>
          <span v-else>Absolute limit</span>
          <b-icon-flag></b-icon-flag>
        </div>
        <span class="contentBig" v-if="!currentAccount"> € {{ user?.transactionLimit }}</span>
        <span class="contentBig" v-else>
          € {{ formatPrice(currentAccount.absoluteLimit) }}</span>
        <span class="contentSmall" v-if="!currentAccount && !user"> 109 </span>
      </div>
      <div class="card">
        <div class="header">
          <span v-if="!currentAccount">monthly Balance Status</span>
          <span v-else>Latest Transaction</span>
          <b-icon-credit-card-fill></b-icon-credit-card-fill>
        </div>
        <span class="contentBig" v-if="!currentAccount">
          - $213.47 (static)</span>
        <span class="contentBig" v-else>
          <span v-if="currentTransactions[0]">{{ currentTransactions[0].amount }}</span>
          <span v-else>None</span>
        </span>
        <span class="contentSmall"> -2.1% from last month</span>
      </div>
    </div>

    <div class="mainContent">
      <div class="card left">
        <div class="header">
          <h3 v-if="!currentAccount">Accounts</h3>
          <h3 v-else @click="currentAccount = null" style="cursor: pointer">
            <b-icon-arrow-left> </b-icon-arrow-left> {{ currentAccount.iban }}
          </h3>
        </div>
        <div class="accountsWrapper" v-if="!currentAccount">
          <AccountWidget v-for="(account, index) in accounts" :key="index" :balance="account.balance"
            :name="user.firstName" :iban="account.iban" :type="account.typeOfAccount"
            @click.native="setCurrentAccountView(account)" />
        </div>
        <CurrentAccountPanel :currentAccount="currentAccount" :user="user" @returnBack="returnDashboardView"
          v-if="currentAccount">
        </CurrentAccountPanel>
      </div>
      <div class="card right">
        <div class="header">
          <h3>Recent Actions</h3>
          <div>
            <p v-if="currentAccount">
              You made/received {{ currentTransactions.length }} transactions
              this week
            </p>
            <p v-else>
              You made/received {{ currentTransactions?.length }} transactions this week
            </p>
          </div>
        </div>
        <div class="transactionsWrapper">
          <TransactionWidget v-for="(transaction, index) in currentTransactions" :key="index" :transaction="transaction"
            :currentAccount="currentAccount" :accounts="accounts" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
var date = new Date();
var second = date.getSeconds();
var minute = date.getMinutes();
var hour = date.getHours();
var leftHour = 23 - hour;
var leftMinute = 59 - minute;
var leftSeconds = 59 - second;

var leftTime = leftHour * 3600 + leftMinute * 60 + leftSeconds;

function updateTimer() {
  var timer = document.getElementById('timer-text');
  if (!timer) {
    return;
  }
  var h = Math.floor(leftTime / 3600);
  var m = Math.floor((leftTime - h * 3600) / 60);
  var s = Math.floor(leftTime % 60);

  timer.innerHTML = h + ' : ' + m + ' : ' + (s + 1) + ' remaining';

  leftTime--;
}
setInterval(updateTimer, 1000);

export default {
  name: 'OverviewTab',
  data() {
    return {
      user: null,
      accounts: null,
      currentTransactions: null,
      transactions: null,
      balance: 0,
      currentAccount: null,
      remainingDayLimit: null,
      timer: null
    };
  },
  mounted() {
    this.getUserAndAccountsAndTransactions();
  },
  methods: {
    setCurrentAccountView(account) {
      this.currentAccount = account;
      this.currentTransactions = this.transactions.filter(
        (t) =>
          t.fromAccount == this.currentAccount.iban ||
          t.toAccount == this.currentAccount.iban
      );
    },
    returnDashboardView() {
      this.currentAccount = null;
      this.currentTransactions = this.transactions;
    },
    formatPrice(value) {
      let val = (value / 1).toFixed(2).replace('.', ',');
      return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    },
    async getUserAndAccountsAndTransactions() {
      try {
        let user = await this.$store.dispatch(
          'getUser',
          this.$store.getters.getUserId
        );
        this.user = user;
        let accounts = await this.$store.dispatch(
          'getAccountsByUserId',
          this.$store.getters.getUserId
        );
        this.accounts = accounts;
        let transactions = await this.$store.dispatch(
          'getTransactionsByUserId',
          this.$store.getters.getUserId
        );
        this.transactions = transactions;
        this.currentTransactions = this.transactions;

        let dayLimit = await this.$store.dispatch(
          'getRemainingDayLimit',
          this.$store.getters.getUserId
        );

        this.remainingDayLimit = dayLimit.remainingDayLimit;
      } catch (error) {
        console.log(error);
        if (this.user == null) {
          this.$store.dispatch('logout');
          location.reload();
        }
      }
    },
    relocate_to_transaction() {
      this.$router.push({ name: 'transfer', params: { accounts: '22' } });
    },
    relocate_to_request() {
      this.$router.push('dashboard/requestAccount');
    },
  },
};
</script>

<style lang="scss" scoped>
.card {
  padding: 35px;
  background: var(--white);
  box-shadow: 0 0 30px #1414140a;
  border-radius: 10px;
}

.dashboardPanel {
  width: 90vw;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;

  .infoCards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    padding: 25px 0px;

    .card {
      .header {
        display: flex;
        justify-content: space-between;
        color: var(--gray-dark);

        span {
          font-weight: 500;
        }
      }

      .contentBig {
        font-size: 1.75rem;
      }

      .contentSmall {
        font-weight: 500;
        color: var(--gray-light);
        padding: 5px 0px;
        font-size: 0.9rem;
      }
    }
  }

  .mainContent {
    display: grid;
    grid-template-columns: 55% auto;
    gap: 20px;
    height: 100%;

    .header {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      color: var(--gray-dark);

      span {
        font-weight: 500;
      }

      p {
        color: var(--gray-light);
      }
    }

    .left {
      padding: 35px;
      background: var(--white);
      box-shadow: 0 0 30px #1414140a;
      border-radius: 10px;
      height: 450px;

      .accountsWrapper {
        padding: 20px;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        gap: 20px;
        overflow: auto;
        white-space: nowrap;
      }

      /* width */
      ::-webkit-scrollbar {
        width: 15px;
        transform: translateY(-10px);
      }

      /* Track */
      ::-webkit-scrollbar-track {
        border: 1px var(--gray-black) solid;
        margin: 20px;
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
    }

    .right {
      height: 450px;

      .transactionsWrapper {
        height: 100%;
        overflow: auto;
        white-space: no-wrap;
      }
    }
  }
}
</style>
