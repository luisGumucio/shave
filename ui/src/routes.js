// Dashboard Components
import dashboard from './views/dashboard'

// Widgets
import widgets from './views/widgets'

// UI Components
import upload from './views/upload'
import ufv from './views/ufv/ufv'
// repuestos
import repuestos from './views/repuestos/repuestos'
import ritem from './views/repuestos/ritem'
import rtransaction from './views/repuestos/rtransaction'
import ralmacen from './views/repuestos/ralmacen'
import rtransactiongeneral from './views/repuestos/rtransactiongeneral'
import rprocess from './views/repuestos/rprocess'

//producto terminado
import producto from './views/producto/productoD'
import pitem from './views/producto/pitem'
import palmacen from './views/producto/palmacen'
import ptienda from './views/producto/ptienda'
//materia prima
import prima from './views/mprima/prima'
import mtransaction from './views/mprima/mtransaction'
import mitem from './views/mprima/mitem'
import mAlmacen from './views/mprima/mAlmacen'

// Form Components
import forms from './views/forms/forms'

// Sample Pages
import error404 from './views/sample-pages/error-404'
import error500 from './views/sample-pages/error-500'
import login from './views/sample-pages/login'
import register from './views/sample-pages/register'
const routes = [{
  path: '/',
  name: 'ufv',
  component: ufv
},
{
  path: '/prima',
  name: 'prima',
  component: resolve => resolve(prima),
  children: [
    {
      path: '/mtransaction/:id',
      name: 'mtransaction',
      component: resolve => resolve(mtransaction) 
    },
    {
      path: '',
      name: 'mitem',
      component: resolve => resolve(mitem)
    },
    {
      path: '/mAlmacen',
      name: 'mAlmacen',
      component: resolve => resolve(mAlmacen)
    }]
},
{
  path: '/upload',
  name: 'upload',
  component: upload
},
{
  path: '/producto',
  name: 'producto',
  component: producto,
  children: [
    {
      path: '/pitem',
      name: 'pitem',
      component: pitem
    },
    {
      path: '',
      name: 'pitem',
      component: pitem
    },
    {
      path: '/palmacen',
      name: 'palmacen',
      component: palmacen
    },
    ,
    {
      path: '/ptienda',
      name: 'ptienda',
      component: ptienda
    }
  ]
},
{
  path: '/repuestos',
  name: 'repuetos',
  component: repuestos,
  children: [
    {
      path: '/ritem',
      name: 'ritem',
      component: ritem
    },
    {
      path: '',
      name: 'ritem',
      component: ritem
    },
    ,
    {
      path: '/rtransaction/:id',
      name: 'rtransaction',
      component: rtransaction
    },
    {
      path: '/rtransactiongeneral',
      name: 'rtransactiongeneral',
      component: rtransactiongeneral
    },
    {
      path: '/rprocess',
      name: 'rprocess',
      component: rprocess
    }
  ]
}];


export default routes;