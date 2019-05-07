import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../services/item.service';

@Component({
  selector: 'app-item-list-detail',
  templateUrl: './item-list-detail.component.html',
  styleUrls: ['./item-list-detail.component.scss']
})
export class ItemListDetailComponent implements OnInit {
  private page: number = 0;
  private Totalpages: Array<number>;
  private items: Array<any>;
  private pages: Array<number>;
  constructor(private itemService: ItemService) {
    console.log(this.itemService.getItemId());
   }

  ngOnInit() {
    this.itemService.getTransactionDetail(this.itemService.getItemId()).subscribe(data => {
      this.items = data['content'];
    });
  }

}
