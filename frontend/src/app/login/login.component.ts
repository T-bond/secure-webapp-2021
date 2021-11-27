import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthControllerService } from '../api/services';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  
  form: FormGroup;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private authApi: AuthControllerService
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  public onSubmit(): void {
    var that = this
    if (this.form.valid) {
      this.authApi.signIn({
        body: {
          email: this.form.get("email").value,
          password: this.form.get("password").value
        }
      }).subscribe({
        complete() { that.router.navigate(["/store"]); },
        error() { alert("Invalid login credentials.") }
      });
    }
  }

  public signup(): void {
    this.router.navigate(["/signup"])
  }
}
