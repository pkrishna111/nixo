# NIXO — News Sharing Platform (JSP/Servlet/JPA/Payara)

NIXO is a news sharing + social app built with **Jakarta EE (JSP, Servlets, JPA, EJB)**, **Payara 6**, **MySQL (XAMPP)** and **Maven**.

## 1) Prerequisites (new PC)

Install these:

- **Git**: https://git-scm.com/downloads  
- **JDK 17** (required for Payara 6)
- **Maven 3.8+**: `mvn -v` should work in terminal
- **XAMPP** (MySQL/MariaDB running on port 3306)
- **Payara 6**: https://www.payara.fish/downloads/payara-platform-community/
- **MySQL Connector/J** (JDBC driver): https://dev.mysql.com/downloads/connector/j/

## 2) Clone the repo

```bash
git clone https://github.com/<YOUR_USERNAME>/nixo.git
cd nixo
```
3) Database setup

Create the database and import the schema/data.

Option A) Import from the provided SQL

If docs/db/nixo_db.sql is present:

Open phpMyAdmin → create DB nixo_db

Click Import → select docs/db/nixo_db.sql → Go

4) Payara — add MySQL JDBC driver

Copy mysql-connector-j-8.x.x.jar to:

PAYARA_HOME/glassfish/domains/domain1/lib/ext/

Restart Payara after placing the jar.

5) Payara — create JDBC pool and resource
-> Admin Console (UI)

Open http://localhost:4848

Resources → JDBC → JDBC Connection Pools → New

Pool Name: nixoPool

Resource Type: javax.sql.DataSource

Database Vendor: MySQL

Next → set:

Datasource Classname: com.mysql.cj.jdbc.MysqlDataSource

Properties:

serverName = localhost

portNumber = 3306

databaseName = nixo_db

user = root

password = (blank or your password)

useSSL = false

allowPublicKeyRetrieval = true

serverTimezone = UTC

Save → Ping (should be Success).

Resources → JDBC → JDBC Resources → New

JNDI Name: jdbc/nixo_pool ← must match persistence.xml

Pool Name: nixoPool

Save.

6) Build & deploy
Deploy the generated WAR:

Payara Admin → Applications → Deploy

Choose: target/nixo.war

Context Root: nixo (lowercase)

Deploy

Open:

http://localhost:8080/nixo/feed

7) Default URLs (after login/register)

Feed: /feed

Auth: /login, /register, /logout

News: /NewsServlet?action=create|edit|delete|like|search

Sharer request:

Receiver form: /SharerRequestServlet?action=applyForm

Admin list: /SharerRequestServlet?action=list
