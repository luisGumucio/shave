<template>
  <div class="col-md-14 grid-margin stretch-card">
    <div class="card">
      <div class="card-body">
        <h4 class="card-title">Detalle de la tienda 00680</h4>
                  <div class="form-group pull-right">
            <input type="text" class="search form-control" placeholder="Buscar Item" />
            <b-button variant="success">
              <i class="mdi mdi-cloud-download"></i>Exportar
            </b-button>
          </div>
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Cantidad</th>
              <th scope="col">Precio</th>
              <th scope="col">Total</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">94100230</th>
              <td>20</td>
              <td>40</td>
              <td>80</td>
            </tr>
            <tr>
              <th scope="row">94100231</th>
              <td>40</td>
              <td>40</td>
              <td>160</td>
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
</template>
<script>
export default {};
</script>

<style scoped>
</style>