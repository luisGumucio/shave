<template>
  <section class="dashboard">
    <h4 class="card-title">Transacciones generales</h4>
    <div class="row">
      <transaction-filter @add:filterDate="addFilter" @button-clicked="download()" />

      <transaction-detail :reportTransaction="reportTransaction" />
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
import TransactionTable from "../repuestos/detail/transactionTable.vue";
import TransactionDetail from "../repuestos/detail/TransactionDetail.vue";
import TransactionFilter from "../repuestos/detail/TransactionFilter.vue";
import DownloadService from "../../services/downloadService";

export default {
  components: {
    TransactionTable,
    TransactionDetail,
    TransactionFilter,
    DownloadService
  },
  data() {
    return {
      reportTransaction: {},
      items: [],
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
        const response = await fetch(this.baseUrl + "/identifier/PRIMA");
        const data = await response.json();
        this.items = data["content"];
        console.log(this.item);
      } catch (error) {
        console.error(error);
      }
    },
    async getTotal() {
      try {
        const response = await fetch(
          this.baseUrl + "/reportTransactionTotal/PRIMA"
        );
        const data = await response.json();
        this.reportTransaction = data[0];
      } catch (error) {}
    },
    download() {
      DownloadService.downloadfile("PRIMA", "transaction");
    },
    async getTotalByDate(filterDate) {
      if (filterDate.lastDate == null) {
        try {
          const response = await fetch(this.baseUrl + "/reportDateTransaction/", {
            method: "POST",
            body: JSON.stringify(filterDate),
            headers: { "Content-type": "application/json; charset=UTF-8" }
          });
          const data = await response.json();
          this.reportTransaction = data[0];
        } catch (error) {}
      }
    },
    async addFilter(filterDate) {
      if (filterDate.lastDate != null) {
        var init = new Date(filterDate.initDate);
        var last = new Date(filterDate.lastDate);
        filterDate.initDate = new Date(
          init.getFullYear(),
          init.getMonth(),
          init.getDate() - 1
        ).toJSON();
        filterDate.lastDate = new Date(
          last.getFullYear(),
          last.getMonth(),
          last.getDate() + 1
        ).toJSON();
      }
      filterDate.identifier = "PRIMA";
      try {
        const response = await fetch(this.baseUrl + "/transactionDate/", {
          method: "POST",
          body: JSON.stringify(filterDate),
          headers: { "Content-type": "application/json; charset=UTF-8" }
        });
        const data = await response.json();
        this.items = data["content"];
        this.getTotalByDate(filterDate);
      } catch (error) {
        console.error(error);
      }
    }
  }
};
</script>
<style scoped>
</style>