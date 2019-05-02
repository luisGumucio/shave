import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PrimaComponent } from './prima.component';
import { AlmacenComponent } from './components/almacen/almacen.component';
const routes: Routes = [
    {
        path: '',
        component: PrimaComponent,
        children: [
            { path: '', redirectTo: 'item', pathMatch: 'prefix' },
            { path: 'item', loadChildren: './components/item/item.module#ItemModule' },
            { path: 'almacen', component:  AlmacenComponent  }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PrimaRoutingModule {
}