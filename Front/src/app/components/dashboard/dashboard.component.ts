import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../service/patient.service';
import { Patient } from '../../models/patient.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  patients: any[] = [];
  newPatient: Partial<Patient> = {};
  selectedPatient: Patient | null = null;
  editedPatient: Patient | null = null;
  addingPatient = false;

  constructor(private patientService: PatientService) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients() {
    this.patientService.getPatients().subscribe((response: any) => {
      console.log('Données reçues:', response);
      if (response && typeof response === 'object') {
        this.patients = Object.values(response.content); // Convert object to array
      } else {
        this.patients = [];
      }
    });
  }

  submitPatient(): void {
    if (this.newPatient.nom && this.newPatient.age && this.newPatient.observation) {
      this.patientService.addPatient(this.newPatient as Patient).subscribe(
        (patient) => {
          this.patients.push(patient);
          this.newPatient = {};
          this.addingPatient = false;
        },
        (error) => {
          console.error('Error adding patient:', error);
        }
      );
    }
  }

  showPatientInfo(patient: Patient): void {
    this.selectedPatient = patient;
    this.addingPatient = false;
  }

  editPatient(patient: Patient): void {
    this.editedPatient = { ...patient };
    this.selectedPatient = null;
    this.addingPatient = false;
  }

  submitEditedPatient(): void {
    if (this.editedPatient) {
      this.patientService.updatePatient(this.editedPatient).subscribe(
        (updatedPatient) => {
          const index = this.patients.findIndex(p => p.id === updatedPatient.id);
          if (index > -1) {
            this.patients[index] = updatedPatient;
          }
          this.editedPatient = null;
        },
        (error) => {
          console.error('Error updating patient:', error);
        }
      );
    }
  }

  deletePatient(patient: Patient): void {
    if (confirm(`Are you sure you want to delete patient ${patient.nom}?`)) {
      this.patientService.deletePatient(patient.id).subscribe(
        () => {
          this.patients = this.patients.filter(p => p.id !== patient.id);
        },
        (error) => {
          console.error('Error deleting patient:', error);
        }
      );
    }
  }

  cancelAction(): void {
    this.addingPatient = false;
    this.selectedPatient = null;
    this.editedPatient = null;
  }
}
