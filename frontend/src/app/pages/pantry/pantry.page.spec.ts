import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';


import { PantryPage } from './pantry.page';
import { of } from 'rxjs';
import { PantryApiService } from '../../services/pantry-api/pantry-api.service';
import { ShoppingListApiService } from '../../services/shopping-list-api/shopping-list-api.service';
import { FoodItemI } from '../../models/interfaces.model';

describe('PantryPage', () => {
  let component: PantryPage;
  let fixture: ComponentFixture<PantryPage>;
  let mockPantryService: jasmine.SpyObj<PantryApiService>;
  let mockShoppingListService: jasmine.SpyObj<ShoppingListApiService>;
  let mockItems: FoodItemI[];

  beforeEach(async () => {
    mockPantryService = jasmine.createSpyObj('PantryApiService', ['getPantryItems', 'addToPantry', 'deletePantryItem']);
    mockShoppingListService = jasmine.createSpyObj('ShoppingListApiService', ['getShoppingListItems', 'addToShoppingList', 'deleteShoppingListItem']);

    mockItems = [
      {
        name: 'test',
        quantity: 1,
        weight: 1,
      },
      {
        name: 'test2',
        quantity: 2,
        weight: 2,
      },
    ];

    mockPantryService.getPantryItems.and.returnValue(of(mockItems));
    mockPantryService.addToPantry.and.returnValue(of(mockItems[0]));
    mockShoppingListService.getShoppingListItems.and.returnValue(of(mockItems));
    mockShoppingListService.addToShoppingList.and.returnValue(of(mockItems[0]));


    await TestBed.configureTestingModule({
      declarations: [PantryPage],
      imports: [IonicModule],
      providers: [
        { provide: PantryApiService, useValue: mockPantryService },
        { provide: ShoppingListApiService, useValue: mockShoppingListService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PantryPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('#ngOnInit should call getPantryItems and getShoppingListItems', () => {
    component.ngOnInit();
    expect(mockPantryService.getPantryItems).toHaveBeenCalled();
    expect(mockShoppingListService.getShoppingListItems).toHaveBeenCalled();

    expect(component.pantryItems).toEqual(mockItems);
    expect(component.shoppingItems).toEqual(mockItems);

    expect(mockPantryService.getPantryItems).toHaveBeenCalledTimes(1);
    expect(mockShoppingListService.getShoppingListItems).toHaveBeenCalledTimes(1);
  });

  it('#addItemToPantry should call addToPantry', () => {
    component.addItemToPantry({ detail: { role: 'confirm', data: mockItems[0] } } as unknown as Event);
    expect(mockPantryService.addToPantry).toHaveBeenCalled();
    expect(mockPantryService.addToPantry).toHaveBeenCalledWith(mockItems[0]);
    expect(component.pantryItems).toContain(mockItems[0]);
  });

  it('#addItemToShoppingList should call addToShoppingList', () => {
    component.addItemToShoppingList({ detail: { role: 'confirm', data: mockItems[0] } } as unknown as Event);
    expect(mockShoppingListService.addToShoppingList).toHaveBeenCalled();
    expect(mockShoppingListService.addToShoppingList).toHaveBeenCalledWith(mockItems[0]);
    expect(component.shoppingItems).toContain(mockItems[0]);
  });

  it('#onItemDeleted should call deletePantryItem if segment is pantry ', () => {
    component.segment = 'pantry';
    component.onItemDeleted(mockItems[0]);
    expect(mockPantryService.deletePantryItem).toHaveBeenCalled();
    expect(mockPantryService.deletePantryItem).toHaveBeenCalledWith(mockItems[0]);
    expect(component.pantryItems).not.toContain(mockItems[0]);
  });

  it('#onItemDeleted should call deleteShoppingListItem if segment is shopping ', () => {
    component.segment = 'shopping';
    component.onItemDeleted(mockItems[0]);
    expect(mockShoppingListService.deleteShoppingListItem).toHaveBeenCalled();
    expect(mockShoppingListService.deleteShoppingListItem).toHaveBeenCalledWith(mockItems[0]);
    expect(component.shoppingItems).not.toContain(mockItems[0]);
  });

  it('#onItemDeleted should not call deleteShoppingListItem or deletePantryItem if segment is not shopping or pantry ', () => {
    component.segment = null;
    component.onItemDeleted(mockItems[0]);
    expect(mockShoppingListService.deleteShoppingListItem).not.toHaveBeenCalled();
    expect(mockPantryService.deletePantryItem).not.toHaveBeenCalled();
  });

  it('#segmentChanged should set segment to the event detail value', () => {
    component.segmentChanged({ detail: { value: 'shopping' } } as unknown as Event);
    expect(component.segment).toEqual('shopping');
  });

  it('#segmentChanged should set segment to pantry if event detail value is not shopping or pantry', () => {
    component.segmentChanged({ detail: { value: 'test' } } as unknown as Event);
    expect(component.segment).toEqual('pantry');
  });
});
