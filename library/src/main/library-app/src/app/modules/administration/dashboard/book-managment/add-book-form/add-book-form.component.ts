import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AddBookForm } from 'src/app/modules/core/models/forms.model';
import { AdministrationService } from '../../../service/administration.service';
import { AddBookData } from 'src/app/modules/core/models/add-book-managment';

@Component({
  selector: 'app-add-book-form',
  templateUrl: './add-book-form.component.html',
  styleUrls: ['./add-book-form.component.scss']
})
export class AddBookFormComponent {
  addBookForm: FormGroup<AddBookForm> = new FormGroup({
    title: new FormControl('', {
      validators: [
        Validators.required,
        // Validators.minLength(8),
        // Validators.maxLength(50),
      ],
      nonNullable: true,
    }),
    numberOfPage: new FormControl('', {
      validators: [
        Validators.required,
        // Validators.minLength(8),
        // Validators.maxLength(75),
      ],
      nonNullable: true,
    }),
  });

  get controls() {
    return this.addBookForm.controls;
  }

  constructor(
    private administrationService: AdministrationService,
  ) {
    
  }

  onAddBook() {
    const formValue = this.addBookForm.getRawValue();
    
    const addBookData: AddBookData = {
      title: formValue.title,
      numberOfPage: +formValue.numberOfPage
    }

    this.administrationService.addBook(addBookData).subscribe({
      next: () => {

      },
      error: (err) => {
        
      }
    });
  }
}
