import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PantryPage } from './pantry.page';
import { of } from 'rxjs';
import { FoodItemI } from '../../models/interfaces';
import { AuthenticationService, PantryApiService, ShoppingListApiService } from '../../services/services';
import { HttpResponse } from '@angular/common/http';

describe('PantryPageIntegration', () => {
    let httpMock: HttpTestingController;
    let pantryService: PantryApiService;
    let shoppingListService: ShoppingListApiService;
    let authService: AuthenticationService;
    let component: PantryPage;
    let pantryItems: FoodItemI[];
    let shoppingListItems: FoodItemI[];
    let apiUrl: string = 'http://localhost:8080';

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [IonicModule.forRoot(), HttpClientTestingModule],
            providers: [PantryApiService, 
                        ShoppingListApiService, 
                        AuthenticationService,
                        PantryPage
                    ],
        }).compileComponents();

        httpMock = TestBed.inject(HttpTestingController);
        pantryService = TestBed.inject(PantryApiService);
        shoppingListService = TestBed.inject(ShoppingListApiService);
        authService = TestBed.inject(AuthenticationService);
        component = TestBed.inject(PantryPage);

        pantryItems = [
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

        shoppingListItems = [
            {
                name: 'test3',
                quantity: 3,
                weight: 3,
            },
            {
                name: 'test4',
                quantity: 4,
                weight: 4,
            },
        ];
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should fetch pantry and shopping list', async () => {
        spyOn(pantryService, 'getPantryItems').and.callThrough();
        spyOn(authService, 'logout').and.callThrough();
        spyOn(shoppingListService, 'getShoppingListItems').and.callThrough();

        await component.fetchItems();

        const req = httpMock.expectOne(apiUrl + '/getPantry');
        expect(req.request.method).toBe('POST');
        req.flush(pantryItems);

        const req2 = httpMock.expectOne(apiUrl + '/getShoppingList');
        expect(req2.request.method).toBe('POST');
        req2.flush(shoppingListItems);

        expect(component.pantryItems).toEqual(pantryItems);
        expect(pantryService.getPantryItems).toHaveBeenCalled();

        expect(component.shoppingItems).toEqual(shoppingListItems);
        expect(shoppingListService.getShoppingListItems).toHaveBeenCalled();

    })
});