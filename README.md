# CMS
### Content Management Service

1. 사용자의 그룹 및 권한에 따른 컨텐츠 관리 서비스
    - User
    - Group
        - 사용자는 여러 그룹에 소속될 수 있음.
    - Role
        - 관리자 / 광고주 / 출판사 / 일반 사용자
        - 사용자는 각 그룹마다 여러 역활을 담당할 수 있음.  
    - Permission
        - 읽기 / 쓰기 권한
        - 사용자별 / 그룹별 권한 

2. 결재(Approval Path)
    - 초안 / 결재 / 상신
    
---

### 실행

```sh
./mvnw spring-boot:run
```

---

application.properties

    # database
    spring.datasource.url=jdbc:mysql://localhost/content_db?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    
    spring.datasource.username=user
    spring.datasource.password=password
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    
    # Specify the DBMS
    spring.jpa.database=MYSQL
    spring.jpa.properties.hibernate.connection.CharSet=utf-8
    spring.jpa.properties.hibernate.connection.characterEncoding=utf-8
    spring.jpa.properties.hibernate.connection.useUnicode=true
    
    # Hibernate settings
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect