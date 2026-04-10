Testing related:
1. Unit Testing
Unit testing means testing the smallest testable part of the program, such as a function or method. Its goal is to verify that one individual component works correctly by itself, and it should not depend on real external dependencies. Unit tests are usually written by developers during development, and mocking tools like Mockito are commonly used.
For example, if I have a calculateTax() method, I can test whether calculateTax(100) returns the expected value. This is different from integration testing because I am only checking one small piece of logic, not how multiple systems work together.

2. Functional Testing
Functional testing checks whether a feature behaves according to the business requirement, from the user’s point of view. Testing functions of a single module or a complete application from the user’s perspective, to verify whether requirements and features are fulfilled.
For example, if a user clicks “Login,” functional testing checks whether the user can successfully log in with valid credentials and get the expected response. Compared with unit testing, functional testing cares less about internal code and more about whether the feature works as expected.

3. Integration Testing
Integration testing focuses on the interaction between multiple components or modules. Its purpose is to verify that the collaboration between them is correct. Functional and integration tests should use real dependencies instead of mocking, such as a real database or another real service.
For example, if OrderService calls InventoryService and PaymentService, integration testing checks whether these services work together correctly. Compared with unit testing, integration testing is broader because it tests how pieces connect, not just one isolated method.

4. Regression Testing
Regression testing means re-running existing tests after code or environment changes to make sure no new defects were introduced. This often appears in the CI/CD pipeline.
For example, if I fix a bug in the checkout page, I should re-run previously passed tests for login, cart, payment, and order history to confirm that my change did not break old features. Compared with smoke testing, regression testing is usually wider and more thorough.

5. Smoke Testing
Smoke testing is also called build verification testing. It is part of the CI/CD pipeline and checks whether the newly deployed build can perform basic functions. If it fails, the pipeline should stop.
For example, after deployment, I may only test whether the application starts, whether the homepage loads, and whether the login API returns successfully. Compared with regression testing, smoke testing is much lighter. It is like asking, “Is the build alive?” while regression asks, “Did we break anything important?”

6. Performance Testing
Performance testing checks the performance and response time of the system under a specific workload. For examples like QPS and response time targets, such as 200 ms.
For example, I may test whether a search API can respond within 200 ms under 500 requests per second. Compared with stress testing, performance testing usually stays within expected or target traffic levels.

7. Stress Testing
Stress testing pushes the system beyond normal conditions to check how much pressure it can handle and whether it still works under a very high load. It is testing with workloads beyond normal levels, such as 2k QPS.
For example, if the normal load is 300 QPS, I may intentionally increase it to 3000 QPS to see whether the service crashes, slows down badly, or recovers properly. Compared with performance testing, stress testing is more about limits and failure behavior.

8. A/B Testing
In practice, it means showing two different versions of a feature to different user groups and comparing their business results.
For example, group A sees the old “Buy Now” button, and group B sees a redesigned button. Then we compare the click-through rate or conversion rate. This is different from performance or functional testing, because A/B testing is not mainly about correctness or speed. It is about which version performs better for real users.

9. End-to-End Testing
End-to-End testing verifies a complete workflow from start to finish. It is mainly performed by QA engineers and ensures end-to-end functionality works as expected.
For example, a customer signs up, logs in, adds a product to the cart, pays, and then checks order history. That full journey is an end-to-end test. Compared with integration testing, E2E testing is wider because it checks the entire user flow instead of just the interaction between a few modules.

10. User Acceptance Testing (UAT)
UAT happens when the end user tests the software under real or near-real conditions to confirm that it can perform required tasks. The goal is to ensure the software meets user needs and expectations.
For example, an operations team may use the system to create reports or approve requests before the company officially accepts the product. Compared with E2E testing, UAT is more about business acceptance. E2E says the workflow technically works, while UAT says the real users are satisfied with it.

Quick comparison of the main testing types
Unit testing is the smallest and most isolated type. It checks one method or one class and usually uses mocks. Integration testing is larger because it verifies that multiple modules work together, usually with real dependencies. Functional testing checks whether a feature meets the business requirement. End-to-End testing checks the whole workflow across the system. UAT is the final confirmation from real users that the software is good enough to use in practice. Unit tests rely on mocking, while integration tests run real methods and do not use Mockito.
Regression testing and smoke testing are both often seen in CI/CD, but they are not the same. Smoke testing is quick and basic, while regression testing is broader and is used to ensure existing functionality still works after a change. Performance testing and stress testing are also related, but performance focuses on normal target workloads, while stress testing pushes the system beyond normal conditions to find its limit.

Environment related:
1. Development
The development environment is where developers write code, test new features, and validate their own changes first. Temporary development environments for an individual code branch.
For example, my local machine or a personal dev server is a development environment. This is where I do coding, debugging, and unit testing first.

2. QA (Quality Assurance)
QA environment is mainly used by QA engineers to verify functionality, run test cases, and do integration, regression, or end-to-end testing before release. QA is one of the standard software development environments, and it also notes that QA usually works in non-production or staging-like environments.
For example, after a developer finishes a feature, QA may validate login, order creation, and edge cases in the QA environment.

3. Pre-prod / Staging
Staging, or pre-production, is the environment that is closest to production. Staging is “ready for production.”
For example, before going live, the team may deploy the release candidate to staging and run final smoke tests and regression tests there. Compared with QA, staging is usually more stable and more production-like.

4. Production
Production is the real-life environment used by actual users. This is where the final version of the software runs. Production is the last environment in the software development flow.
For example, the public e-commerce website that real customers visit and use is the production environment. Compared with all other environments, production changes are the most carefully controlled because real users are affected.

Quick comparison of environments
Development is mainly for developers to build and test their own code. QA is for testing and validation. Staging is the final rehearsal environment before release and should be very close to production. Production is the actual live environment for real users. A common flow is: developers finish code in development, QA validates it in QA, the team verifies release readiness in staging, and then the software is deployed to production.
