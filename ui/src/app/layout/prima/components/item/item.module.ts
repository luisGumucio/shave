import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';

import { ItemRoutingModule } from './item-routing.module';
import { ItemComponent } from './item.component';
import {
    ItemInfoComponent,
    ItemUpdateComponent,
    ItemListComponent,
    ItemListDetailComponent
} from './components';
// import { UfvService } from './services/ufv.service';

@NgModule({
    imports: [CommonModule, NgxSpinnerModule,
        ItemRoutingModule, FormsModule, NgbModule],
    declarations: [ItemComponent, ItemInfoComponent, ItemUpdateComponent, ItemListComponent, ItemListDetailComponent],
    // providers: [UfvService],
})
export class ItemModule {}