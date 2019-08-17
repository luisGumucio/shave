<template>
      <div class="col-md-14 grid-margin stretch-card">
    <div class="card">
      <div class="card-body">
        <h4 class="card-title">Almacen</h4>
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Almacen</th>
              <th scope="col">Total Item</th>
              <th scope="col">Total Cantidad</th>
              <th scope="col">Precio Total</th>
              <th scope="col">Accion</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">110</th>
              <td>Cueros Fresco</td>
              <td>40</td>
              <td>80</td>
              <td>14,484.00</td>
              <td>
                  <p data-placement="top" data-toggle="tooltip" title="Detalle">
                    <router-link class="btn btn-primary btn-xs" to="/ptienda/"><span class="mdi mdi-file-chart"></span></router-link>
                  </p>
                </td>
            </tr>
            <tr>
              <th scope="row">00680</th>
              <td>FABRICA MANACO</td>
              <td>60</td>
              <td>120</td>
              <td>142,484.00</td>
                                            <td>
                  <p data-placement="top" data-toggle="tooltip" title="Transaction">
                    <router-link class="btn btn-primary btn-xs" to="/ptienda/"><span class="mdi mdi-file-chart"></span></router-link>
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
</template>
<script>
export default {
    
}
</script>
<style scoped>

</style>