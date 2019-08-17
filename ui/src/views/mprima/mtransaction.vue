<template>
  <div class="col-md-14 grid-margin stretch-card">
    <div class="card">
      <div class="card-body">
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="col">Tipo</th>
              <th scope="col">Fecha</th>
              <th scope="col">Ingreso</th>
              <th scope="col">Egreso</th>
              <th scope="col">Cantidad</th>
              <th scope="col">Precio Neto</th>
              <th scope="col">Precio Actual</th>
              <th scope="col">Ufv</th>
              <th scope="col">Ingreso total</th>
              <th scope="col">Egreso total</th>
              <th scope="col">Total</th>
              <th scope="col">Total actualizado</th>
              <th scope="col">Incremento</th>
              <th scope="col">Accion</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">Act</th>
              <td>01/04/2018</td>
              <td>0.00</td>
              <td>0.00</td>
              <td>14,484.00</td>
              <td>0.00</td>
              <td>3.53</td>
              <td>2.23758</td>
              <td>0.00</td>
              <td>0.00</td>
              <td>50,465.79</td>
              <td>51,174.86</td>
              <td>14.64</td>
              <td>
                <p data-placement="top" data-toggle="tooltip" title="Informacion">
                  <router-link class="btn btn-primary btn-xs" to="/rtransaction/">
                    <span class="mdi mdi-file-chart"></span>
                  </router-link>
                </p>
              </td>
            </tr>
                        <tr>
              <th scope="row">Ing</th>
              <td>01/04/2018</td>
              <td>12</td>
              <td>0.00</td>
              <td>15,484.00</td>
              <td>4.00</td>
              <td>3.53</td>
              <td>2.23758</td>
              <td>20,465.79</td>
              <td>0.00</td>
              <td>60,465.79</td>
              <td>61,174.86</td>
              <td>1.64</td>
              <td>
                <p data-placement="top" data-toggle="tooltip" title="Informacion">
                  <router-link class="btn btn-primary btn-xs" to="/rtransaction/">
                    <span class="mdi mdi-file-chart"></span>
                  </router-link>
                </p>
              </td>
            </tr>
                        <tr>
              <th scope="row">Act</th>
              <td>01/05/2018</td>
              <td>0.00</td>
              <td>0.00</td>
              <td>14,484.00</td>
              <td>0.00</td>
              <td>3.53</td>
              <td>2.23758</td>
              <td>0.00</td>
              <td>0.00</td>
              <td>50,465.79</td>
              <td>51,174.86</td>
              <td>14.64</td>
              <td>
                <p data-placement="top" data-toggle="tooltip" title="Informacion">
                  <router-link class="btn btn-primary btn-xs" to="/rtransaction/">
                    <span class="mdi mdi-file-chart"></span>
                  </router-link>
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

<style scoped lang="scss">
</style>