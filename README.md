## JCipher, a very simple AES implementation

1. Supports AES by default

2. Easy integration with Spring

## REQUIREMENTS

1. Java 1.8

2. Maven

## HOW TO INSTALL

1. Place this in pom.xml

```xml
<repositories>
		<repository>
			<id>JCipher-mvn-repo</id>
			<url>https://raw.github.com/juanroldanbrz/JCipher/mvn-repo/</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
		...
</repositories>
	
<dependencies>
		<dependency>
			<groupId>com.yamajun.jcypher</groupId>
			<artifactId>jcypher</artifactId>
			<version>1.1</version>
		</dependency>
		
		...
</dependencies>

```
## HOW TO USE

### Declaring class

1.1 In Spring with Java config

```java
@Bean
public JCipher testJCipher() throws JCipherInvalidKey {
    return new JCipher("yourKey");
}
```

1.2 A more standard way

```java
JCipher cipher = new JCipher("MyKey");
```

### Using class

1. We can encrypt / decrypt a plan string with:

```java
cipher.encryptString("foo");
cipher.decryptString("encryptedFoo");
```

2. We can encrypt a full object with java annotations

```java
@Ciphered
class SampleObj {

    @CipherMe
    private String numberToCipher = "1234";

    private String numberToNOTCipher = INITIAL_NUMBER;
    //..
}

class test{
    public static void main(String[] args){
        SampleObj sampleObj = new SampleObj();
        cypher.encryptObject(sampleObj);
        //Now the params with @CipherMe are encrypted
        cypher.decryptObject(sampleObj);
    }
}

```

## TODO

1. Enable custom configuration.

2. Add more exceptions when trying to encrypt a non-string variable.

3. Integrate with hibernate