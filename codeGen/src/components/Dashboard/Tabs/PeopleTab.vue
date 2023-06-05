<script setup>
import UserWidget from '../UserWidget.vue';
</script>

<template>
  <div class="peoplePanel">
    <b-input placeholder="enter username" v-model="search" @input="searchIban"/>

    <div class="card users">
      <UserWidget v-for="(result, index) in searchResults" :username="result.firstName + ' ' + result.lastName" :index="index+1" :iban="result.iban" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'PeopleTab',
  data() {
    return {
      search: "",
      searchResults: null,
    };
  },
  mounted() {
    this.searchIban();
  },
  methods: {
    async searchIban() {
      console.log("searching");
      try {
        let request = {
          firstName: this.search.split(' ')[0] ? this.search.split(' ')[0] : "",
          lastName: this.search.split(' ')[1] ? this.search.split(' ')[1] : ""
        }
        let res = await this.$store.dispatch("getIbanByName", request);
        this.searchResults = res;
      }
      catch (e) {
        console.log(e)
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.card {
  padding: 35px;
  background: var(--white);
  box-shadow: 0 0 30px #1414140a;
  border-radius: 10px;
}

.peoplePanel {
  width: 90vw;
  height: 100%;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;
  justify-content: center;

  input {
    width: 200px;
  }

  .users {
    width: 100%;
    // max-height: 610px;
    height: 65vh;
    overflow: auto;
    white-space: nowrap;


  }

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
</style>
