### Spring MVC Unit Test

#### Overview
Fluent api, enable testing of controller as `controller`, not just a method, (think request mapping).

See the full presentation from springsource [here](https://www.youtube.com/watch?v=K6x8LE7Qd1Q "Webinar: Testing Web Applications with Spring 3.2").

#### Details
##### Unit Test (standalone) vs Integration Test
I would say this is a unit test because we will mock the `collborators`, although we also have the option to not mock the `collaborators` thus doing something like an integration test.

In the spirit of unit-testing, I personally prefer to do standalone testing on the controller, by mocking the `collaborators` and testing one controller at a time.

##### DispatcherServlet runtime (so we can test request mapping)
It will handle the creation of `DispatcherServlet` runtime for each test, so we can also test the mvc wiring, not just the inner workings of the methods.

We won't call the controller methods directly. Instead we will create a mock request and pass it to the provided [mvc test infrastructure](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/test/web/servlet/MockMvc.html "MockMvc"). 

##### MockMvc and other usefull mocks
Classes in `org.springframework.test.web.servlet.*`
Check for yourself [here](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/test/web/servlet/package-summary.html "Contains server-side support for testing Spring MVC applications").


##### Assertions
We have two options for asserting, either the `response` object or the `ModelAndView`. I would prefer asserting the `ModelAndView`.

I think asserting the response object would be realistic if our controller are json/xml rest endpoints. I don't feel like asserting html markup...

Asserting the `response` object is also not supported if the rendering tech is `jsp` (not running in a servlet container). Freemarker, Velocity, etc, is supported. Another reason not to use `jsp`?

##### Fluent api
Api that looks like a `builder` pattern.

##### Sample from springsource presentation and ref docs
Creating Standalone Unit Test

    ...
    this.mockMvc = MockMvcBuilders.standaloneSetup(new MyController())....build();
    ...

Fluent Api

    ...
    @Test
    public void getAccount() throws Exception {
        this.mockMvc.perform(get("/accounts/1").accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.name").value("Lee");
    }
    ...

Access to request, response, ModelAndView

    ...
    @Test
    public void getAccountAssertRequestResponseModelAndView() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/accounts/1").accept("application/json;charset=UTF-8"))
            .andReturn();
        ...
        MockHttpServletRequest request = mvcResult.getRequest();
        MockHttpServletResponse response = mvcResult.getResponse();
        ModelAndView mav = mvcResult.getModelAndView();
        ...
    }
    ...
