<script setup>
import Navigation from '@/components/Navigation.vue';
import AccountWidgetV2 from './AccountWidgetV2.vue';
import TransactionWidgetV2 from './TransactionWidgetV2.vue';
</script>

<template>
  <div id="header">
    <navigation />
  </div>

  <div class="dashboardPage">
    <div class="dashboardPanel">
      <div class="titleBar"><h1 id="title">Dashboard</h1></div>
      <div class="secondTitleBar">
        <div class="options">
          <option class="selected">Overview</option>
          <option>Accounts</option>
          <option>Transactions</option>
          <option>People</option>
        </div>
        <b-button variant="dark_primary" @click="relocate_to_transaction()">New Transaction</b-button>
      </div>
      <div class="infoCards">
        <div class="card">
          <div class="header">
            <span>Total Balance</span>
            <i class="bi bi-currency-euro"></i>
          </div>
          <span class="contentBig"> $45,232.89 </span>
          <span class="contentSmall"> +20.1% from last month</span>
        </div>
        <div class="card">
          <div class="header">
            <span>Remaining daily balance</span>
            <i class="bi bi-currency-euro"></i>
          </div>
          <span class="contentBig"> $200.23 </span>
          <span class="contentSmall"> 12h 2m 12s remaining</span>
        </div>
        <div class="card">
          <div class="header">
            <span>Daily Transactions remaining</span>
            <i class="bi bi-currency-euro"></i>
          </div>
          <span class="contentBig"> 12 </span>
          <span class="contentSmall"> 12 of 15 </span>
        </div>
        <div class="card">
          <div class="header">
            <span>monthly Balance Status</span>
            <i class="bi bi-currency-euro"></i>
          </div>
          <span class="contentBig"> - $213.47 </span>
          <span class="contentSmall"> -2.1% from last month</span>
        </div>
      </div>

      <div class="mainContent">
        <div class="card left">
          <div class="header">
            <h3>Accounts</h3>
          </div>
          <div class="accountsWrapper">
            <AccountWidgetV2
              v-for="(account, index) in accounts"
              :key="index"
              :balance="account.balance"
              :name="user.firstName"
              :iban="account.iban"
              :type="account.typeOfAccount"
            />
          </div>
        </div>
        <div class="card right">
          <div class="header">
            <h3>Recent Actions</h3>
            <p>You made/received 236 transactions this week</p>
          </div>
          <div class="transactionsWrapper">
            <TransactionWidgetV2
              v-for="(transaction, index) in currentTransactions"
              :key="index"
              :transaction="transaction"
            />
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
export default {
  name: 'DashboardV2',
  data() {
    return {
      user: null,
      accounts: null,
      currentTransactions: null,
      transactions: null,
      balance: 0,
      currentAccount: null,
      chartOptions: {
        chart: {
          type: 'line',
          zoom: {
            enabled: false,
          },
        },
        dataLabels: {
          enabled: false,
        },
        stroke: {
          curve: 'straight',
        },
        title: {
          text: 'Total Spent by Month',
          align: 'left',
        },
        grid: {
          row: {
            colors: ['#f3f3f3', 'transparent'],
            opacity: 0.5,
          },
        },
        xaxis: {
          categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May'], // Replace with actual month labels
        },
      },
      chartSeries: [
        {
          name: 'Total Spent',
          data: [480, 674, 400, 552, 122], // Replace with actual spent amounts
        },
      ],
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
      } catch (error) {
        console.log(error);
        if (this.user == null) {
          this.$store.dispatch('logout');
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

.dashboardPage {
  background-position-x: center;
  background-size: cover;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  .dashboardPanel {
    width: 90vw;
    margin: auto;
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 30px;

    .titleBar {
      height: 75px;
      width: 100%;
      display: flex;
      align-items: center;

      #title {
        color: var(--gray-dark);
      }
    }
    .secondTitleBar {
      display: flex;
      flex-direction: row;
      .options {
        display: flex;
        flex-direction: row;
        padding: 5px;
        gap: 10px;
        background: #ebebeb;
        width: fit-content;
        border-radius: 7px;

        option {
          padding: 5px 15px;
          border-radius: 4px;
          color: var(--gray-light);
          cursor: pointer;
        }

        .selected {
          color: var(--gray-dark);
          background: var(--white);
          box-shadow: 0 0 30px #14141417;
          font-weight: 500;
        }
      }

      button {
        margin-left: auto;
      }

      
    }

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
          font-size: xx-large;
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
}
</style>
