import { TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PantryPage } from './pantry.page';
import { FoodItemI } from '../../models/interfaces';
import { AuthenticationService, PantryApiService, ShoppingListApiService } from '../../services/services';
import { OverlayEventDetail } from '@ionic/core/components';


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

    it('should handle 403 error when getting items', async () => {
        spyOn(pantryService, 'getPantryItems').and.callThrough();
        spyOn(shoppingListService, 'getShoppingListItems').and.callThrough();
        spyOn(authService, 'logout').and.callThrough();
        spyOn(component, 'fetchItems').and.callThrough();

        await component.fetchItems();

        const req = httpMock.expectOne(apiUrl + '/getPantry');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 403, statusText: 'Forbidden' });

        const req2 = httpMock.expectOne(apiUrl + '/getShoppingList');
        expect(req2.request.method).toBe('POST');
        req2.flush(null, { status: 403, statusText: 'Forbidden' });

        expect(pantryService.getPantryItems).toHaveBeenCalled();
        expect(authService.logout).toHaveBeenCalled();
        expect(shoppingListService.getShoppingListItems).toHaveBeenCalled();
    });

    it('should handle other error when getting items', async () => {
        spyOn(pantryService, 'getPantryItems').and.callThrough();
        spyOn(shoppingListService, 'getShoppingListItems').and.callThrough();
        spyOn(authService, 'logout').and.callThrough();
        // spyOn(component, 'fetchItems').and.callThrough();

        await component.fetchItems();

        const req = httpMock.expectOne(apiUrl + '/getPantry');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 500, statusText: 'Internal Server Error' });

        const req2 = httpMock.expectOne(apiUrl + '/getShoppingList');
        expect(req2.request.method).toBe('POST');
        req2.flush(null, { status: 500, statusText: 'Internal Server Error' });

        expect(pantryService.getPantryItems).toHaveBeenCalled();
        expect(shoppingListService.getShoppingListItems).toHaveBeenCalled();
    });

    it('should add item to pantry', async () => {
        let item: FoodItemI = {
            name: 'test4',
            quantity: 1,
            weight: 1,
        };
        let response = {
            status: 200,
            body: item
        }
        let ev: CustomEvent<OverlayEventDetail<FoodItemI>> = {
            detail: {
                data: item,
                role: 'confirm'
            },
            initCustomEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined, detail?: OverlayEventDetail<FoodItemI> | undefined): void {
                throw new Error('Function not implemented.');
            },
            bubbles: false,
            cancelBubble: false,
            cancelable: false,
            composed: false,
            currentTarget: null,
            defaultPrevented: false,
            eventPhase: 0,
            isTrusted: false,
            returnValue: false,
            srcElement: null,
            target: null,
            timeStamp: 0,
            type: '',
            composedPath: function (): EventTarget[] {
                throw new Error('Function not implemented.');
            },
            initEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined): void {
                throw new Error('Function not implemented.');
            },
            preventDefault: function (): void {
                throw new Error('Function not implemented.');
            },
            stopImmediatePropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            stopPropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            NONE: 0,
            CAPTURING_PHASE: 1,
            AT_TARGET: 2,
            BUBBLING_PHASE: 3
        }

        spyOn(pantryService, 'addToPantry').and.callThrough();

        await component.addItemToPantry(ev);

        const req = httpMock.expectOne(apiUrl + '/addToPantry');
        expect(req.request.method).toBe('POST');
        req.flush(response.body);

        expect(pantryService.addToPantry).toHaveBeenCalled();
        expect(component.pantryItems).toContain(item);
    });

    it('should handle 403 error when adding item to pantry', async () => {
        let item: FoodItemI = {
            name: 'test4',
            quantity: 1,
            weight: 1,
        };
        let response = {
            status: 403,
            body: item
        }
        let ev: CustomEvent<OverlayEventDetail<FoodItemI>> = {
            detail: {
                data: item,
                role: 'confirm'
            },
            initCustomEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined, detail?: OverlayEventDetail<FoodItemI> | undefined): void {
                throw new Error('Function not implemented.');
            },
            bubbles: false,
            cancelBubble: false,
            cancelable: false,
            composed: false,
            currentTarget: null,
            defaultPrevented: false,
            eventPhase: 0,
            isTrusted: false,
            returnValue: false,
            srcElement: null,
            target: null,
            timeStamp: 0,
            type: '',
            composedPath: function (): EventTarget[] {
                throw new Error('Function not implemented.');
            },
            initEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined): void {
                throw new Error('Function not implemented.');
            },
            preventDefault: function (): void {
                throw new Error('Function not implemented.');
            },
            stopImmediatePropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            stopPropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            NONE: 0,
            CAPTURING_PHASE: 1,
            AT_TARGET: 2,
            BUBBLING_PHASE: 3
        }

        spyOn(pantryService, 'addToPantry').and.callThrough();
        spyOn(authService, 'logout').and.callThrough();

        await component.addItemToPantry(ev);

        const req = httpMock.expectOne(apiUrl + '/addToPantry');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 403, statusText: 'Forbidden' });

        expect(pantryService.addToPantry).toHaveBeenCalled();
        expect(authService.logout).toHaveBeenCalled();
    });

    it('should handle other errors when adding item to pantry', async () => {
        let item: FoodItemI = {
            name: 'test4',
            quantity: 1,
            weight: 1,
        };
        let response = {
            status: 500,
            body: item
        }
        let ev: CustomEvent<OverlayEventDetail<FoodItemI>> = {
            detail: {
                data: item,
                role: 'confirm'
            },
            initCustomEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined, detail?: OverlayEventDetail<FoodItemI> | undefined): void {
                throw new Error('Function not implemented.');
            },
            bubbles: false,
            cancelBubble: false,
            cancelable: false,
            composed: false,
            currentTarget: null,
            defaultPrevented: false,
            eventPhase: 0,
            isTrusted: false,
            returnValue: false,
            srcElement: null,
            target: null,
            timeStamp: 0,
            type: '',
            composedPath: function (): EventTarget[] {
                throw new Error('Function not implemented.');
            },
            initEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined): void {
                throw new Error('Function not implemented.');
            },
            preventDefault: function (): void {
                throw new Error('Function not implemented.');
            },
            stopImmediatePropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            stopPropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            NONE: 0,
            CAPTURING_PHASE: 1,
            AT_TARGET: 2,
            BUBBLING_PHASE: 3
        }

        spyOn(pantryService, 'addToPantry').and.callThrough();

        await component.addItemToPantry(ev);

        const req = httpMock.expectOne(apiUrl + '/addToPantry');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 500, statusText: 'Internal Server Error' });

        expect(pantryService.addToPantry).toHaveBeenCalled();
    });

    it('should add item to shopping list', async () => {
        let item: FoodItemI = {
            name: 'test4',
            quantity: 1,
            weight: 1,
        };
        let response = {
            status: 200,
            body: item
        }
        let ev: CustomEvent<OverlayEventDetail<FoodItemI>> = {
            detail: {
                data: item,
                role: 'confirm'
            },
            initCustomEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined, detail?: OverlayEventDetail<FoodItemI> | undefined): void {
                throw new Error('Function not implemented.');
            },
            bubbles: false,
            cancelBubble: false,
            cancelable: false,
            composed: false,
            currentTarget: null,
            defaultPrevented: false,
            eventPhase: 0,
            isTrusted: false,
            returnValue: false,
            srcElement: null,
            target: null,
            timeStamp: 0,
            type: '',
            composedPath: function (): EventTarget[] {
                throw new Error('Function not implemented.');
            },
            initEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined): void {
                throw new Error('Function not implemented.');
            },
            preventDefault: function (): void {
                throw new Error('Function not implemented.');
            },
            stopImmediatePropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            stopPropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            NONE: 0,
            CAPTURING_PHASE: 1,
            AT_TARGET: 2,
            BUBBLING_PHASE: 3
        }

        spyOn(shoppingListService, 'addToShoppingList').and.callThrough();

        await component.addItemToShoppingList(ev);

        const req = httpMock.expectOne(apiUrl + '/addToShoppingList');
        expect(req.request.method).toBe('POST');
        req.flush(response.body);

        expect(shoppingListService.addToShoppingList).toHaveBeenCalled();
        expect(component.shoppingItems).toContain(item);
    });

    it('should handle 403 error when adding item to shopping list', async () => {
        let item: FoodItemI = {
            name: 'test4',
            quantity: 1,
            weight: 1,
        };
        let response = {
            status: 403,
            body: item
        }
        let ev: CustomEvent<OverlayEventDetail<FoodItemI>> = {
            detail: {
                data: item,
                role: 'confirm'
            },
            initCustomEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined, detail?: OverlayEventDetail<FoodItemI> | undefined): void {
                throw new Error('Function not implemented.');
            },
            bubbles: false,
            cancelBubble: false,
            cancelable: false,
            composed: false,
            currentTarget: null,
            defaultPrevented: false,
            eventPhase: 0,
            isTrusted: false,
            returnValue: false,
            srcElement: null,
            target: null,
            timeStamp: 0,
            type: '',
            composedPath: function (): EventTarget[] {
                throw new Error('Function not implemented.');
            },
            initEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined): void {
                throw new Error('Function not implemented.');
            },
            preventDefault: function (): void {
                throw new Error('Function not implemented.');
            },
            stopImmediatePropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            stopPropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            NONE: 0,
            CAPTURING_PHASE: 1,
            AT_TARGET: 2,
            BUBBLING_PHASE: 3
        }

        spyOn(shoppingListService, 'addToShoppingList').and.callThrough();
        spyOn(authService, 'logout').and.callThrough();

        await component.addItemToShoppingList(ev);

        const req = httpMock.expectOne(apiUrl + '/addToShoppingList');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 403, statusText: 'Forbidden' });

        expect(shoppingListService.addToShoppingList).toHaveBeenCalled();
        expect(authService.logout).toHaveBeenCalled();
    });

    it('should handle other errors when adding item to shopping list', async () => {
        let item: FoodItemI = {
            name: 'test4',
            quantity: 1,
            weight: 1,
        };
        let response = {
            status: 500,
            body: item
        }
        let ev: CustomEvent<OverlayEventDetail<FoodItemI>> = {
            detail: {
                data: item,
                role: 'confirm'
            },
            initCustomEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined, detail?: OverlayEventDetail<FoodItemI> | undefined): void {
                throw new Error('Function not implemented.');
            },
            bubbles: false,
            cancelBubble: false,
            cancelable: false,
            composed: false,
            currentTarget: null,
            defaultPrevented: false,
            eventPhase: 0,
            isTrusted: false,
            returnValue: false,
            srcElement: null,
            target: null,
            timeStamp: 0,
            type: '',
            composedPath: function (): EventTarget[] {
                throw new Error('Function not implemented.');
            },
            initEvent: function (type: string, bubbles?: boolean | undefined, cancelable?: boolean | undefined): void {
                throw new Error('Function not implemented.');
            },
            preventDefault: function (): void {
                throw new Error('Function not implemented.');
            },
            stopImmediatePropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            stopPropagation: function (): void {
                throw new Error('Function not implemented.');
            },
            NONE: 0,
            CAPTURING_PHASE: 1,
            AT_TARGET: 2,
            BUBBLING_PHASE: 3
        }

        spyOn(shoppingListService, 'addToShoppingList').and.callThrough();

        await component.addItemToShoppingList(ev);

        const req = httpMock.expectOne(apiUrl + '/addToShoppingList');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 500, statusText: 'Internal Server Error' });

        expect(shoppingListService.addToShoppingList).toHaveBeenCalled();
    });

    it('should delete item from pantry if segment is pantry', async () => {
        let item: FoodItemI =  {
            name: 'test',
            quantity: 1,
            weight: 1,
        };
        component.segment = 'pantry';
        component.pantryItems = pantryItems;
        component.shoppingItems = shoppingListItems;

        spyOn(pantryService, 'deletePantryItem').and.callThrough();

        await component.onItemDeleted(item);

        const req = httpMock.expectOne(apiUrl + '/removeFromPantry');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 200, statusText: 'OK' });

        expect(pantryService.deletePantryItem).toHaveBeenCalled();
        expect(component.pantryItems).not.toContain(item);

        expect(component.shoppingItems).toEqual(shoppingListItems);
    });

    it('should delete item from shopping list if segment is shopping', async () => {
        let item: FoodItemI =  {
            name: 'test',
            quantity: 1,
            weight: 1,
        };
        component.segment = 'shopping';
        component.pantryItems = pantryItems;
        component.shoppingItems = shoppingListItems;

        spyOn(shoppingListService, 'deleteShoppingListItem').and.callThrough();

        await component.onItemDeleted(item);

        const req = httpMock.expectOne(apiUrl + '/removeFromShoppingList');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 200, statusText: 'OK' });

        expect(shoppingListService.deleteShoppingListItem).toHaveBeenCalled();
        expect(component.shoppingItems).not.toContain(item);

        expect(component.pantryItems).toEqual(pantryItems);
    });

    it('should move item from shopping list to pantry if item is bought', async () => {
        let item: FoodItemI =  {
            name: 'test3',
            quantity: 3,
            weight: 3,
        };
        let response = {
            body: [
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
                {
                    name: 'test3',
                    quantity: 3,
                    weight: 3,
                }
            ]
        }
        component.segment = 'shopping';
        component.pantryItems = pantryItems;
        component.shoppingItems = shoppingListItems;

        spyOn(shoppingListService, 'buyItem').and.callThrough();

        await component.onItemBought(item);

        const req = httpMock.expectOne(apiUrl + '/buyItem');
        expect(req.request.method).toBe('POST');
        req.flush(response.body, { status: 200, statusText: 'OK' });

        expect(shoppingListService.buyItem).toHaveBeenCalled();
        expect(component.shoppingItems).not.toContain(item);
        expect(component.pantryItems).toContain(item);

        expect(component.pantryItems).toEqual(response.body);
    });

    it('should handle 403 error when moving item from shopping list to pantry if item is bought', async () => {
        let item: FoodItemI =  {
            name: 'test3',
            quantity: 3,
            weight: 3,
        };
        component.segment = 'shopping';
        component.pantryItems = pantryItems;
        component.shoppingItems = shoppingListItems;

        spyOn(shoppingListService, 'buyItem').and.callThrough();
        spyOn(authService, 'logout').and.callThrough();

        await component.onItemBought(item);

        const req = httpMock.expectOne(apiUrl + '/buyItem');
        expect(req.request.method).toBe('POST');
        req.flush(null, { status: 403, statusText: 'Forbidden' });

        expect(shoppingListService.buyItem).toHaveBeenCalled();
        expect(authService.logout).toHaveBeenCalled();
    });
});