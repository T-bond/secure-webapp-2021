import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthControllerService } from '../api/services';
import { UserCreateRequestDto } from '../api/models';

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
    if (this.form.valid) {
      try {
        this.authApi.signIn({
          body: {
            email: this.form.get("email").value,
            password: this.form.get("password").value
          }
        }).subscribe();
        this.router.navigate(["/store"]);
      } catch {

      }
    }
  }

  public signup(): void {
    this.router.navigate(["/signup"])
  }
}
