<template>
  <section class="badges">
    <vue-instant-loading-spinner ref="Spinner"></vue-instant-loading-spinner>
    <div class="row">
      <div class="col-md-8 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Ufv</h4>
            <form @submit.prevent="handleSubmit">
              <label class="control-label" for="date">Buscar</label>
              <input
                class="form-control"
                id="date"
                name="date"
                placeholder="MM/DD/YYY"
                type="date"
                :class="{ 'has-error': submitting && invalidInitDate}"
                v-model="initDate"
                @focus="clearStatus"
                @keypress="clearStatus"
              />
              <p v-if="error && submitting" class="error-message">❗Por favor llene las fechas</p>
              <button class="btn btn-success spce">
                <i class="mdi mdi-magnify"></i> Buscar
              </button>
            </form>
            <br />
            <b-alert v-model="showDismissibleAlert" variant="danger" dismissible>Ufv no encontrado</b-alert>
            <b-table responsive striped hover :items="items" :fields="fields"></b-table>
            <div class="clearfix btn-group col-md-2 offset-md-5">
              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                v-if="page != 1"
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
          </div>
        </div>
      </div>
      <div class="col-md-4 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Subir Archivo</h4>
            <form class="forms-sample" @submit.prevent="submitFile">
              <b-form-group label="cargar archivo" label-for="input8">
                <b-form-file
                  v-model="file"
                  id="inpu8"
                  :state="Boolean(file)"
                  placeholder="buscar un archivo..."
                ></b-form-file>
              </b-form-group>
              <label class="control-label" for="date">Año</label>
              <input
                class="form-control"
                id="year"
                name="year"
                placeholder="2018"
                type="text"
                :class="{ 'has-error': submitting && invalidYear}"
                v-model="year"
                @keypress="clearStatus"
              />
              <p v-if="errorUpload && submitting" class="error-message">❗Por favor agrege el año</p>
              <b-button type="submit" variant="success" class="mr-2 spce">Cargar</b-button>
              <b-button variant="light" v-on:click="clearStatus()" class="spce">Cancelar</b-button>
            </form>
            <b-alert
              v-model="showDismissibleAlertUfv"
              variant="danger"
              dismissible
              class="spce"
            >Fallo al cargar..</b-alert>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import VueInstantLoadingSpinner from "vue-instant-loading-spinner/src/components/VueInstantLoadingSpinner.vue";
import { setTimeout } from "timers";

export default {
  name: "upload",
  components: {
    VueInstantLoadingSpinner
  },
  data() {
    return {
      baseUrl: "http://localhost:4000/",
      file: null,
      submitting: false,
      error: false,
      success: false,
      initDate: "",
      year: "",
      fields: [
        {
          key: "id",
          label: "ID"
        },
        {
          key: "value",
          label: "Valor"
        },
        {
          key: "creationDate",
          label: "Fecha Subida"
        }
      ],
      items: [],
      showDismissibleAlert: false,
      showDismissibleAlertUfv: false,
      errorUpload: false,
      page: 1,
      perPage: 10,
      pages: [],
      totalPages: 0
    };
  },
  mounted() {
    this.getItems();
  },
  methods: {
    async getItems() {
      try {
        const response = await fetch(this.baseUrl + "ufvs");
        const data = await response.json();
        this.items = data["content"];
        this.totalPages = data["totalPages"];
      } catch (error) {
        console.error(error);
      }
    },
    async getItemsPagination(page) {
      try {
        const response = await fetch(this.baseUrl + "ufvs?page=" + page);
        const data = await response.json();
        this.items = data["content"];
        this.totalPages = data["totalPages"];
      } catch (error) {
        console.error(error);
      }
    },
    async submitFile() {
      this.submitting = true;
      if (this.invalidYear) {
        this.errorUpload = true;
        return;
      }
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("year", this.year);
      this.$refs.Spinner.show();
      setTimeout(
        function() {
          this.$refs.Spinner.hide();
        }.bind(this),
        2000
      );
      this.axios
        .post(this.baseUrl + "files/ufv", formData)
        .then(response => {
          this.clearStatus();
          this.$refs.Spinner.hide();
          this.getItems();
        })
        .catch(error => {
          this.showDismissibleAlertUfv = true;
          this.$refs.Spinner.hide();
          setTimeout(() => {
            this.showDismissibleAlertUfv = false;
          }, 3000);
        });
    },
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    async handleSubmit() {
      this.submitting = true;
      if (this.invalidInitDate) {
        this.error = true;
        return;
      }
      this.axios
        .get(this.baseUrl + "ufvs/fecha/" + this.initDate)
        .then(response => {
          this.items = [];
          this.items.push(response.data);
          this.clearStatus();
        })
        .catch(error => {
          this.showDismissibleAlert = true;
          setTimeout(() => {
            this.showDismissibleAlert = false;
          }, 2000);
        });
    },
    clearStatus() {
      this.success = false;
      this.error = false;
      this.file = null;
      this.submitting = false;
      this.year = null;
      this.errorUpload = false;
    },
    setPages() {
      for (let index = 1; index <= this.totalPages; index++) {
        this.pages.push(index);
      }
    },
    paginate() {
      let page = this.page;
      let perPage = this.perPage;
      let from = page * perPage - perPage;
      let to = page * perPage;
      return this.getItemsPagination(page);
    }
  },
  computed: {
    invalidInitDate() {
      return this.initDate === "";
    },
    invalidYear() {
      return this.year === "";
    },
    displayedPosts() {
      return this.paginate(this.items);
    }
  },
  watch: {
    initDate: function(val) {
      if (val === "" && this.items.length <= 1) {
        this.getItems();
      }
    },
    items() {
      console.log("holas");
      this.setPages();
    },
    page: function(val) {
      this.paginate();
    }
  }
};
</script>

<style scoped>
.spce {
  margin-top: 10px;
}
</style>