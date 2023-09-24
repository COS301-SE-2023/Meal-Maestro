import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { PantryPage } from './pantry.page';
import { of } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';
import {
  AuthenticationService,
  BarcodeApiService,
  PantryApiService,
  ShoppingListApiService,
} from '../../services/services';
import { HttpResponse } from '@angular/common/http';

describe('PantryPage', () => {
  let component: PantryPage;
  let fixture: ComponentFixture<PantryPage>;
  let mockPantryService: jasmine.SpyObj<PantryApiService>;
  let mockShoppingListService: jasmine.SpyObj<ShoppingListApiService>;
  let mockAuthService: jasmine.SpyObj<AuthenticationService>;
  let mockBarcodeService: jasmine.SpyObj<BarcodeApiService>;
  let mockItems: FoodItemI[];

  beforeEach(async () => {
    mockPantryService = jasmine.createSpyObj('PantryApiService', [
      'getPantryItems',
      'addToPantry',
      'deletePantryItem',
    ]);
    mockShoppingListService = jasmine.createSpyObj('ShoppingListApiService', [
      'getShoppingListItems',
      'addToShoppingList',
      'deleteShoppingListItem',
    ]);
    mockAuthService = jasmine.createSpyObj('AuthenticationService', ['logout']);
    mockBarcodeService = jasmine.createSpyObj('BarcodeApiService', [
      'findProduct',
    ]);
    mockItems = [
      {
        name: 'test',
        quantity: 1,
        unit: 'pcs',
        price: 2,
      },
      {
        name: 'test2',
        quantity: 2,
        unit: 'g',
      },
    ];

    const emptyResponse = new HttpResponse<void>({ body: null, status: 200 });
    const itemsResponse = new HttpResponse<FoodItemI[]>({
      body: mockItems,
      status: 200,
    });
    const itemResponse = new HttpResponse<FoodItemI>({
      body: mockItems[0],
      status: 200,
    });
    const barcodeResponse = new HttpResponse<FoodItemI>({
      body: mockItems[0],
      status: 200,
    });

    mockPantryService.getPantryItems.and.returnValue(of(itemsResponse));
    mockPantryService.addToPantry.and.returnValue(of(itemResponse));
    mockPantryService.deletePantryItem.and.returnValue(of(emptyResponse));
    mockShoppingListService.getShoppingListItems.and.returnValue(
      of(itemsResponse)
    );
    mockShoppingListService.addToShoppingList.and.returnValue(of(itemResponse));
    mockShoppingListService.deleteShoppingListItem.and.returnValue(
      of(emptyResponse)
    );
    mockBarcodeService.findProduct.and.returnValue(of(barcodeResponse));

    await TestBed.configureTestingModule({
      imports: [IonicModule, PantryPage],
      providers: [
        { provide: PantryApiService, useValue: mockPantryService },
        { provide: ShoppingListApiService, useValue: mockShoppingListService },
        { provide: AuthenticationService, useValue: mockAuthService },
        { provide: BarcodeApiService, useValue: mockBarcodeService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PantryPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('#addItemToPantry should call addToPantry', () => {
    component.addItemToPantry({
      detail: { role: 'confirm', data: mockItems[0] },
    } as unknown as Event);
    expect(mockPantryService.addToPantry).toHaveBeenCalled();
    expect(mockPantryService.addToPantry).toHaveBeenCalledWith(mockItems[0]);
    expect(component.pantryItems).toContain(mockItems[0]);
  });

  it('#addItemToShoppingList should call addToShoppingList', () => {
    component.addItemToShoppingList({
      detail: { role: 'confirm', data: mockItems[0] },
    } as unknown as Event);
    expect(mockShoppingListService.addToShoppingList).toHaveBeenCalled();
    expect(mockShoppingListService.addToShoppingList).toHaveBeenCalledWith(
      mockItems[0]
    );
    expect(component.shoppingItems).toContain(mockItems[0]);
  });

  it('#onItemDeleted should call deletePantryItem if segment is pantry ', () => {
    component.segment = 'pantry';
    component.pantryItems = [...mockItems];
    component.onItemDeleted(mockItems[0]);
    expect(mockPantryService.deletePantryItem).toHaveBeenCalled();
    expect(mockPantryService.deletePantryItem).toHaveBeenCalledWith(
      mockItems[0]
    );
    expect(component.pantryItems).not.toContain(mockItems[0]);
  });

  it('#onItemDeleted should call deleteShoppingListItem if segment is shopping ', () => {
    component.segment = 'shopping';
    component.shoppingItems = [...mockItems];
    component.onItemDeleted(mockItems[0]);
    expect(mockShoppingListService.deleteShoppingListItem).toHaveBeenCalled();
    expect(mockShoppingListService.deleteShoppingListItem).toHaveBeenCalledWith(
      mockItems[0]
    );
    expect(component.shoppingItems).not.toContain(mockItems[0]);
  });

  it('#onItemDeleted should not call deleteShoppingListItem or deletePantryItem if segment is not shopping or pantry ', () => {
    component.segment = null;
    component.onItemDeleted(mockItems[0]);
    expect(
      mockShoppingListService.deleteShoppingListItem
    ).not.toHaveBeenCalled();
    expect(mockPantryService.deletePantryItem).not.toHaveBeenCalled();
  });
});
