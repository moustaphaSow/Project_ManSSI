import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient.model';
import {JwtService} from "./jwt.service";

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private apiUrl = 'http://localhost:8080/api/diag';

  constructor(private http: HttpClient, private jwtService: JwtService) { }

  private createAuthorizationHeader(): HttpHeaders {
    const jwtToken = this.jwtService.getToken();
    if (jwtToken) {
      return new HttpHeaders().set('Authorization', `Bearer ${jwtToken}`);
    } else {
      throw new Error('JWT token not found in local storage');
    }
  }

  getPatients(): Observable<Patient[]> {
    const headers = this.createAuthorizationHeader();
    return this.http.get<Patient[]>(this.apiUrl, { headers });
  }

  addPatient(patient: Patient): Observable<Patient> {
    const headers = this.createAuthorizationHeader();
    return this.http.post<Patient>(this.apiUrl, patient, { headers });
  }

  updatePatient(patient: Patient): Observable<Patient> {
    const headers = this.createAuthorizationHeader();
    return this.http.put<Patient>(`${this.apiUrl}/${patient.id}`, patient, { headers });
  }

  deletePatient(patientId: number): Observable<void> {
    const headers = this.createAuthorizationHeader();
    return this.http.delete<void>(`${this.apiUrl}/${patientId}`, { headers });
  }
}
