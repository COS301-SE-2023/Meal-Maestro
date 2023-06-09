import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ActionSheetController, IonicModule, PickerController } from '@ionic/angular';

import { FoodListItemComponent } from './food-list-item.component';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { ShoppingListApiService } from '../../services/shopping-list-api/shopping-list-api.service';
import { FoodItemI } from '../../models/interfaces';
import { of } from 'rxjs';

describe('FoodListItemComponent', () => {
  let component: FoodListItemComponent;
  let fixture: ComponentFixture<FoodListItemComponent>;
  let mockPantryService: jasmine.SpyObj<PantryApiService>;
  let mockShoppingListService: jasmine.SpyObj<ShoppingListApiService>;
  let mockActionSheetController: jasmine.SpyObj<any>;
  let mockPickerController: jasmine.SpyObj<any>;
  let mockItem: FoodItemI;

  beforeEach(waitForAsync(() => {
    mockPantryService = jasmine.createSpyObj('PantryApiService', ['updatePantryItem']);
    mockShoppingListService = jasmine.createSpyObj('ShoppingListApiService', ['updateShoppingListItem']);
    mockActionSheetController = jasmine.createSpyObj('ActionSheetController', ['create']);
    mockPickerController = jasmine.createSpyObj('PickerController', ['create']);
    mockItem = {
      name: 'test',
      quantity: 1,
      weight: 1,
    };

    const emptyFoodItem: FoodItemI = {
      name: '',
      quantity: null,
      weight: null,
    };

    mockPantryService.updatePantryItem.and.returnValue(of(emptyFoodItem));
    mockShoppingListService.updateShoppingListItem.and.returnValue(of(emptyFoodItem));
    mockActionSheetController.create.calls.reset();

    TestBed.configureTestingModule({
      imports: [IonicModule.forRoot(), FoodListItemComponent],
      providers: [
        { provide: PantryApiService, useValue: mockPantryService },
        { provide: ShoppingListApiService, useValue: mockShoppingListService },
        { provide: ActionSheetController, useValue: mockActionSheetController },
        { provide: PickerController, useValue: mockPickerController },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FoodListItemComponent);
    component = fixture.componentInstance;
    component.item = mockItem;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should inject ActionSheetController', () => {
  //   expect(mockActionSheetController).toBeDefined();
  // });

  // describe('openDeleteSheet', () => {
  //   it('should call actionSheetController.create', () => {
  //     component.openDeleteSheet();
  //     expect(mockActionSheetController.create).toHaveBeenCalled();
  //   });

  //   it('should call actionSheetController.create with correct arguments', () => {
  //     component.openDeleteSheet();
  //     expect(mockActionSheetController.create).toHaveBeenCalledWith({
  //       header: 'Are you sure?',
  //       buttons: [
  //         {
  //           text: 'Delete',
  //           role: 'destructive',
  //           data: {
  //             name: mockItem.name,
  //             quantity: mockItem.quantity,
  //             weight: mockItem.weight,
  //           },
  //         },
  //         {
  //           text: 'Cancel',
  //           role: 'cancel',
  //           data: {
  //             action: 'cancel',
  //           },
  //         },
  //       ],
  //     });
  //   });

  //   it('should present the action sheet', async () => {
  //     const mockActionSheet = {
  //       present: jasmine.createSpy('present'),
  //       onDidDismiss: () => Promise.resolve({ role: 'cancel', data: mockItem }),
  //     };
  //     mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));
  //     await component.openDeleteSheet();
  //     expect(mockActionSheet.present).toHaveBeenCalled();
  //   });

  //   it('should call emit itemDeleted when role is destructive', async () => {
  //     const mockActionSheet = {
  //       present: jasmine.createSpy('present'),
  //       onDidDismiss: () => Promise.resolve({ role: 'destructive', data: mockItem }),
  //     };
  //     mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));
  //     spyOn(component.itemDeleted, 'emit');
  //     await component.openDeleteSheet();
  //     expect(component.itemDeleted.emit).toHaveBeenCalledWith(mockItem);
  //   });

  //   it('should not call emit itemDeleted when role is cancel', async () => {
  //     const mockActionSheet = {
  //       present: jasmine.createSpy('present'),
  //       onDidDismiss: () => Promise.resolve({ role: 'cancel', data: mockItem }),
  //     };
  //     mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));
  //     spyOn(component.itemDeleted, 'emit');
  //     await component.openDeleteSheet();
  //     expect(component.itemDeleted.emit).not.toHaveBeenCalled();
  //   });

  //   it('should call closeItem when role is cancel', async () => {
  //     const mockActionSheet = {
  //       present: jasmine.createSpy('present'),
  //       onDidDismiss: () => Promise.resolve({ role: 'cancel', data: mockItem }),
  //     };
  //     mockActionSheetController.create.and.returnValue(Promise.resolve(mockActionSheet));
  //     spyOn(component, 'closeItem');
  //     await component.openDeleteSheet();
  //     expect(component.closeItem).toHaveBeenCalled();
  //   });

  // });
});
