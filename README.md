# WeatherMeasurements

This is a simulation of sending and receiving weather measurements.

There is a weather sensor that measures the temperature of the ambient air and determines if it is raining.
Our application accepts data in JSON format to register a new sensor and record new measurements and can send all the measurements taken and the number of rainy days in JSON format.

You can use Postman or the SensorProjectRESTApiClient project to send and receive data, where a simulation of sending and receiving data with a graph of temperatures is implemented.

Registering a new sensor:

![Снимок](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/73ac998c-1b72-433f-9f28-f385196a6b60)
![registerSensorResponse](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/53b8131a-2681-47cd-9a97-48bb86190b98)

Sensor name validation:

![nameValidation](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/baff6937-3dad-43af-a635-71e644f99701)

Adding a measurement:

![addMeasurement](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/84b0aab9-b84a-46fc-b24b-98c2c3d10132)
![addmeasurementresponse](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/6c28637c-66b8-42cd-8b28-8777b9b6d877)

Attempting to add a measurement of an unregistered sensor:

![validation](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/16442c26-47eb-4b54-b17f-f726ce9fc531)

Receiving measurements:

![measurements](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/027c1d30-ca88-4e19-8640-47a1b5fc02c3)

Number of rainy days:

![rainyDaysCount](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/3fceb753-8b89-488c-bd78-40298d295368)

Getting data on the client and getting a graph of temperatures:

![graph](https://github.com/DEUS-VULT-1095/SensorProjectRESTApi/assets/109753552/16975e19-017f-426c-ac15-6d62cf22c0eb)

