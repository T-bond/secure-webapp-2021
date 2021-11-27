import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthControllerService } from '../api/services';
import { UserCreateRequestDto } from '../api/models';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  form: FormGroup;

  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private authApi: AuthControllerService
  ) { }

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required]
    });
  }

  public onSubmit(): void {

    this.authApi.signUp({
      body: {
        username: this.form.get("username").value,
        password: this.form.get("password").value,
        email: this.form.get("email").value
      }
    });
    this.router.navigate(["/login"]);
  }

  public login(): void {
    this.router.navigate(["/login"]);
  }
}
