<script setup>
import Navigation from '@/components/Navigation.vue';
import AccountWidgetV2 from './AccountWidgetV2.vue';
import CurrentAccountPanelV2 from './CurrentAccountPanelV2.vue';
import TransactionWidgetV2 from './TransactionWidgetV2.vue';
import Overview from './Overview.vue';
</script>

<template>
  <div id="header">
    <navigation />
  </div>

  <div class="dashboardPage">
  <Overview />
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
