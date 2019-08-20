<template>
  <section class="dashboard">
    <h4 class="card-title">Transacciones generales</h4>
    <div class="row">
      <!-- <transaction-filter @add:filterDate="addFilter" />
      <transaction-detail :item="item" :increment="increment" />-->
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
export default {
  components: {
    TransactionTable
  },
  data() {
    return {
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