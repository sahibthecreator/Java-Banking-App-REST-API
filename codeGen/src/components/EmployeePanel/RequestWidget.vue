<script setup>
import AccountIcon from '@/components/Dashboard/AccountIcon.vue';
</script>

<template>
  <div class="requestWidget">
    <AccountIcon :accountName="request.user.firstName" class="icon"/>
    <div class="d-flex user">
      <span class="userInfo"><b>{{ request.user.firstName }} {{ request.user.lastname }}</b></span>
      <span class="userInfo">Requested on: {{ request.requestDate }}</span>
    </div>
    <div class="requestControls">
      <button class="button btn-black" @click="updateUser()">View</button>
      <button class="button btn-danger" @click="deleteUser()">Ignore</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RequestWidget',
  props: {
    request: Object,
  },
  data() {
    return {
      requests: null,
      request: {
        user: {
          firstName: 'Yvan',
          lastname: 'Roes',
        },
        requestDate: '11-03-1923'
      },
    };
  },
  mounted() {},
  methods: {},
};
</script>

<style lang="scss" scoped>
.requestWidget {
  height: fit-content;
  width: 95%;
  background-color: var(--white);
  padding: 15px 15px;
  border-radius: 15px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  box-shadow: 0 0 30px #14141417;

  .icon {
    width: 57px;
    height: 57px;
    aspect-ratio: 1;
  }

  .user {
    width: fit-content;
    display: flex;
    flex-direction: column;
    gap: 5px;

    .userInfo {
      color: var(--gray-black);
    }

    .userInfo:nth-child(2){
      color: var(--gray-light);
    }
  }

  .requestControls{

    margin-left: auto;
    display: flex;
    flex-direction: column;
    gap: 10px;

    button {
    height: fit-content;
    padding-block: 5px;
    width: 130px;
    border-radius: 7px;
    background-color: var(--gray-black);
    border: none;
    color: white;
    cursor: pointer;
    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.103);
    position: relative;
    overflow: hidden;
    transition-duration: 0.3s;

    &:active {
      transform: translate(5px, 5px);
      transition-duration: 0.3s;
    }
  }
  }
}
</style>
