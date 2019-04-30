import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PrimaComponent } from './prima.component';
import { ItemComponent } from  './components/item/item.component';
import { AlmacenComponent } from  './components/almacen/almacen.component';
const routes: Routes = [
    {
        path: '',
        component: PrimaComponent,
        children: [
            { path: '', component:  ItemComponent },
            { path: 'item', component:  ItemComponent },
            { path: 'almacen', component:  AlmacenComponent  },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PrimaRoutingModule {
}