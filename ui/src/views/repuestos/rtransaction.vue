<template>
  <section class="dashboard">
    <h4 class="card-title">Detalle del articulo: {{ $route.params.id }}</h4>
    <div class="row">
      <transaction-filter @add:filterDate="addFilter" />
      <div class="col-xl-4 col-lg-4 col-md-3 col-sm-6 grid-margin stretch-card">
        <div class="card card-statistics">
          <div class="card-body">
            <h4 class="card-title">Articulo detalle</h4>
            <div class="template-demo">
              <table class="table mb-0">
                <thead>
                  <tr>
                    <th class="pl-0">Item</th>
                    <th class="text-right">Total</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="pl-0">Cantidad total</td>
                    <td class="pr-0 text-right">
                      <b-badge pill variant="primary">5</b-badge>
                    </td>
                  </tr>
                  <tr>
                    <td class="pl-0">Precio total</td>
                    <td class="pr-0 text-right">
                      <b-badge pill variant="primary">124,425.23</b-badge>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12 grid-margin">
        <div class="card">
          <div class="card-body">
            <div class="table-responsive">
              <transaction-table :items="items" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import TransactionTable from "./detail/transactionTable.vue";
import TransactionFilter from "./detail/TransactionFilter.vue";

export default {
  components: {
    TransactionTable,
    TransactionFilter
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
        const response = await fetch(
          this.baseUrl + "/" + this.$route.params.id
        );
        const data = await response.json();
        this.items = data["content"];
      } catch (error) {
        console.error(error);
      }
    },
    async addFilter(filterDate) {
      console.log(filterDate.initDate);
      console.log(filterDate.lastDate);

      try {
        const response = await fetch(
          this.baseUrl + "/reportTransaction?id=" + this.$route.params.id,
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
    }
  }
};
</script>

<style scoped>
</style>