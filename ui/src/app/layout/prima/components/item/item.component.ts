import { Component, OnInit } from '@angular/core';
import { ItemService } from './services/item.service';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent implements OnInit {

  show: boolean = false;
  constructor(private itemService: ItemService) { }

  ngOnInit() {
    this.itemService.receiveUfv.subscribe(() => {
      this.show = true;
    });
  }

}
