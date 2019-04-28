import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UfvComponent } from './ufv.component';

const routes: Routes = [
    {
        path: '', component: UfvComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UfvRoutingModule {
}
