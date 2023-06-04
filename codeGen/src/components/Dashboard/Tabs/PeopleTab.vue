<template>
  <div class="peoplePanel">
    <b-input placeholder="hello"/>
  </div>
</template>

<script>

export default {
  name: "PeopleTab"
}

</script>

<style lang="scss" scoped>

* {
  outline: 1px red solid;
}

.peoplePanel {
  width: 90vw;
  height: 100%;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;
  align-items: center;
  justify-content: center;

  input{ 
    width: 200px;
  }
}
  </style>