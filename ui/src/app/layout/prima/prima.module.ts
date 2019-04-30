import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';

import { PrimaRoutingModule } from './prima-routing.module';
import { PrimaComponent } from './prima.component';
import { ItemComponent } from  './components/item/item.component';
import { AlmacenComponent } from  './components/almacen/almacen.component';
// import {
//     UfvUploadComponent,
//     UfvListComponent
// } from './components';
// import { UfvService } from './services/ufv.service';

@NgModule({
    imports: [CommonModule, NgxSpinnerModule,
        PrimaRoutingModule, FormsModule, NgbModule],
    declarations: [PrimaComponent, ItemComponent, AlmacenComponent],
    // providers: [UfvService],
})
export class PrimaModule {}