<template>
    <div class="transaction">
        <div class="header">
            <h5>- €{{ amount }}</h5>
            <p class="dateTime">09:22</p>
        </div>
        <div class="details">
            <p><b>To</b> Sam Jhonson</p>
            <p>{{ toIban }}</p>
        </div>
        <b-button variant="black">See Details</b-button>
    </div>
</template>

<script>
export default {
    name: 'TransactionWidget',
    props: {
        toIban: String,
        amount: Number
    },
}
</script>

<style lang="scss" scoped>
.transaction {
    background: white;
    position: relative;
    width: 230px;
    border-radius: 25px;
    box-shadow: 0 0 30px #14141417;
    display: flex;
    flex-direction: column;
    padding: 20px;
}

.transaction .details p {
    margin-bottom: 0px;
}

.transaction .details p:nth-child(2) {
    font-size: small;
}

.transaction .btn {
    padding: 5px 5px !important;
    margin-top: 10%;
}

.transaction .header {
    display: flex;
}

.transaction .dateTime {
    font-size: smaller;
    font-weight: 500;
    margin-left: auto;
}
</style>