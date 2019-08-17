// Dashboard Components
import dashboard from './views/dashboard'

// Widgets
import widgets from './views/widgets'

// UI Components
import alerts from './views/ui-components/alerts'
import badges from './views/ui-components/badges'
import breadcrumbs from './views/ui-components/breadcrumbs'
import buttons from './views/ui-components/buttons'
import carousel from './views/ui-components/carousel'
import dropdowns from './views/ui-components/dropdowns'
import icons from './views/ui-components/icons'
import modals from './views/ui-components/modals'
import paginations from './views/ui-components/paginations'
import progress from './views/ui-components/progress'
import tables from './views/ui-components/tables'
import typography from './views/ui-components/typography'
import tabs from './views/ui-components/tabs'
import tooltips from './views/ui-components/tooltips'
import upload from './views/upload'
import repuestos from './views/repuestos'
import ritem from './views/ritem'
import rtransaction from './views/repuestos/rtransaction'
import ralmacen from './views/repuestos/ralmacen'
//producto terminado
import producto from './views/producto/productoD'
import pitem from './views/producto/pitem'
import palmacen from './views/producto/palmacen'
import ptienda from './views/producto/ptienda'
//materia prima
import prima from './views/mprima/prima'
import mtransaction from './views/mprima/mtransaction'
import mitem from './views/mprima/mitem'

// Form Components
import forms from './views/forms/forms'

// Sample Pages
import error404 from './views/sample-pages/error-404'
import error500 from './views/sample-pages/error-500'
import login from './views/sample-pages/login'
import register from './views/sample-pages/register'
const routes = [{
  path: '/',
  name: 'dashboard',
  component: dashboard
},
{
  path: '/widgets',
  name: 'widgets',
  component: widgets
},
{
  path: '/404',
  name: 'error-404',
  component: error404
},
{
  path: '/500',
  name: 'error-500',
  component: error500
},
{
  path: '/login',
  name: 'login',
  component: login
},
{
  path: '/register',
  name: 'register',
  component: register
},
{
  path: '/alerts',
  name: 'alerts',
  component: alerts
},
{
  path: '/badges',
  name: 'badges',
  component: badges
},
{
  path: '/breadcrumbs',
  name: 'breadcrumbs',
  component: breadcrumbs
},
{
  path: '/buttons',
  name: 'buttons',
  component: buttons
},
{
  path: '/carousel',
  name: 'carousel',
  component: carousel
},
{
  path: '/dropdowns',
  name: 'dropdowns',
  component: dropdowns
},
{
  path: '/icons',
  name: 'icons',
  component: icons
},
{
  path: '/modals',
  name: 'modals',
  component: modals
},
{
  path: '/paginations',
  name: 'paginations',
  component: paginations
},
{
  path: '/progress',
  name: 'progress',
  component: progress
},
{
  path: '/tables',
  name: 'tables',
  component: tables
},
{
  path: '/typography',
  name: 'typography',
  component: typography
},
{
  path: '/tabs',
  name: 'tabs',
  component: tabs
},
{
  path: '/tooltips',
  name: 'tooltips',
  component: tooltips
},
{
  path: '/prima',
  name: 'prima',
  component: prima,
  children: [
    {
      path: '/mtransaction',
      name: 'mtransaction',
      component: mtransaction
    },
    {
      path: '/mitem',
      name: 'mitem',
      component: mitem
    },
    {
      path: '',
      name: 'mitem',
      component: mitem
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
      path: '/rtransaction',
      name: 'rtransaction',
      component: rtransaction
    },
    {
      path: '/ralmacen',
      name: 'ralmacen',
      component: ralmacen
    }
  ]
}];


export default routes;