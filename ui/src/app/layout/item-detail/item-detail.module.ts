import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ItemPageRoutingModule } from './item-detail-routing.module';
import { ItemDetailComponent } from './item-detail.component';

import { ItemService } from '../charts/services/item.service';

@NgModule({
    imports: [CommonModule, ItemPageRoutingModule, NgbModule],
    declarations: [ItemDetailComponent],
    providers: [ItemService]
})
export class ItemDetailModule {}
