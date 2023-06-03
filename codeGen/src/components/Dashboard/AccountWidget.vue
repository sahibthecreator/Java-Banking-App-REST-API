<script setup>
import AccountIcon from './AccountIcon.vue';
</script>

<template>
  <div class="account">
    <div class="d-flex">
      <AccountIcon :accountName="name"></AccountIcon>
      <div class="title">
        <p class="my-auto ml-3 font-weight-bolder">{{type}} Account</p>
        <p class="my-auto ml-3 iban">{{ iban }}</p>
      </div>
    </div>

    <p class="balance">€ {{ formatPrice(balance) }}</p>
  </div>
</template>

<script>
export default {
  name: 'AccountWidget',
  props: {
    type: String,
    name: String,
    balance: Number,
    iban: String
  },
  methods: {
    formatPrice(value) {
      let val = (value / 1).toFixed(2).replace('.', ',');
      return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    },
  },
};

var formatter = new Intl.NumberFormat('de-DE', {
  style: 'currency',
  currency: 'EUR',
});
</script>

<style lang="scss" scoped>
.account {
  background-color: white;
  padding: 10px 15px;
  border-radius: 15px;
  cursor: pointer;

  .balance {
    font-size: 2vw;
    font-weight: 500;
    text-align: left;
  }

  .iban {
    color: var(--gray-light);
  }
}
</style>
