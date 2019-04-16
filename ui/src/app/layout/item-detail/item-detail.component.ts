import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
// import { Popup } from 'ng2-opd-popup';
import { ItemService } from '../charts/services/item.service';

@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.scss']
})
export class ItemDetailComponent implements OnInit {
  private model: any;

  closeResult: string;
  private items: Array<any>;
  constructor(private route: ActivatedRoute, private itemService: ItemService,
    private modalService: NgbModal) {

  }
  open(content, id) {
    var detail = this.items.filter(b => b.id == id)[0].transactionDetail;
    this.itemService.getByStore(detail.wareHouseId).subscribe(result => {
      this.model.wareHouseId = result.name;
      this.model.idtype = detail.idtype;
      this.model.description = detail.description;
      this.model.seccion = detail.seccion;
      this.model.nroAccount = detail.nroAccount;
      this.modalService.open(content).result.then((result) => {
        this.closeResult = `Closed with: ${result}`;
      }, (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      });
    },
      (error) => {
        console.log(error.error.message);
        this.model = {
          wareHouseId: "",
          idtype: 0,
          description: "",
          seccion: 0,
          nroAccount: 0
        }
        this.modalService.open(content).result.then((result) => {
          this.closeResult = `Closed with: ${result}`;
        }, (reason) => {
          this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        });
      }
    );
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }



  ngOnInit() {
    //alert(this.route.snapshot.paramMap.get("id"));
    this.getDetail();
    this.model = {
      wareHouseId: "",
      idtype: 0,
      description: "",
      seccion: 0,
      nroAccount: 0
    }
  }

  getDetail() {
    console.log("hoa");
    var id = this.route.snapshot.paramMap.get("id");
    this.itemService.getByItem(id).subscribe(result => {
      this.items = result;
    },
      (error) => {
        console.log(error.error.message);
      }
    );
  }

}
