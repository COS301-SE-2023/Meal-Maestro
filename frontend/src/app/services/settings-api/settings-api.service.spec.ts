//write unit test for the settings-api.service.ts
// Path: frontend/src/app/services/settings-api/settings-api.service.spec.ts  
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SettingsApiService } from './settings-api.service';
import { UserPreferencesI } from '../../models/userpreference.model';

describe('SettingsApiService', () => {
  let service: SettingsApiService;
  let httpMock: HttpTestingController;
  let settings: UserPreferencesI;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SettingsApiService]
    });

    service = TestBed.inject(SettingsApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get settings', () => {
    settings = {
      goal: 'lose',
      shoppingInterval: 'weekly',
      foodPreferences: ['vegan'],
      calorieAmount: 2000,
      budgetRange: 'low',
      macroRatio: {protein: 0.3, carbs: 0.4, fat: 0.3},
      allergies: ['dairy'],
      cookingTime: '30',
      userHeight: 180,
      userWeight: 80,
      userBMI: 24.7,

      bmiset : true,
      cookingTimeSet : true,
      allergiesSet : true,
      macroSet : true,
      budgetSet : true,
      calorieSet : true,
      foodPreferenceSet : true,
      shoppingIntervalSet : true,

    };

    service.getSettings().subscribe((res) => {
      expect(res.status).toBe(200);
      expect(res.body).toEqual(settings);
    });

    const req = httpMock.expectOne(`${service.url}/getSettings`);
    expect(req.request.method).toBe('POST');
    req.flush(settings);
  });


  it('should update settings', () => {
    settings = {
      goal: 'lose',
      shoppingInterval: 'weekly',
      foodPreferences: ['vegan'],
      calorieAmount: 2000,
      budgetRange: 'low',

      macroRatio: {protein: 0.3, carbs: 0.4, fat: 0.3},
      allergies: ['dairy'],
      cookingTime: '30',
      userHeight: 180,
      userWeight: 80,
      userBMI: 24.7,

      bmiset : true,
      cookingTimeSet : true,
      allergiesSet : true,
      macroSet : true,
      budgetSet : true,
      calorieSet : true,
      foodPreferenceSet : true,
      shoppingIntervalSet : true,
    };


    service.updateSettings(settings).subscribe((res) => {
      expect(res.status).toBe(200);
    });

    const req = httpMock.expectOne(`${service.url}/updateSettings`);
    expect(req.request.method).toBe('POST');
    req.flush(settings);
  });
});



