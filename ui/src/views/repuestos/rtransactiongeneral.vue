<template>
  <section class="dashboard">
    <h4 class="card-title">Transacciones generales</h4>
    <div class="row">
      <transaction-filter @add:filterDate="addFilter" />
      <!-- <h1>hola</h1> -->
      <transaction-detail :item="item" :increment="increment" />
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="table-responsive">
          <transaction-table :items="items" />
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import TransactionTable from "./detail/transactionTable.vue";
import TransactionDetail from "./detail/TransactionDetail.vue";
import TransactionFilter from "./detail/TransactionFilter.vue";

export default {
  components: {
    TransactionTable,
    TransactionDetail,
    TransactionFilter
  },
  data() {
    return {
      item: {},
      items: [],
      baseUrl: "http://localhost:4000/transaction"
    };
  },
  mounted() {
    this.getItems();
  },
  methods: {
    async getItems() {
      try {
        const response = await fetch(this.baseUrl + "/identifier/REPUESTOS");
        const data = await response.json();
        this.items = data["content"];
        console.log(this.item);
      } catch (error) {
        console.error(error);
      }
    }
  }
};
</script>
<style scoped>
</style>