<script setup>

import AccountIcon from '../Dashboard/AccountIcon.vue'
</script >

<template>
  <div class="user">
    <span>{{ index }}</span>
    <AccountIcon :accountName="username" />
    <div class="left">
      <p>{{ username }}</p>
      <p>{{ iban }}</p>
    </div>
    <b-button variant="dark_primary" @click="relocate_to_transaction()">Send Money</b-button>
  </div>
</template>

<script>
export default {
  name: 'UserWidget',

  props: {
    index: Number,
    username: String,
    iban: String,
  },

  methods: {
    relocate_to_transaction() {
      //this.$router.push('dashboard/transaction');
      //this.$router.push({ name: 'transaction', params: { receiverIban: "this.iban" } });
      this.$router.push({ path: 'dashboard/transaction', query: { receiverIban: this.iban } });

    }
  }
};
</script>

<style lang="scss" scoped>
.user {
  position: relative;
  display: flex;
  flex-direction: row;
  gap: 50px;
  padding: 15px;
  border-bottom: 1px var(--gray-light) solid;
  align-items: center;
  color: var(--gray-black);

  span {
    color: var(--gray-light);
    font-weight: 500;
  }

  .left>p {
    margin-left: -25px;
    margin-bottom: 0px;
    font-weight: 500;
  }

  .left>p:nth-child(2) {
    color: var(--gray-light);
  }

  button {
    margin-left: auto;
  }
}
</style>
