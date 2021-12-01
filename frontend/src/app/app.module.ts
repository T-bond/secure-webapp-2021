import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StoreComponent } from './store/store.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SignupComponent } from './signup/signup.component';
import { AppRoutingModule } from './app-routing.module';
import { RouterModule, Routes } from '@angular/router'
import { AuthControllerService, MediaControllerService, UserControllerService } from './api/services';
import { HttpClientModule } from '@angular/common/http';
import { ProfileComponent } from './profile/profile.component';
import { UploadComponent } from './upload/upload.component';
import { MediaComponent } from './media/media.component';
import { StoreItemComponent } from './store-item/store-item.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'store', component: StoreComponent },
  { path: 'upload', component: UploadComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'media/:id', component: MediaComponent }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    StoreComponent,
    SignupComponent,
    ProfileComponent,
    UploadComponent,
    MediaComponent,
    StoreItemComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    RouterModule.forRoot(routes)
    ],
  providers: [
    AuthControllerService,
    MediaControllerService,
    UserControllerService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
