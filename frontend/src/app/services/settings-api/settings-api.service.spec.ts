import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SettingsApiService } from './settings-api.service';
import { HttpResponse } from '@angular/common/http';
import { UserPreferencesI } from '../../models/interfaces';

describe('SettingsApiService', () => {
  let service: SettingsApiService;
  let httpMock: HttpTestingController;

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

  it('should get user settings', () => {
    const mockToken = 'sample-token';
    const mockResponse: UserPreferencesI = {
        goal: 'Weight Loss',
        shopping_interval: 'Weekly',
        food_preferences: [],
        calorie_amount: 0,
        budget_range: '',
        macro_ratio: {
            protein: 0,
            carbs: 0,
            fats: 0
        },
        allergies: [],
        cooking_time: 0,
        user_height: 0,
        user_weight: 0,
        user_BMI: 0,
        BMI_set: false,
        cookingtime_set: false,
        allergies_set: false,
        macro_set: false,
        budget_set: false,
        calorie_set: false,
        foodpreferance_set: false,
        shoppinginterfval_set: false
    };

    service.getSettings(mockToken).subscribe((response: HttpResponse<UserPreferencesI>) => {
      expect(response.status).toBe(200);
      expect(response.body).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${service.url}/getSettings`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    req.flush(mockResponse);
  });

  it('should update user settings', () => {
    const mockToken = 'sample-token';
    const mockSettings: UserPreferencesI = {
        goal: 'Weight Loss',
        shopping_interval: 'Weekly',
        food_preferences: [],
        calorie_amount: 0,
        budget_range: '',
        macro_ratio: {
            protein: 0,
            carbs: 0,
            fats: 0
        },
        allergies: [],
        cooking_time: 0,
        user_height: 0,
        user_weight: 0,
        user_BMI: 0,
        BMI_set: false,
        cookingtime_set: false,
        allergies_set: false,
        macro_set: false,
        budget_set: false,
        calorie_set: false,
        foodpreferance_set: false,
        shoppinginterfval_set: false
    };

    service.updateSettings(mockSettings, mockToken).subscribe((response: HttpResponse<void>) => {
      expect(response.status).toBe(200);
    });

    const req = httpMock.expectOne(`${service.url}/updateSettings`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    expect(req.request.body).toEqual(mockSettings);
    req.flush(null); // Assuming the backend returns an empty response for updateSettings
  });

  // Add more test cases for other API endpoints as needed
});
