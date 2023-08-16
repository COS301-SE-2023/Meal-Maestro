import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
  waitForAsync,
} from '@angular/core/testing';
import {
  ActionSheetController,
  IonicModule,
  PickerController,
} from '@ionic/angular';

import { FoodListItemComponent } from './food-list-item.component';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { ShoppingListApiService } from '../../services/shopping-list-api/shopping-list-api.service';
import { FoodItemI } from '../../models/interfaces';
import { of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ElementRef } from '@angular/core';

describe('FoodListItemComponent', () => {
  let component: FoodListItemComponent;
  let fixture: ComponentFixture<FoodListItemComponent>;
  let mockPantryService: jasmine.SpyObj<PantryApiService>;
  let mockShoppingListService: jasmine.SpyObj<ShoppingListApiService>;
  let mockActionSheetController: jasmine.SpyObj<any>;
  let mockPickerController: jasmine.SpyObj<any>;
  let mockItem: FoodItemI;

  const mockElementRef = new ElementRef({
    nativeElement: document.createElement('div'),
  });

  beforeEach(waitForAsync(() => {
    mockPantryService = jasmine.createSpyObj('PantryApiService', [
      'updatePantryItem',
    ]);
    mockShoppingListService = jasmine.createSpyObj('ShoppingListApiService', [
      'updateShoppingListItem',
    ]);
    mockActionSheetController = jasmine.createSpyObj('ActionSheetController', [
      'create',
    ]);
    mockPickerController = jasmine.createSpyObj('PickerController', ['create']);
    mockItem = {
      name: 'test',
      quantity: 1,
      unit: 'pcs',
    };

    const emptyResponse = new HttpResponse<void>({ body: null, status: 200 });

    mockPantryService.updatePantryItem.and.returnValue(of(emptyResponse));
    mockShoppingListService.updateShoppingListItem.and.returnValue(
      of(emptyResponse)
    );
    mockActionSheetController.create.calls.reset();

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), FoodListItemComponent],
      providers: [
        { provide: PantryApiService, useValue: mockPantryService },
        { provide: ShoppingListApiService, useValue: mockShoppingListService },
        { provide: ActionSheetController, useValue: mockActionSheetController },
        { provide: PickerController, useValue: mockPickerController },
        { provide: ElementRef, useValue: mockElementRef },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FoodListItemComponent);
    component = fixture.componentInstance;
    component.item = mockItem;
    component.segment = 'pantry';
    component.isVisible = true;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should inject ActionSheetController', () => {
    expect(mockActionSheetController).toBeDefined();
  });

  // describe('openDeleteSheet', () => {
  // it('should call actionSheetController.create', () => {
  //   component.openDeleteSheet();
  //   expect(mockActionSheetController.create).toHaveBeenCalled();
  // });

  // it('should call actionSheetController.create with correct arguments', () => {
  //   component.openDeleteSheet();
  //   expect(mockActionSheetController.create).toHaveBeenCalledWith({
  //     header: 'Are you sure?',
  //     buttons: [
  //       {
  //         text: 'Delete',
  //         role: 'destructive',
  //         data: {
  //           name: mockItem.name,
  //           quantity: mockItem.quantity,
  //           weight: mockItem.weight,
  //         },
  //       },
  //       {
  //         text: 'Cancel',
  //         role: 'cancel',
  //         data: {
  //           action: 'cancel',
  //         },
  //       },
  //     ],
  //   });
  // });

  // it('should present the action sheet', fakeAsync (() => {
  //   const mockActionSheet = {
  //     present: jasmine.createSpy('present').and.returnValue(Promise.resolve()),
  //     onDidDismiss: () => jasmine.createSpy('onDidDismiss').and.returnValue(Promise.resolve({ role: 'cancel', data: mockItem })),
  //   };
  //   mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));

  //   component.openDeleteSheet();
  //   tick();

  //   expect(mockActionSheet.present).toHaveBeenCalled();
  // }));

  // it('should call emit itemDeleted when role is destructive', fakeAsync (() => {
  //   const mockActionSheet = {
  //     present: jasmine.createSpy('present').and.returnValue(Promise.resolve()),
  //     onDidDismiss: () => jasmine.createSpy('onDidDismiss').and.returnValue(Promise.resolve({ role: 'destructive', data: mockItem }))
  //   };
  //   mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));

  //   spyOn(component.itemDeleted, 'emit');
  //   component.openDeleteSheet();
  //   tick();
  //   // expect(component.itemDeleted.emit);
  // }));

  // it('should not call emit itemDeleted when role is cancel', fakeAsync (() => {
  //   const mockActionSheet = {
  //     present: jasmine.createSpy('present').and.returnValue(Promise.resolve()),
  //     onDidDismiss: () => jasmine.createSpy('onDidDismiss').and.returnValue(Promise.resolve({ role: 'cancel', data: mockItem }))
  //   };
  //   mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));

  //   spyOn(component.itemDeleted, 'emit');
  //   component.openDeleteSheet();
  //   tick();
  //   expect(component.itemDeleted.emit).not.toHaveBeenCalled();
  // }));

  // it('should call closeItem when role is cancel', async () => {
  //   const mockActionSheet = {
  //     present: jasmine.createSpy('present').and.returnValue(Promise.resolve()),
  //     onDidDismiss: () => Promise.resolve({ role: 'cancel', data: mockItem }),
  //   };
  //   mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));
  //   spyOn(component, 'closeItem');
  //   await component.openDeleteSheet();
  //   expect(component.closeItem).toHaveBeenCalled();
  // });

  // });
});
