<script setup>
import AccountIcon from './AccountIcon.vue';
</script>

<template>
  <div class="account">
    <div class="d-flex info">
      <AccountIcon :accountName="name"></AccountIcon>
      <div class="title">
        <p class="my-auto ml-3">{{ type }} Account</p>
        <p class="my-auto ml-3 iban">{{ iban }}</p>
      </div>
      <p class="balance">€ {{ formatPrice(balance) }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AccountWidgetV2',
  props: {
    type: String,
    name: String,
    balance: Number,
    iban: String,
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
  padding: 10px 15px;
  cursor: pointer;
  border-bottom: .5px var(--gray-light) solid;

  &:hover {
    box-shadow: 0 0 10px #1414141d;
  }

  .info {
    display: flex;
    flex-direction: row;
    gap: 20px;
    align-items: center;

    .title {
      p {
        font-weight: 500;
      }
    }

    .iban {
      color: var(--gray-light);
    }

    .balance {
      margin-bottom: 0px;
      margin-left: auto;
      font-size: 1.5rem;
      font-weight: 00;
      text-align: left;
    }
  }
}
</style>
