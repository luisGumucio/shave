<template>
  <div class="clearfix btn-group col-md-2 offset-md-5">
    <button
      type="button"
      class="btn btn-sm btn-outline-secondary"
      v-if="pagiItem.page != 1"
      @click="page--"
    ><<</button>
    <button
      type="button"
      class="btn btn-sm btn-outline-secondary"
      v-for="pageNumber in pages.slice(page-1, page+5)"
      @click="page = pageNumber"
    >{{pageNumber}}</button>
    <button
      type="button"
      @click="page++"
      v-if="page < pages.length"
      class="btn btn-sm btn-outline-secondary"
    >>></button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pagiItem: {
        page: 1,
        perPage: 10,
        pages: [],
        totalPages: 0
      }
    };
  },
  methods: {
    paginate() {
      //  this.$emit("")
      alert("hola");
    }
  },
  watch: {
    page: function(value) {
      this.paginate();
    }
  }
};
</script>

<style scoped>
</style>