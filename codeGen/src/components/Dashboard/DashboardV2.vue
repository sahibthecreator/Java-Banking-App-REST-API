<script setup>
import Navigation from '@/components/Navigation.vue';
import OverviewTab from './Tabs/OverviewTab.vue';
import TransactionTab from './Tabs/TransactionTab.vue';
import PeopleTab from './Tabs/PeopleTab.vue';
</script>

<template>
  <div id="header">
    <navigation />
  </div>

  <div class="dashboardPage">
    <div class="titleBar"><h1 id="title">Dashboard</h1></div>
    <div class="secondTitleBar">
      <div class="options">
        <option v-bind:id="currentTab == 0 ? 'selected' : ''" @click="switchTab(this, 'overview')">Overview</option>
        <option v-bind:id="currentTab == 1 ? 'selected' : ''" @click="switchTab(this, 'transactions')">Transactions</option>
        <option v-bind:id="currentTab == 2 ? 'selected' : ''" @click="switchTab(this, 'people')">People</option>
      </div>
      <b-button variant="dark_primary" @click="relocate_to_transaction()"
        >New Transaction</b-button
      >
    </div>
    <OverviewTab v-if="currentTab==0"/>
    <TransactionTab v-if="currentTab==1"/>
    <PeopleTab v-if="currentTab==2"/>
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
      currentTab: 0
    };
  },
  methods: {
    formatPrice(value) {
      let val = (value / 1).toFixed(2).replace('.', ',');
      return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    },
    relocate_to_transaction() {
      this.$router.push({ name: 'transfer', params: { accounts: '22' } });
    },
    relocate_to_request() {
      this.$router.push('dashboard/requestAccount');
    },
    switchTab(elem, tab) {
      switch (tab) {
        case "overview":
          this.currentTab = 0;
          break;
        case "transactions":
          this.currentTab = 1;
          break;  
        case "people":
          this.currentTab = 2;
          break;
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.dashboardPage {
  height: fit-content;
  width: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-inline: 100px;

  .titleBar {
    height: 75px;
    width: 90vw;
    display: flex;
    align-items: center;
    padding-inline: 30px;
    padding-block: 25px;

    #title {
      color: var(--gray-dark);
    }
  }
  .secondTitleBar {
    width: 90vw;
    display: flex;
    flex-direction: row;
    padding-inline: 30px;
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

      #selected {
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
}
</style>
