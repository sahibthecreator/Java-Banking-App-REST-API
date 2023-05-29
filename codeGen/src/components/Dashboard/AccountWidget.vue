<script setup>
import AccountIcon from './AccountIcon.vue';
</script>

<template>
    <div class="account">
        <div class="d-flex">
            <AccountIcon :accountName="name"></AccountIcon>
            <p class="my-auto ml-3 font-weight-bolder">Current Account</p>
        </div>

        <p class="balance">€ {{ balance }}</p>
    </div>
</template>

<script>

export default {
    name: 'AccountWidget',
    props: {
        name: String,
        balance: Number
    },
}
</script>

<style lang="scss" scoped>
.account {
    background-color: white;
    padding: 10px 15px;
    border-radius: 15px;

    .balance {
        font-size: 2vw;
        font-weight: 500;
        text-align: center;
    }
}

</style>