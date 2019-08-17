<template>
      <div class="col-md-12 grid-margin stretch-card">
    <div class="col-md-8 grid-margin stretch-card">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Articulos</h4>
          <!-- <b-table responsive :items="items1"></b-table>
            <b-pagination :total-rows="20" v-model="currentPage" :per-page="5">
          </b-pagination>-->
          <div class="form-group pull-right">
            <input type="text" class="search form-control" placeholder="Buscar Item" />
            <b-button variant="success">
              <i class="mdi mdi-cloud-download"></i>Exportar
            </b-button>
          </div>
          <!-- <div class="form-group">
            <label class="control-label" for="date">Date</label>
            <input class="form-control" id="date" name="date" placeholder="MM/DD/YYY" type="date" />
          </div> -->
          <table class="table">
            <thead class="thead-dark">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Cantidad</th>
                <th scope="col">Precio</th>
                <th scope="col">Fecha Actualizacion</th>
                <th scope="col">Total</th>
                <th scope="col">Accion</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th scope="row">94100230</th>
                <td>5</td>
                <td>12</td>
                <td>10/20/2019</td>
                <td>22</td>
                <td>
                  <p data-placement="top" data-toggle="tooltip" title="Transaction">
                    <!-- <button
                      class="btn btn-primary btn-xs"
                      data-title="Transaction"
                      data-toggle="modal"
                      data-target="#edit"
                    >
                      <span class="mdi mdi-file-chart"></span>
                    </button> -->
                    <router-link class="btn btn-primary btn-xs" to="/rtransaction/"><span class="mdi mdi-file-chart"></span></router-link>
                  </p>
                </td>
              </tr>
              <tr>
                <th scope="row">1</th>
                <td>94100231</td>
                <td>12</td>
                <td>10/20/2019</td>
                <td>22</td>
                <td>
                  <p data-placement="top" data-toggle="tooltip" title="Transaction">
                    <button
                      class="btn btn-primary btn-xs"
                      data-title="Transaction"
                      data-toggle="modal"
                      data-target="#edit"
                    >
                      <span class="mdi mdi-file-chart"></span>
                    </button>
                  </p>
                </td>
              </tr>
              <tr>
                <th scope="row">1</th>
                <td>94100233</td>
                <td>12</td>
                <td>10/20/2019</td>
                <td>22</td>
                <td>
                  <p data-placement="top" data-toggle="tooltip" title="Transaction">
                    <button
                      class="btn btn-primary btn-xs"
                      data-title="Transaction"
                      data-toggle="modal"
                      data-target="#edit"
                    >
                      <span class="mdi mdi-file-chart"></span>
                    </button>
                  </p>
                </td>
              </tr>
            </tbody>
          </table>
          <nav aria-label="Page navigation example">
            <ul class="pagination">
              <li class="page-item">
                <a class="page-link" href="#" aria-label="Previous">
                  <span aria-hidden="true">&laquo;</span>
                </a>
              </li>
              <li class="page-item">
                <a class="page-link" href="#">1</a>
              </li>
              <li class="page-item">
                <a class="page-link" href="#">2</a>
              </li>
              <li class="page-item">
                <a class="page-link" href="#">3</a>
              </li>
              <li class="page-item">
                <a class="page-link" href="#" aria-label="Next">
                  <span aria-hidden="true">&raquo;</span>
                </a>
              </li>
            </ul>
          </nav>
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
</template>

<script>
export default {
    
}
</script>

<style scoped>

</style>