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
    <div class="titleBar">
      <h1 id="title">Dashboard</h1>
    </div>
    <div class="secondTitleBar">
      <div class="options">
        <option v-bind:id="currentTab == 0 ? 'selected' : ''" @click="switchTab(this, 'overview')">Overview</option>
        <option v-bind:id="currentTab == 1 ? 'selected' : ''" @click="switchTab(this, 'transactions')">Transactions
        </option>
        <option v-bind:id="currentTab == 2 ? 'selected' : ''" @click="switchTab(this, 'people')">People</option>
      </div>
      <b-button variant="black" @click="this.$router.push('/dashboard/requestAccount')" style="margin-left: auto;margin-right: 1.5rem;">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bank"
          viewBox="0 0 16 16">
          <path
            d="m8 0 6.61 3h.89a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5H15v7a.5.5 0 0 1 .485.38l.5 2a.498.498 0 0 1-.485.62H.5a.498.498 0 0 1-.485-.62l.5-2A.501.501 0 0 1 1 13V6H.5a.5.5 0 0 1-.5-.5v-2A.5.5 0 0 1 .5 3h.89L8 0ZM3.777 3h8.447L8 1 3.777 3ZM2 6v7h1V6H2Zm2 0v7h2.5V6H4Zm3.5 0v7h1V6h-1Zm2 0v7H12V6H9.5ZM13 6v7h1V6h-1Zm2-1V4H1v1h14Zm-.39 9H1.39l-.25 1h13.72l-.25-1Z" />
        </svg>
        Request account
      </b-button>

      <b-button variant="black" @click="this.$router.push('/atm')" style="margin-right: 1.5rem;">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cash-coin"
          viewBox="0 0 16 16">
          <path fill-rule="evenodd" d="M11 15a4 4 0 1 0 0-8 4 4 0 0 0 0 8zm5-4a5 5 0 1 1-10 0 5 5 0 0 1 10 0z" />
          <path
            d="M9.438 11.944c.047.596.518 1.06 1.363 1.116v.44h.375v-.443c.875-.061 1.386-.529 1.386-1.207 0-.618-.39-.936-1.09-1.1l-.296-.07v-1.2c.376.043.614.248.671.532h.658c-.047-.575-.54-1.024-1.329-1.073V8.5h-.375v.45c-.747.073-1.255.522-1.255 1.158 0 .562.378.92 1.007 1.066l.248.061v1.272c-.384-.058-.639-.27-.696-.563h-.668zm1.36-1.354c-.369-.085-.569-.26-.569-.522 0-.294.216-.514.572-.578v1.1h-.003zm.432.746c.449.104.655.272.655.569 0 .339-.257.571-.709.614v-1.195l.054.012z" />
          <path
            d="M1 0a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h4.083c.058-.344.145-.678.258-1H3a2 2 0 0 0-2-2V3a2 2 0 0 0 2-2h10a2 2 0 0 0 2 2v3.528c.38.34.717.728 1 1.154V1a1 1 0 0 0-1-1H1z" />
          <path d="M9.998 5.083 10 5a2 2 0 1 0-3.132 1.65 5.982 5.982 0 0 1 3.13-1.567z" />
        </svg>
        ATM
      </b-button>

      <b-button variant="dark_primary" @click="this.$router.push('/dashboard/transaction')">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-send-fill"
          viewBox="0 0 16 16">
          <path
            d="M15.964.686a.5.5 0 0 0-.65-.65L.767 5.855H.766l-.452.18a.5.5 0 0 0-.082.887l.41.26.001.002 4.995 3.178 3.178 4.995.002.002.26.41a.5.5 0 0 0 .886-.083l6-15Zm-1.833 1.89L6.637 10.07l-.215-.338a.5.5 0 0 0-.154-.154l-.338-.215 7.494-7.494 1.178-.471-.47 1.178Z" />
        </svg>
        New Transaction
      </b-button>
    </div>
    <OverviewTab v-if="currentTab == 0" />
    <TransactionTab v-if="currentTab == 1" />
    <PeopleTab v-if="currentTab == 2" />
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

    button {
      display: flex;
      width: fit-content;
      align-items: center;
      white-space: nowrap;
      svg {
        margin-right: 0.6rem;
      }
    }

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
  }
}
</style>
