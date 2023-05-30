<script setup>
import Navigation from '@/components/Navigation.vue'
import AccountWidget from './AccountWidget.vue';
import TransactionWidget from './TransactionWidget.vue';
</script>

<template>
  <div id="header">
    <navigation />
  </div>

  <div class="dashboardPage">
    <div class="panel">
      <div class="topPart">
        <div class="dashboardName">
          <h1 class="welcomeText">Good afternoon,<br>
            {{ user ? user.firstName : "" }} {{ user ? user.lastName[0] : "" }}.
          </h1>

          <div class="accounts">
            <AccountWidget v-for="(account, index) in accounts" :key="index" :balance="account.balance"
              :name="user.firstName" />
          </div>

        </div>
        <div class="dashboardAction">
          <b-button variant="dark_primary" @click="relocate_to_transaction()">Send money</b-button>
          <b-button variant="gray_dark">View transaction history</b-button>
          <b-button variant="black" @click="relocate_to_request()">Request an account</b-button>
        </div>
        <div class="chart">
          <apexchart type="line" height="280px" :options="chartOptions" :series="chartSeries" />
        </div>
      </div>

      <div class="transactions">
        <TransactionWidget v-for="(transaction, index) in transactions" :key="index" :amount="transaction.amount"
          :toIban="transaction.toIban" />

      </div>
    </div>


  </div>
</template>

<script>
import VueApexCharts from 'vue-apexcharts';

export default {
  name: 'Dashboard',
  components: {
    apexchart: VueApexCharts,
  },
  data() {
    return {
      user: null,
      accounts: null,
      transactions: null,
      chartOptions: {
        chart: {
          type: 'line',
          zoom: {
            enabled: false
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'straight'
        },
        title: {
          text: 'Total Spent by Month',
          align: 'left'
        },
        grid: {
          row: {
            colors: ['#f3f3f3', 'transparent'],
            opacity: 0.5
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
    }
  },
  mounted() {
    this.getUserAndAccountsAndTransactions();
  },
  methods: {
    async getUserAndAccountsAndTransactions() {
      try {
        let user = await this.$store.dispatch('getUser', this.$store.getters.getUserId);
        this.user = user;
        let accounts = await this.$store.dispatch('getAccountsByUserId', this.$store.getters.getUserId);
        this.accounts = accounts;
        let transaction = await this.$store.dispatch('getTransactionsByUserId', this.$store.getters.getUserId);
        this.transactions = transaction;

      } catch (error) {
        console.log(error);
        if (this.user == null) {
          this.$store.dispatch('logout');
        }
      }
    },
    relocate_to_transaction() {
      this.$router.push({ name: 'transfer', params: { accounts: "22" } });
    },
    relocate_to_request() {
      this.$router.push('/dashboard/requestAccount')
    }
  },
};


</script>


<style>
.dashboardPage {
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

.panel {
  background: var(--white);
  width: 90vw;
  border-radius: 35px;
  background-color: var(--white);
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: space-between;
  padding: 30px;
}

.topPart {
  display: flex;
}

.btn {
  border: 0px !important;
  border-radius: 7px !important;
  padding: 10px 15px !important;
}


.btn::-moz-focus-outer {
  outline: none;
}

.welcomeText {
  font-size: 2.3vw;
  width: fit-content;
}

.dashboardName {
  display: flex;
  flex-direction: column;
  width: 50%;
}

.accounts {
  width: 95%;
  margin-top: 5%;
  display: flex;
  gap: 15px;
  overflow: auto;
  white-space: nowrap;
  border-radius: 15px;
  box-shadow: 0 0 30px #14141417;
  padding: 15px;
  border: 15px var(--white) solid;
}



/* width */
::-webkit-scrollbar {
  width: 100px;
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


.transactions {
  background: white;
  width: 100%;
  border-radius: 35px;
  box-shadow: 0 0 30px #14141417;
  display: flex;
  flex-direction: row;
  gap: 20px;
  padding: 20px;
}

.dashboardAction {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10%;
  margin-left: auto;
  margin-right: 5%;
}

.chart {
  background-color: white;
  border-radius: 35px;
  padding: 10px;
  height: 100%;
}
</style>