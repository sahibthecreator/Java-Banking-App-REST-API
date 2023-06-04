<template>
    <div class="accountIcon">
        <p class="m-auto">{{ accountName[0] }}</p>
    </div>
</template>

<script>
export default {
    name: 'AccountIcon',
    props: {
        accountName: String,
    }
}
</script>

<style lang="scss" scoped>
.accountIcon {
    background: url('@/assets/AccountIconBackground.webp');
    width: 57.59px;
    height: 57.59px;
    border-radius: 50%;
    display: flex;
}

.accountIcon p {
    font-weight: 500;
    font-size: 1.5rem;
    color: white;
}
</style>