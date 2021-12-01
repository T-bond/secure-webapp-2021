import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-store-item',
  templateUrl: './store-item.component.html',
  styleUrls: ['./store-item.component.scss']
})
export class StoreItemComponent implements OnInit {

  public id;
  public title;
  public description;
  public createdAt;
  public createdBy;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  viewMedia(): void {
    this.router.navigate(["/media/" + this.id])
  }

}
