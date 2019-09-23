<template>
  <div class="col-md-12 grid-margin stretch-card">
    <vue-instant-loading-spinner ref="Spinner"></vue-instant-loading-spinner>
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
          <b-button variant="success" @click="download()">
            <i class="mdi mdi-cloud-download"></i>Exportar
          </b-button>
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
import DownloadService from "../../services/downloadService";
import ItemUpdate from "../repuestos/detail/itemUpdate.vue";
import VueInstantLoadingSpinner from "vue-instant-loading-spinner/src/components/VueInstantLoadingSpinner.vue";
import ItemService from "../../services/itemService";

export default {
  components: {
    ItemTable,
    ItemUpdate,
    VueInstantLoadingSpinner,
    ItemService
  },
  data() {
    return {
      items: [],
      itemstemporal: [],
      totalsum: 0,
      totalPrice: 0,
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
      ItemService.getItems("PRIMA", this.currentPage)
        .then(response => {
          this.items = response.data["content"];
          this.itemstemporal = response.data["content"];
          this.perPage = response.data.size;
          this.rows = response.data.totalPages;
          this.totalsum = response.data.totalElements;
        })
        .catch(error => {
          console.log(error);
        });
    },
    async getTotal() {
      ItemService.getTotal("PRIMA").then(response => {
        this.totalPrice = response.data[0].total;
      });
    },
    async getItemById(value) {
      ItemService.getItemById(value).then(response => {
        if (response.data !== "") {
          this.items = [];
          this.items.push(response.data);
        } else {
          this.items = [];
        }
      }).catch(error => {
        console.log(error);
        this.items = [];
      });
    },
    async download() {
      this.$refs.Spinner.show();
      DownloadService.downloadfile("PRIMA", "item", this.$refs.Spinner);
    },
    async update(initDate) {
      DownloadService.updateItem(initDate, "PRIMA");
    },
    sleep(milliseconds) {
      var start = new Date().getTime();
      for (var i = 0; i < 1e7; i++) {
        if (new Date().getTime() - start > milliseconds) {
          break;
        }
      }
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
</style>