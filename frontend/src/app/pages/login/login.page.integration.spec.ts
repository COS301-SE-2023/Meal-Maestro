import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { AuthenticationService, ErrorHandlerService } from "../../services/services";
import { LoginPage } from "./login.page";
import { TestBed } from "@angular/core/testing";
import { IonicModule } from "@ionic/angular";
import { RouterTestingModule } from "@angular/router/testing";
import { Router } from "@angular/router";
import { UserI } from "../../models/user.model";
import { Component } from "@angular/core";

describe('LoginPageIntegration', () => {
    let httpMock: HttpTestingController;
    let auth: AuthenticationService;
    let errorHandler: ErrorHandlerService;
    let component: LoginPage;
    let routerSpy = {navigate: jasmine.createSpy('navigate')};
    let apiUrl = 'http://localhost:8080';
    let mockUser: UserI;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [IonicModule.forRoot(), HttpClientTestingModule, RouterTestingModule.withRoutes([
                {path: 'app/tabs/home', component: DummyComponent}
            ])],
            providers: [
                AuthenticationService,
                ErrorHandlerService,
                { provide: Router, useValue: routerSpy },
                LoginPage
            ]
        }).compileComponents();

        httpMock = TestBed.inject(HttpTestingController);
        auth = TestBed.inject(AuthenticationService);
        errorHandler = TestBed.inject(ErrorHandlerService);
        component = TestBed.inject(LoginPage);
        
        mockUser = {
            username: 'test',
            password: 'test',
            email: 'test@test.com'
        };
    })

    afterEach(() => {
        httpMock.verify();
    });

    it('should login a user and navigate to home', async () => {
        spyOn(auth, 'login').and.callThrough();
        spyOn(errorHandler, 'presentSuccessToast').and.callThrough();

        let mockForm = {
            email: 'test@test.com',
            password: 'test'
        };

        await component.login(mockForm);

        const req = httpMock.expectOne(apiUrl + '/authenticate');
        expect(req.request.method).toBe('POST');
        req.flush({token: 'test'}, {status: 200, statusText: 'OK'});

        expect(auth.login).toHaveBeenCalled();
        expect(errorHandler.presentSuccessToast).toHaveBeenCalled();
        expect(routerSpy.navigate).toHaveBeenCalledWith(['app/tabs/home']);

    });
    
    it('should not login a user if 403 response and display an error', async () => {
        spyOn(auth, 'login').and.callThrough();
        spyOn(errorHandler, 'presentErrorToast').and.callThrough();

        let mockForm = {
            email: 'test@test.com',
            password: 'test'
        };

        await component.login(mockForm);

        const req = httpMock.expectOne(apiUrl + '/authenticate');
        expect(req.request.method).toBe('POST');
        req.flush(null, {status: 403, statusText: 'Forbidden'});

        expect(auth.login).toHaveBeenCalled();
        expect(errorHandler.presentErrorToast).toHaveBeenCalled();
    });

    it('should not login a user if 500 response and display an error', async () => {
        spyOn(auth, 'login').and.callThrough();
        spyOn(errorHandler, 'presentErrorToast').and.callThrough();

        let mockForm = {
            email: 'test@test.com',
            password: 'test'
        };

        await component.login(mockForm);

        const req = httpMock.expectOne(apiUrl + '/authenticate');
        expect(req.request.method).toBe('POST');
        req.flush(null, {status: 500, statusText: 'Internal Server Error'});

        expect(auth.login).toHaveBeenCalled();
        expect(errorHandler.presentErrorToast).toHaveBeenCalled();
    });
});

@Component({template: ''})
class DummyComponent {}