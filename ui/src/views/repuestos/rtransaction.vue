<template>
  <section class="dashboard">
    <h4 class="card-title">Detalle del articulo: {{ $route.params.id }}</h4>
    <div class="row">
      <transaction-filter @add:filterDate="addFilter" />
      <transaction-detail :item="item" :increment="increment" />
    </div>
    <div class="row">
      <div class="col-12 grid-margin">
        <div class="table-responsive">
          <transaction-table :items="items" @detail:item="detail" />
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import TransactionTable from "./detail/transactionTable.vue";
import TransactionFilter from "./detail/TransactionFilter.vue";
import TransactionDetail from "./detail/TransactionDetail.vue";

export default {
  components: {
    TransactionTable,
    TransactionFilter,
    TransactionDetail
  },
  data() {
    return {
      items: [],
      item: {},
      increment: undefined,
      baseUrl: "http://localhost:4000/transaction"
    };
  },
  mounted() {
    this.getItems();
    this.getTotal();
  },
  methods: {
    async getItems() {
      try {
        const response = await fetch(
          this.baseUrl + "/" + this.$route.params.id
        );
        const data = await response.json();
        this.items = data["content"];
        this.item = this.items[0].item;
        console.log(this.item);
      } catch (error) {
        console.error(error);
      }
    },
    async addFilter(filterDate) {
      if (filterDate.lastDate != null) {
        filterDate.initDate.setDate(filterDate.initDate.getDate() - 1);
        filterDate.lastDate.setDate(filterDate.lastDate.getDate() + 1);
      }
      try {
        const response = await fetch(
          this.baseUrl + "/reportTransaction/" + this.$route.params.id,
          {
            method: "POST",
            body: JSON.stringify(filterDate),
            headers: { "Content-type": "application/json; charset=UTF-8" }
          }
        );
        const data = await response.json();
        this.items = data["content"];
      } catch (error) {
        console.error(error);
      }
    },

    async getTotal() {
      try {
        const response = await fetch(
          "http://localhost:4000/transaction/transactionTotal?id=" +
            this.$route.params.id
        );

        const data = await response.json();
        this.increment = data[0].total;
      } catch (error) {
        console.error(error);
      }
    },
    detail(detail) {
      alert(
        "Almacen: " +
          detail.information.ALMACEN +
          " Cuenta: " +
          detail.information.CUENTA
      );
    }
  }
};
</script>

<style scoped>
</style>