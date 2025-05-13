plugins {
	java
	id("org.springframework.boot") version "3.3.11"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "cl"
version = "0.0.1-SNAPSHOT"

// USO la version java 17 coretto ya que es la única o al menos la que sé para poder dockerizar las builds de java que estoy haciendo desde la v. de spring
java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("io.jsonwebtoken:jjwt-api:0.11.2")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.2")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.2")
}
// no usare Junit

