import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.scss']
})
export class ItemListComponent implements OnInit {

  private items: Array<any>;
  constructor() { }

  ngOnInit() {
    this.items = [ {
      'id': 1001,
      'price': 50.23,
      'quantity': 200.12,
      'lastUpdate': '2018-09-01'
    },
    {
      'id': 1001,
      'price': 50.23,
      'quantity': 200.12,
      'lastUpdate': '2018-09-01'
    },
    {
      'id': 1001,
      'price': 50.23,
      'quantity': 200.12,
      'lastUpdate': '2018-09-01'
    },
    {
      'id': 1001,
      'price': 50.23,
      'quantity': 200.12,
      'lastUpdate': '2018-09-01'
    },
    {
      'id': 1001,
      'price': 50.23,
      'quantity': 200.12,
      'lastUpdate': '2018-09-01'
    }];
  }

}
