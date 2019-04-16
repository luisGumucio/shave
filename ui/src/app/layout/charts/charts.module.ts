import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule }   from '@angular/forms';

import { ChartsRoutingModule } from './charts-routing.module';
import { ChartsComponent } from './charts.component';
import { PageHeaderModule } from '../../shared';
import { UfvService } from '../dashboard/services/ufv.service'
import { ItemService } from './services/item.service';
import { StatModule } from '../../shared';
import {
    ItemComponent
} from './components';
@NgModule({
    imports: [CommonModule, ChartsRoutingModule, PageHeaderModule, FormsModule, StatModule],
    declarations: [ChartsComponent, ItemComponent],
    providers: [UfvService, ItemService],
})
export class ChartsModule {}
