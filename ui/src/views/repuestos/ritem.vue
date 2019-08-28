<template>
  <div class="col-md-12 grid-margin stretch-card">
    <div class="col-md-8 grid-margin stretch-card">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Articulos</h4>
          <div class="col-lg-6">
            <div class="input-group">
              <input
                type="number"
                class="search form-control"
                placeholder="Buscar Item"
                v-model="search"
              />
            </div>
          </div>
          <br />
          <b-button variant="success spce" @click="download()">
            <i class="mdi mdi-cloud-download"></i>Exportar
          </b-button>
          <!-- <download-modal /> -->
          <br />
          <item-table :items="items" :current-page="currentPage" />
        </div>
      </div>
    </div>
    <div class="col-md-4 grid-margin stretch-card">
      <div class="card">
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
                    <b-badge pill variant="primary">{{ totalsum }}</b-badge>
                  </td>
                </tr>
                <tr>
                  <td class="pl-0">Precio total</td>
                  <td class="pr-0 text-right">
                    <b-badge pill variant="primary">{{ totalPrice | currency }}</b-badge>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <br />
        <item-update @add:initDate="update" />
      </div>
    </div>
  </div>
</template>
<script>
import ItemTable from "./detail/itemTable.vue";
import ItemUpdate from "./detail/itemUpdate.vue";
import DownloadModal from "../utils/modal.vue";
import DownloadService from "../../services/downloadService";

export default {
  components: {
    ItemTable,
    ItemUpdate,
    DownloadModal,
    DownloadService
  },
  data() {
    return {
      items: [],
      itemstemporal: [],
      totalsum: 0,
      totalPrice: 0,
      baseUrl: "http://localhost:4000/items",
      currentPage: 0,
      perPage: 0,
      rows: 0,
      search: ""
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
          this.baseUrl + "?page=" + this.currentPage + "&identifier=REPUESTOS"
        );
        const data = await response.json();
        this.items = data["content"];
        this.itemstemporal = data["content"];
        this.perPage = data.size;
        this.rows = data.totalPages;
        this.totalsum = data.totalElements;
      } catch (error) {
        console.error(error);
      }
    },
    async getTotal() {
      try {
        const response = await fetch(
          "http://localhost:4000/items/itemtotal?identifier=REPUESTOS"
        );
        const data = await response.json();
        this.totalPrice = data[0].total;
      } catch (error) {
        console.error(error);
      }
    },
    async getItemById(value) {
      try {
        const response = await fetch(this.baseUrl + "/" + value);
        const data = await response.json();
        if (data != null) {
          this.items = [];
          this.items.push(data);
        } else {
          this.items = [];
        }
      } catch (error) {
        console.error(error);
        this.items = [];
      }
    },
    async update(initDate) {
      DownloadService.updateItem(initDate, "REPUESTOS");
    },
    download() {
      DownloadService.downloadfile("REPUESTOS", "item");
    }
  },
  filters: {
    currency(amount) {
      const amt = Number(amount);
      return (
        (amt && amt.toLocaleString(undefined, { maximumFractionDigits: 3 })) ||
        "0"
      );
    }
  },
  watch: {
    search: function(value) {
      if (this.search.length == 0) {
        this.items = this.itemstemporal;
      } else if (this.search.length >= 4) {
        this.getItemById(value);
      } else {
        this.items = [];
      }
    }
  }
};
</script>

<style scoped>
.spce {
  margin-bottom: 5px;
}
</style>

