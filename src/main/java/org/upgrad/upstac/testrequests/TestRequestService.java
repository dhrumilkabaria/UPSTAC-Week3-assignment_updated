package org.upgrad.upstac.testrequests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.users.User;

import java.time.LocalDate;
import java.util.List;

@Service
public class TestRequestService {

    @Autowired
    private TestRequestRepository testRequestRepository;



    private static Logger logger = LoggerFactory.getLogger(TestRequestService.class);



    public TestRequest createTestRequestFrom(User user,CreateTestRequest createTestRequest) {

        validateExistingRequestsNotPresentWithSameDetails(createTestRequest);

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

        testRequest.setCreatedBy(user);
        return testRequestRepository.save(testRequest);
    }

    public void validateExistingRequestsNotPresentWithSameDetails(CreateTestRequest createTestRequest) {
        List<TestRequest> testRequests = testRequestRepository.findByEmailOrPhoneNumber(createTestRequest.getEmail(), createTestRequest.getPhoneNumber());


        for (TestRequest testRequest : testRequests) {

            if (testRequest.getStatus().equals(RequestStatus.COMPLETED) == false)
                throw new AppException("A Request with same PhoneNumber or Email is already in progress ");
        }

    }

    public List<TestRequest> findByStatus(RequestStatus requestStatus){

        return testRequestRepository.findByStatus(requestStatus);
    }

    public List<TestRequest> getHistoryFor(User loggedInUser){

        return testRequestRepository.findByCreatedBy(loggedInUser);
    }


}
