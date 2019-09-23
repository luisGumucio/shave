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
                type="text"
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
import DownloadService from "../../services/downloadService";
import ItemService from "../../services/itemService";
import VueInstantLoadingSpinner from "vue-instant-loading-spinner/src/components/VueInstantLoadingSpinner.vue";

export default {
  components: {
    ItemTable,
    ItemUpdate,
    DownloadService,
    ItemService,
    VueInstantLoadingSpinner
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
      ItemService.getItems("REPUESTOS", this.currentPage)
      .then(response => {
        this.items = response.data["content"];
        this.itemstemporal = response.data["content"];
        this.perPage = response.data.size;
        this.rows = response.data.totalPages;
        this.totalsum = response.data.totalElements;
      }).catch(error => {
        console.log(error);
      });
    },
    async getTotal() {
      ItemService.getTotal("REPUESTOS").then(response => {
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
    async update(initDate) {
      DownloadService.updateItem(initDate, "REPUESTOS");
    },
    download() {
      this.$refs.Spinner.show();
      DownloadService.downloadfile("REPUESTOS", "item", this.$refs.Spinner);
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
      console.log(value);
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

