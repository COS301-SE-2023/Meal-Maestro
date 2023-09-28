import { TestBed } from '@angular/core/testing';

import { LoginService } from './login.service';

describe('LoginService', () => {
  let service: LoginService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set and get home refreshed', () => {
    service.setHomeRefreshed(true);
    expect(service.isHomeRefreshed()).toBeTrue();

    service.setHomeRefreshed(false);
    expect(service.isHomeRefreshed()).toBeFalse();
  });

  it('should set and get pantry refreshed', () => {
    service.setPantryRefreshed(true);
    expect(service.isPantryRefreshed()).toBeTrue();

    service.setPantryRefreshed(false);
    expect(service.isPantryRefreshed()).toBeFalse();
  });

  it('should set and get recipe book refreshed', () => {
    service.setRecipeBookRefreshed(true);
    expect(service.isRecipeBookRefreshed()).toBeTrue();

    service.setRecipeBookRefreshed(false);
    expect(service.isRecipeBookRefreshed()).toBeFalse();
  });

  it('should set and get settings refreshed', () => {
    service.setSettingsRefreshed(true);
    expect(service.isSettingsRefreshed()).toBeTrue();

    service.setSettingsRefreshed(false);
    expect(service.isSettingsRefreshed()).toBeFalse();
  });
});
