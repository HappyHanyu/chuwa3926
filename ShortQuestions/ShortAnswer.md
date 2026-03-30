Q1. Explain Referential Integrity in RDBMS/SQL with sample queries.
In an RDBMS, referential integrity means relationships between tables stay valid. If one table refers to another, the referenced row must exist, or the relationship must be set to NULL if the schema allows it.
A common example is student and department. If student.dept_id points to department.dept_id, then every non-null dept_id in student must match a real row in department. This is usually enforced with a foreign key.
Example: 
CREATE TABLE department (
    dept_id INT PRIMARY KEY,
    DEPT_NAME VARCHAR(100) NOT NULL
);
CREATE TABLE student (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    dept_id INT,
    CONSTRAINT fk_student_department
        FOREIGN KEY (dept_id)
        REFERENCES department(dept_id)
);
Here, department.dept_id is the parent key, and student.dept_id is the foreign key. Because of referential integrity, this works:
INSERT INTO department (dept_id, dept_name)
VALUES (101, 'Computer Science');
INSERT INTO student (student_id, student_name, dept_id)
VALUES (1, 'Alice', 101);
But this usually fails, because department 999 does not exist:
INSERT INTO student (student_id, student_name, dept_id)
VALUES (2, 'Bob', 999);
It also affects deletes and updates. For example, if students already reference department 101, this delete may fail:
DELETE FROM department
WHERE dept_id = 101;
The database blocks it because removing that department would leave student rows pointing to nothing. Depending on how the foreign key is defined, you can choose different behaviors. For example:
CREATE TABLE student (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    dept_id INT,
    CONSTRAINT fk_student_department
        FOREIGN KEY (dept_id)
        REFERENCES department(dept_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
With ON DELETE SET NULL, deleting a department sets related student.dept_id values to NULL. With ON UPDATE CASCADE, if the parent key changes, child rows update automatically.
=====================================================================
Q2. Explain Join in RDBMS/SQL with sample queries.
For JOIN. A join combines rows from two or more tables based on related columns. It is how you query data spread across normalized tables.
Suppose we have these tables:
CREATE TABLE department (
    dept_id INT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL
);
CREATE TABLE student (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id)
);
And sample data:
INSERT INTO department VALUES
(101, 'Computer Science'),
(102, 'Mathematics'),
(103, 'Physics');
INSERT INTO student VALUES
(1, 'Alice', 101),
(2, 'Bob', 102),
(3, 'Carol', NULL),
(4, 'David', 101);
An INNER JOIN returns only rows that match on both sides:
SELECT s.student_name, d.dept_name
FROM student s
INNER JOIN department d
    ON s.dept_id = d.dept_id;
Result:
Alice   Computer Science
Bob     Mathematics
David   Computer Science
Carol is not included because her dept_id is NULL, so there is no match.
A LEFT JOIN keeps all rows from the left table, even if there is no match on the right:
SELECT s.student_name, d.dept_name
FROM student s
LEFT JOIN department d
    ON s.dept_id = d.dept_id;
Result:
Alice   Computer Science
Bob     Mathematics
Carol   NULL
David   Computer Science
A RIGHT JOIN keeps all rows from the right table:
SELECT s.student_name, d.dept_name
FROM student s
RIGHT JOIN department d
    ON s.dept_id = d.dept_id;
If a department has no students, it still appears, with student columns as NULL.
A FULL OUTER JOIN keeps all rows from both sides. Not every database supports it directly, but where supported it looks like this:
SELECT s.student_name, d.dept_name
FROM student s
FULL OUTER JOIN department d
    ON s.dept_id = d.dept_id;
There is also CROSS JOIN, which returns the Cartesian product:
SELECT s.student_name, d.dept_name
FROM student s
CROSS JOIN department d;
If there are 4 students and 3 departments, this returns 12 rows. Usually this is not what you want unless you intentionally need every combination.
You can also join more than two tables. For example, if department belongs to a school:
CREATE TABLE school (
    school_id INT PRIMARY KEY,
    school_name VARCHAR(100) NOT NULL
);
ALTER TABLE department
ADD school_id INT,
ADD CONSTRAINT fk_department_school
    FOREIGN KEY (school_id)
    REFERENCES school(school_id);
Then:
SELECT s.student_name, d.dept_name, sc.school_name
FROM student s
JOIN department d
    ON s.dept_id = d.dept_id
JOIN school sc
    ON d.school_id = sc.school_id;
This lets you pull related data across several tables in one query.
So in simple terms, referential integrity is about keeping table relationships valid during insert, update, and delete operations, while joins are about reading related data from multiple tables together. Referential integrity protects correctness; joins help you query that related data efficiently.

-------------------------------------------------------------------

Q1. Compare Developer API vs User API
A Developer API is mainly designed for programmers or backend systems to integrate one application with another. It is usually consumed by code, not directly by end users. For example, a payment API, maps API, or weather API lets a developer send requests and receive structured data like JSON. It focuses on things like endpoints, authentication, rate limits, request/response formats, and SDK support.

A User API is not a standard formal term in the same way, but in many interview or system contexts it usually means an interface intended more directly for end-user actions or for frontend apps acting on behalf of a user. In other words, it is closer to “user-facing API.” For example, an app may call /login, /profile, or /orders for a signed-in user. The big difference is that developer APIs are built for integration by external or internal developers, while user-facing APIs are built around product features and user workflows. You can also say: developer API is about enabling developers, while user API is about serving end-user functionality.
=====================================================================
Q2. Explain components of REST API?
A REST API usually has several core components. First is the resource, which is the object being operated on, such as users, orders, or products. These resources are identified by URLs, for example /users/123. Second is the HTTP method, which tells the server what action is requested, such as GET to read data, POST to create, PUT or PATCH to update, and DELETE to remove. Third is the request, which may contain headers, query parameters, path variables, and sometimes a body. Fourth is the response, which includes a status code, headers, and usually a body in JSON format. Fifth is the status code, such as 200 OK, 201 Created, 400 Bad Request, or 404 Not Found, which tells the client the result of the operation. A REST API also often relies on statelessness, meaning each request should contain everything needed for the server to process it, without depending on remembered session state on the server side.
=====================================================================
Q3. Compare each type of HTTP Method
The most common HTTP methods are GET, POST, PUT, PATCH, DELETE, HEAD, and OPTIONS.

GET is used to retrieve data from the server. It should not change server state. For example, GET /users/1 fetches user data. It is considered safe and idempotent, meaning repeated identical requests should produce the same effect on the server.

POST is usually used to create a new resource or trigger some server-side processing. For example, POST /users with a JSON body may create a new user. POST is generally not idempotent, because sending the same request twice may create two records.

PUT is used to replace a resource completely. For example, PUT /users/1 may overwrite the user’s full information. It is idempotent, because repeating the same PUT should still leave the resource in the same final state.

PATCH is used for partial updates. For example, PATCH /users/1 with just an email field changes only that field. It is often used when you do not want to send the entire object.

DELETE removes a resource. For example, DELETE /users/1 deletes that user. It is generally idempotent, because deleting the same resource multiple times should not create extra side effects beyond the first delete.

HEAD is like GET but only returns headers, not the response body. It is useful for checking whether a resource exists or for reading metadata such as content type or content length.

OPTIONS tells the client what methods or capabilities are supported by the server for a given resource. It is often used in CORS preflight requests by browsers.

You can summarize them like this: GET reads, POST creates, PUT replaces, PATCH partially updates, DELETE removes, HEAD checks metadata, and OPTIONS asks what is allowed.
=====================================================================
Q4. Explain authentication field in http header?
There is not one universal header literally called “authentication,” but the most common header used for authentication is the Authorization header. Its purpose is to send credentials or proof of identity from client to server. A common example is:
Authorization: Bearer <token>
This is widely used in token-based authentication, such as JWT or OAuth. Another example is Basic authentication:
Authorization: Basic <base64(username:password)>
The server reads this header to determine who the caller is and whether they are allowed to access the requested resource. This is important because without authentication, the server cannot safely protect private data or restricted operations. In short, the authentication-related header tells the server, “this is who I am” or “this is my access credential.”
=====================================================================
Q5. Explain cookies field in http header?
The cookie-related headers are mainly Cookie and Set-Cookie.
The Cookie request header is sent by the client to the server. It contains stored cookie values, such as session IDs or user preferences. Example:
Cookie: sessionId=abc123; theme=dark

The Set-Cookie response header is sent by the server to the client to ask the browser to store a cookie. Example:
Set-Cookie: sessionId=abc123; HttpOnly; Secure; Path=/

Cookies are commonly used for session management, login state, personalization, and tracking. For example, after login, the server may send a session cookie, and the browser automatically includes it in later requests so the server knows which user is making the request. So the main idea is: Set-Cookie creates or updates a cookie on the client side, and Cookie sends stored cookies back to the server.
=====================================================================
Q6. Explain the purpose of http response headers? Why it is necessary?
HTTP response headers carry important metadata about the server’s response. They are necessary because the response body alone is not enough for the client to correctly interpret, cache, secure, or manage the data. Response headers tell the client things like what the content type is, how to handle caching, whether the response can be compressed, whether cookies should be stored, and whether the request succeeded under certain conditions.

For example, Content-Type: application/json tells the client how to parse the response body. Content-Length tells the size of the body. Cache-Control tells the browser or proxy whether the response may be cached. Set-Cookie tells the browser to store a cookie. Location may tell the client where a newly created resource is. Security-related headers like Strict-Transport-Security or X-Content-Type-Options help enforce safe browser behavior.

So response headers are necessary because they provide the rules and context for how the client should treat the response. Without them, the client would have much less information about format, caching, authentication state, redirection, encoding, and security behavior.

