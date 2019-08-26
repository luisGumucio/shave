<template>
  <div class="clearfix btn-group col-md-2 offset-md-5">
    <button
      type="button"
      class="btn btn-sm btn-outline-secondary"
      v-if="pagiItem.page != 1"
      @click="pagiItem.page--"
    ><<</button>
    <button
      type="button"
      class="btn btn-sm btn-outline-secondary"
      v-for="pageNumber in pagiItem.pages.slice(pagiItem.page-1, pagiItem.page+5)"
      @click="pagiItem.page = pageNumber"
    >{{pageNumber}}</button>
    <button
      type="button"
      @click="pagiItem.page++"
      v-if="pagiItem.page < pagiItem.pages.length"
      class="btn btn-sm btn-outline-secondary"
    >>></button>
  </div>
</template>

<script>
export default {
  name: "pagination",
  props: {
    pagiItem: {
        page: Number,
        pages: Array,
        totalPages: Number
    }
  },
  methods: {
    paginate() {
      let page = this.pagiItem.page -1;
      console.log("pepe")
      this.$emit("paginate:pagiItem", page);
    }
  },
  watch: {
    'pagiItem.page': function(val) {
      this.paginate();
    }
  }
};
</script>

<style scoped>
</style>