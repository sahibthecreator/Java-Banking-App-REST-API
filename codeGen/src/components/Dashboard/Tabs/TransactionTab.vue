<script setup>
import TransactionTabWidget from '../TransactionTabWidget.vue';
</script>

<template>
  <div class="transactionPanel">
    <div class="controls">
      <b-input type="text" v-model="input" placeholder="Search Transactions" />
      <div class="buttons">
        <b-button variant="black">Last week</b-button>
        <b-button variant="black">Last Month</b-button>
        <b-button variant="black">Last Year</b-button>
      </div>
    </div>

    <div class="card transactions">
      <TransactionTabWidget
        v-for="(transaction, index) in transactions"
        :key="index"
        :transaction="transaction"
        :currentAccount="currentAccount"
        :accounts="accounts"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'TransactionTab',
  data() {
    return {
      input: null,
      transactions: null,
    };
  },
  mounted() {
    this.getTransactions();
  },
  methods: {
    async getTransactions() {
      let transactions = await this.$store.dispatch(
        'getTransactionsByUserId',
        this.$store.getters.getUserId
      );
      this.transactions = transactions;
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
.transactionPanel {
  width: 90vw;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;

  .controls {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    padding: 25px 0px;

    .buttons {
      display: flex;
      flex-direction: row;
      gap: 20px;
      button {
        width: fit-content;
        padding-inline: 20px;
      }
    }
  }
}
</style>
