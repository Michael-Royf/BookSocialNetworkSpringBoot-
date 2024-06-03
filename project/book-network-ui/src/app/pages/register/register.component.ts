import {Component} from '@angular/core';

import {NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {RegistrationRequest} from "../../services/models/registration-request";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmationPassword: ''
  }
  errorMsg: Array<String> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {
  }


  register() {
    this.errorMsg = [];
    this.authService.register({
      body: this.registerRequest
    })
      .subscribe({
        next: () => {
          this.router.navigate(['activate-account']);
        },
        error: (err) => {
          console.error(err); // Логирование ошибки для отладки
          if (err.error.message) {
            this.errorMsg.push(err.error.message);
          } else if (err.error.validationErrors) {
            this.errorMsg = err.error.validationErrors;
          } else {
            this.errorMsg.push('An unknown error occurred.');
          }
        }
      });
  }

  login() {
    this.router.navigate(['login']);
  }
}

