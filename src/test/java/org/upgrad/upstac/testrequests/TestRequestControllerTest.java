package org.upgrad.upstac.testrequests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TestRequestControllerTest {

    @InjectMocks
    TestRequestController testRequestController;

    @Mock
    TestRequestService testRequestService;

    @Mock
    UserLoggedInService userLoggedInService;

    @Test
    public void when_testRequestCreateService_createTestRequestFrom_returns_valid_expect_same_as_response(){

        //Arrange
        User user= createUser();
        CreateTestRequest createTestRequest = createTestRequest();
        TestRequest mockedResponse = getMockedResponseFrom(createTestRequest);
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestService.createTestRequestFrom(user,createTestRequest)).thenReturn(mockedResponse);

        //Act
        TestRequest result = testRequestController.createRequest(createTestRequest);

        //Assert
        assertNotNull(result);
        assertEquals(result,mockedResponse);

    }

    @Test
    public void when_testRequestCreateService_createTestRequestFrom_throws_appException_expect_response_status_exception_to_be_thrown(){

        //Arrange
        User user= createUser();
        CreateTestRequest createTestRequest = createTestRequest();

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestService.createTestRequestFrom(user,createTestRequest)).thenThrow(new AppException("Invalid data"));

        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            testRequestController.createRequest(createTestRequest);
        });


        //Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid data",result.getReason());

    }

    public CreateTestRequest createTestRequest() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setAddress("some Addres");
        createTestRequest.setAge(98);
        createTestRequest.setEmail("someone" + "123456789" + "@somedomain.com");
        createTestRequest.setGender(Gender.MALE);
        createTestRequest.setName("someuser");
        createTestRequest.setPhoneNumber("123456789");
        createTestRequest.setPinCode(716768);
        return createTestRequest;
    }
    public TestRequest getMockedResponseFrom(CreateTestRequest createTestRequest) {
        TestRequest testRequest = new TestRequest();

        testRequest.setName(createTestRequest.getName());
        testRequest.setCreated(LocalDate.now());
        testRequest.setStatus(RequestStatus.INITIATED);
        testRequest.setAge(createTestRequest.getAge());
        testRequest.setEmail(createTestRequest.getEmail());
        testRequest.setPhoneNumber(createTestRequest.getPhoneNumber());
        testRequest.setPinCode(createTestRequest.getPinCode());
        testRequest.setAddress(createTestRequest.getAddress());
        testRequest.setGender(createTestRequest.getGender());

        testRequest.setCreatedBy(createUser());

        return testRequest;
    }


    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("someuser");
        return user;
    }
}