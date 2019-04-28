import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';

import { UfvRoutingModule } from './ufv-routing.module';
import { UfvComponent } from './ufv.component';
import {
    UfvUploadComponent,
    UfvListComponent
} from './components';
import { UfvService } from './services/ufv.service';

@NgModule({
    imports: [CommonModule, NgxSpinnerModule,
         UfvRoutingModule, FormsModule, NgbModule],
    declarations: [UfvComponent, UfvUploadComponent, UfvListComponent],
    providers: [UfvService],
})
export class UfvModule {}