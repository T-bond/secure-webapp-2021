import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MediaControllerService } from '../api/services';

@Component({
  selector: 'app-login',
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.scss']
})
export class StoreComponent implements OnInit {
  
  form: FormGroup;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private mediaApi: MediaControllerService
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      query: ['', Validators.compose([
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(255)
        ])
      ]
    });
  } 

  public onSubmit(): void {
    var that = this
    if (this.form.valid) {
      this.mediaApi.searchMediaByTitle({
          searchDto: {
            titleContains: this.form.get("query").value
          }
      }).subscribe( {
        complete() { alert("search has been performed (please remove me )!") },
        error() { alert("Invalid query.") }
      });
    }
  }

  public signup(): void {
    this.router.navigate(["/signup"])
  }
}
