# Project README

## Project Description
This project focuses on creating a REST API service for generating QR codes. The API provides two endpoints: 
1. **GET /api/health**: Responds with a status code of `200 OK` to verify the service's operation and availability.
2. **GET /api/qrcode**: Allows clients to retrieve QR code images. This endpoint is fully implemented and functional.

## Technologies Used
- Java
- Spring Boot
- Google ZXing library for QR code generation
- Jackson library for JSON processing

## Project Structure
The project consists of the following components:
- **QrCodeController**: Manages the REST API endpoints for health check and QR code generation.
- **Application**: Contains the main method to run the Spring Boot application.
- **BufferedImageHttpMessageConverter**: Custom HTTP message converter for BufferedImage responses.

## How to Run
To run the project:
1. Ensure you have Java and Maven installed.
2. Clone the project repository.
3. Navigate to the project directory.
4. Run `mvn spring-boot:run` to start the Spring Boot application.

## API Endpoints
- **GET /api/health**: Check the service health status.
- **GET /api/qrcode**: Retrieve QR code images.

## Future Enhancements
- Add error handling and validation for user inputs.
- Improve documentation and add unit tests for better reliability.


Feel free to contribute, report issues, or provide feedback!
