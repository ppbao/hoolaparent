package com.unisharing.hoola.hoolaclient.unit;

import com.unisharing.hoola.hoolaclient.web.UserController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/***
 * Unit Testing the Get Rest Service
 When we are unit testing a rest service, we would want to launch only the specific controller and the related MVC Components.
 WebMvcTest annotation is used for unit testing Spring MVC application. This can be used when a test focuses only Spring MVC components.
 Using this annotation will disable full auto-configuration and only apply configuration relevant to MVC tests.

 @RunWith(SpringRunner.class) : SpringRunner is short hand for SpringJUnit4ClassRunner which extends BlockJUnit4ClassRunner
 providing the functionality to launch a Spring TestContext Framework.
 @WebMvcTest(value = StudentController.class, secure = false): WebMvcTest annotation is used for unit testing Spring MVC application.
 This can be used when a test focuses only Spring MVC components. In this test, we want to launch only StudentController.
 All other controllers and mappings will not be launched when this unit test is executed.
 @Autowired private MockMvc mockMvc: MockMvc is the main entry point for server-side Spring MVC test support.
 It allows us to execute requests against the test context.
 @MockBean private StudentService studentService: MockBean is used to add mocks to a Spring ApplicationContext.
 A mock of studentService is created and auto-wired into the StudentController.
 Mockito.when(studentService.retrieveCourse(Mockito.anyString(),Mockito.anyString())).thenReturn(mockCourse):
 Mocking the method retrieveCourse to return the specific mockCourse when invoked.
 MockMvcRequestBuilders.get("/students/Student1/courses/Course1").accept(MediaType.APPLICATION_JSON):
 Creating a Request builder to be able to execute a get request to uri “/students/Student1/courses/Course1” with accept header as “application/json”
 mockMvc.perform(requestBuilder).andReturn(): mockMvc is used to perform the request and return the response back.
 JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false): We are using org.skyscreamer.jsonassert.JSONAssert.
 This allows us to do partial asserts against a JSON String. We are passing strict as false since we do not want to check for all fields in the response.
 *
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class,secure = false) //only focus on WebMvcTest which belongs to junit test
public class UserControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testContext(){
        Assert.assertNotNull(mockMvc);
    }


}

