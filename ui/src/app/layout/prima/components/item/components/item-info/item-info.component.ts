import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../services/item.service';
@Component({
  selector: 'app-item-info',
  templateUrl: './item-info.component.html',
  styleUrls: ['./item-info.component.scss']
})
export class ItemInfoComponent implements OnInit {

  private itemInfo = {
    'totalItem': 0,
    'total': 0,
    'totalQuantity': 0
  };

  constructor(private itemService: ItemService) { }

  ngOnInit() {
    this.getItemInfo();
  }

  getItemInfo() {
    this.itemService.getItemInformation().subscribe(data => {
      this.itemInfo = data;
    });
  }

}
