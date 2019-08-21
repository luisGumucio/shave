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
                v-model="filterDate.initDate"
                @focus="clearStatus"
                @keypress="clearStatus"
              />
              <p v-if="error && submitting" class="error-message">❗Por favor llene las fechas</p>
              <!-- <p v-if="success" class="success-message">✅ Employee successfully added</p> -->
              <button class="btn btn-success">
                <i class="mdi mdi-magnify"></i> Buscar
              </button>
            </form>
            <b-table responsive striped hover :items="items" :fields="fields"></b-table>
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
              />
              <b-button type="submit" variant="success" class="mr-2">Cargar</b-button>
              <b-button variant="light" v-on:click="clear()">Cancelar</b-button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import VueInstantLoadingSpinner from "vue-instant-loading-spinner/src/components/VueInstantLoadingSpinner.vue";

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
      filterDate: {
        initDate: "",
        lastDate: ""
      },
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
      items: []
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
      } catch (error) {
        console.error(error);
      }
    },
    async submitFile() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("year", this.year);
      this.$refs.Spinner.show();
      setTimeout(
        function() {
          this.$refs.Spinner.hide();
        }.bind(this),
        10000
      );
      const response = await fetch(this.baseUrl + "files/ufv", {
        method: "POST",
        body: formData
      });
      const data = await response.json();
      this.clear();
      this.$refs.Spinner.hide();
      this.getItems();
    },
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    async handleSubmit() {
      this.submitting = true;
      this.clearStatus();
      if (this.invalidInitDate) {
        this.error = true;
        return;
      }

      const response = await fetch(
        this.baseUrl + "ufvs/fecha/" + this.filterDate.initDate
      );
      const data = await response.json();
      this.items = [];
      this.items.push(data);

      this.error = false;
      this.success = true;
      this.submitting = false;
    },
    clearStatus() {
      this.success = false;
      this.error = false;
    }
  },
  computed: {
    invalidInitDate() {
      return this.filterDate.initDate === "";
    },
    invalidYear() {
      return this.year === "";
    }
  }
};
</script>

<style scoped>
</style>