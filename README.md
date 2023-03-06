
# Product EAN

This is a RESTful API controller that provides endpoints for creating, updating, deleting, and fetching products by product code and EAN code.

## Prerequisites

- Java 17
- MySQL 8.0.32

## Installation

1. Clone the repository to your local machine
2. Navigate to the project directory
3. Run the following command to build the application:

```bash
mvn clean package
```

## Usage

1. Create database with name product_ean

```mysql
CREATE DATABASE product_ean;
```

2. Create user 'spring' with password 'spring'

```mysql
CREATE USER 'spring'@'localhost' IDENTIFIED BY 'spring';
```

3. Run the application using the following command:

```bash
java -jar target/product-controller-1.0-SNAPSHOT.jar
```

4. The application should start on http://localhost:8080

## Endpoints

### Get Product EAN

Returns the product with the given ID

`GET /product/ean/{id}`

Path Parameter

| Name | Type | Description                           |
|------|------|---------------------------------------|
| id   | Long | The ID of the product EAN to retrieve |

Responses

| Status Code | Description                            | 	Response Body                        |
|-------------|----------------------------------------|---------------------------------------|
| 200         | The product was retrieved successfully | Object containing the product details |
| 401         | Unauthorized                           | Credentials not valid                 |
| 404         | The product was not found              | Object containing an error            |

### Find Product EAN

Returns the product with the given details

`GET /product/ean/find`

Parameters

| Name            | Type   | Description                                                        |
|-----------------|--------|--------------------------------------------------------------------|
| productCode     | String | The product code to search for. Must be a 5-digit number.          |
| destinationCode | String | The destination code to search for. Must be a single digit number. |
| providerCode    | String | The provider code to search for. Must be a 7-digit number.         |

Responses

| Status Code | Description                            | 	Response Body                        |
|-------------|----------------------------------------|---------------------------------------|
| 200         | The product was retrieved successfully | Object containing the product details |
| 400         | Bad parameters                         | Object containing an error            |
| 401         | Unauthorized                           | Credentials not valid                 |
| 404         | The product was not found              | Object containing an error            |

### Store Product EAN

Creates a new product with the provided EAN code

`PUT /product/ean/find`

Request Body

| Field   | Type   | Required | Description                           |
|---------|--------|----------|---------------------------------------|
| eanCode | String | Yes      | The EAN code of the product to create |

Responses

| Status Code | Description                    | 	Response Body                               |
|-------------|--------------------------------|----------------------------------------------|
| 201         | The product was created        | Object containing the stored product details |
| 400         | The product EAN is not valid   | Object containing an error                   |
| 401         | Unauthorized                   | Credentials not valid                        |
| 409         | The product EAN already exists | Object containing an error                   |

### Update Product EAN

`POST /product/ean/find/{id}`

Updates a product with the provided EAN code

Path Parameter

| Name | Type | Descritpion                         |
|------|------|-------------------------------------|
| id   | Long | The ID of the product EAN to update |

Request Body

| Field   | Type   | Required | Description                           |
|---------|--------|----------|---------------------------------------|
| eanCode | String | Yes      | The EAN code of the product to create |

Responses

| Status Code | Description                    | 	Response Body                                |
|-------------|--------------------------------|-----------------------------------------------|
| 200         | The product was updated        | Object containing the updated product details |
| 400         | The product EAN is not valid   | Object containing an error                    |
| 401         | Unauthorized                   | Credentials not valid                         |
| 409         | The product EAN already exists | Object containing an error                    |


### Delete Product EAN

Delete a product

`DELETE /product/ean/{id}`

Path Parameter

| Name | Type | Description                         |
|------|------|-------------------------------------|
| id   | Long | The ID of the product EAN to delete |

Responses

| Status Code | Description     | 	Response Body        |
|-------------|-----------------|-----------------------|
| 401         | Unauthorized    | Credentials not valid |
| 501         | Not implemented | null                  |

## Postman

Postman collection available under ```/src/main/resources/postman```

### Authorization

Requests must be sent with Authorization Bearer token.

Setting up must be done in Authorization Tab

Steps:

1. Make sure the following collection variables are set:

| VARIABLE            | INITIAL VALUE                                                     | 	CURRENT VALUE                                                    |
|---------------------|-------------------------------------------------------------------|-------------------------------------------------------------------|
| cognitoClientId     | 26bphp547ks2vo94aim734aa22                                        | 26bphp547ks2vo94aim734aa22                                        |
| authUrl             | https://product-ean.auth.eu-west-3.amazoncognito.com/login        | https://product-ean.auth.eu-west-3.amazoncognito.com/login        |
| accessTokenEndpoint | https://product-ean.auth.eu-west-3.amazoncognito.com/oauth2/token | https://product-ean.auth.eu-west-3.amazoncognito.com/oauth2/token |
| scope               | openid profile                                                    | openid profile                                                    |
| callbackUrl         | https://oauth.pstmn.io/v1/callback                                | https://oauth.pstmn.io/v1/callback                                |

2. In tab 'Authorization', set Type OAuth 2.0, Add auth data to Request Headers.

3. Configure New Token with configuration options:

| Option                | Value                     |
|-----------------------|---------------------------|
| Token name            | any                       |
| Grant Type            | implicit                  |
| Callback URL          | {{callbackUrl}}           |
| Auth URL              | {{authUrl}}               |
| Client ID             | {{cognitoClientId}}       |
| Scope                 | {{scope}}                 |
| Client Authentication | Send as Basic Auth header |

4. Press Get New Access Token and Login with Google, Sign in with new user and password, or Log in using user 'test' password 'test321'