import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

interface CourseState {
  courseId: string;
  title: string;
  capacity: number;
  requiredPrerequisiteId: string;
  enrolledLearnerIds: string[];
  waitlistedLearnerIds: string[];
}

interface OperationResult {
  status: string;
  message: string;
  promotedLearnerId?: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  template: `
    <div style="max-width: 900px; margin: 0 auto; background: white; padding: 24px; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
      <h1 style="margin-top: 0; color: #0f172a;">Course Registration & Waitlist Management</h1>
      
      <div *ngIf="courseState" style="background: #f1f5f9; padding: 16px; border-radius: 8px; margin-bottom: 20px;">
        <h2 style="margin: 0 0 8px 0; color: #1e293b;">{{courseState.title}} ({{courseState.courseId}})</h2>
        <p style="margin: 4px 0;"><strong>Capacity:</strong> {{courseState.enrolledLearnerIds.length}} / {{courseState.capacity}}</p>
        <p style="margin: 4px 0;"><strong>Required Prerequisite:</strong> {{courseState.requiredPrerequisiteId}}</p>
      </div>

      <div style="display: flex; gap: 16px; margin-bottom: 24px;">
        <input [(ngModel)]="selectedLearnerId" placeholder="Enter Learner ID (e.g. L1, L2, L3, L4)" 
               style="flex: 1; padding: 10px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 14px;" />
        <button (click)="enroll()" style="background: #2563eb; color: white; border: none; padding: 10px 18px; border-radius: 6px; cursor: pointer; font-weight: 600;">Enroll / Join Waitlist</button>
        <button (click)="drop()" style="background: #dc2626; color: white; border: none; padding: 10px 18px; border-radius: 6px; cursor: pointer; font-weight: 600;">Drop</button>
      </div>

      <div *ngIf="feedbackMessage" [style.background]="isError ? '#fef2f2' : '#f0fdf4'" 
           [style.color]="isError ? '#991b1b' : '#166534'"
           style="padding: 12px; border-radius: 6px; margin-bottom: 20px; font-weight: 500;">
        {{feedbackMessage}}
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
        <div style="border: 1px solid #e2e8f0; border-radius: 8px; padding: 16px;">
          <h3 style="margin-top: 0; color: #166534;">Enrolled Learners ({{courseState?.enrolledLearnerIds?.length || 0}})</h3>
          <ul style="padding-left: 20px; margin-bottom: 0;">
            <li *ngFor="let learnerId of courseState?.enrolledLearnerIds" style="padding: 4px 0;">
              <strong>{{learnerId}}</strong>
            </li>
            <li *ngIf="!courseState?.enrolledLearnerIds?.length" style="color: #64748b; list-style-type: none;">No learners enrolled yet.</li>
          </ul>
        </div>

        <div style="border: 1px solid #e2e8f0; border-radius: 8px; padding: 16px;">
          <h3 style="margin-top: 0; color: #854d0e;">FIFO Waitlist ({{courseState?.waitlistedLearnerIds?.length || 0}})</h3>
          <ol style="padding-left: 20px; margin-bottom: 0;">
            <li *ngFor="let learnerId of courseState?.waitlistedLearnerIds" style="padding: 4px 0;">
              <strong>{{learnerId}}</strong>
            </li>
            <li *ngIf="!courseState?.waitlistedLearnerIds?.length" style="color: #64748b; list-style-type: none;">Waitlist is empty.</li>
          </ol>
        </div>
      </div>
    </div>
  `
})
export class AppComponent implements OnInit {
  courseState: CourseState | null = null;
  selectedLearnerId: string = 'L1';
  feedbackMessage: string = '';
  isError: boolean = false;

  private apiUrl = 'http://localhost:8080/api/course';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.refreshState();
  }

  refreshState() {
    this.http.get<CourseState>(`${this.apiUrl}/state`).subscribe({
      next: (state) => this.courseState = state,
      error: (err) => this.showFeedback('Failed to fetch course state from backend.', true)
    });
  }

  enroll() {
    if (!this.selectedLearnerId.trim()) return;
    this.http.post<OperationResult>(`${this.apiUrl}/enroll`, { learnerId: this.selectedLearnerId }).subscribe({
      next: (res) => {
        this.isError = res.status.includes('MISSING') || res.status.includes('INVALID');
        this.feedbackMessage = `[${res.status}] ${res.message}`;
        this.refreshState();
      },
      error: (err) => this.showFeedback('API Error on enroll request.', true)
    });
  }

  drop() {
    if (!this.selectedLearnerId.trim()) return;
    this.http.post<OperationResult>(`${this.apiUrl}/drop`, { learnerId: this.selectedLearnerId }).subscribe({
      next: (res) => {
        this.isError = res.status.includes('NOT_ENROLLED') || res.status.includes('INVALID');
        let msg = `[${res.status}] ${res.message}`;
        if (res.promotedLearnerId) {
          msg += ` (Promoted Learner: ${res.promotedLearnerId})`;
        }
        this.feedbackMessage = msg;
        this.refreshState();
      },
      error: (err) => this.showFeedback('API Error on drop request.', true)
    });
  }

  private showFeedback(msg: string, error: boolean) {
    this.feedbackMessage = msg;
    this.isError = error;
  }
}
