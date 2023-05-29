<template>
    <div class="accountIcon">
        <p class="m-auto">{{ letter[0] }}</p>
    </div>
</template>

<script>
export default {
    name: 'AccountIcon',
    props: {
        letter: String,
    }
}
</script>

<style lang="scss" scoped>
.accountIcon {
    background: url('@/assets/AccountIconBackground.webp');
    width: 3vw;
    height: 3vw;
    border-radius: 50%;
    display: flex;
}

.accountIcon p {
    font-weight: 500;
    font-size: 1.5vw;
    color: white;
}
</style>