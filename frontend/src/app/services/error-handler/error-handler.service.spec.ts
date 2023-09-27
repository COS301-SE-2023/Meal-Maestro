import { TestBed } from '@angular/core/testing';

import { ErrorHandlerService } from './error-handler.service';
import { ToastController } from '@ionic/angular';

describe('ErrorHandlerService', () => {
  let service: ErrorHandlerService;
  let toastControllerSpy: jasmine.SpyObj<ToastController>;

  beforeEach(() => {
    toastControllerSpy = jasmine.createSpyObj('ToastController', ['create']);

    TestBed.configureTestingModule({
      providers: [{ provide: ToastController, useValue: toastControllerSpy }],
    });
    service = TestBed.inject(ErrorHandlerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should present error toast', async () => {
    const message = 'Error message';
    const error = { status: 500 };
    const toast = jasmine.createSpyObj('Toast', ['present']);

    toastControllerSpy.create.and.returnValue(Promise.resolve(toast));

    await service.presentErrorToast(message, error);

    expect(toastControllerSpy.create).toHaveBeenCalledWith({
      message: message,
      duration: 2000,
      color: 'danger',
      position: 'top',
      icon: 'alert-circle-outline',
    });
    expect(toast.present).toHaveBeenCalled();
  });

  it('should present success toast', async () => {
    const message = 'Success message';
    const toast = jasmine.createSpyObj('Toast', ['present']);

    toastControllerSpy.create.and.returnValue(Promise.resolve(toast));

    await service.presentSuccessToast(message);

    expect(toastControllerSpy.create).toHaveBeenCalledWith({
      message: message,
      duration: 2000,
      color: 'success',
      position: 'top',
      icon: 'checkmark-circle-outline',
    });
    expect(toast.present).toHaveBeenCalled();
  });
});
