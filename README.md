# Celcius-tech (Thermistor) application

Greenhouse live temperature measurement analytics

## Description

IoT Java server application that reads LIVE temperature measurement from a thermistor (temperature sensor), subscribes to a topic on MQTT Broker and publishes LIVE data temperature measurements from an Arduino installation similar to [this image](https://ibb.co/zxcy4P2). In a particular case, the application reads LIVE temperature measurements in a greenhouse and displays analytics in a dynamic chart.

Though this backend application can be also used to display temperature measurements in a dynamic chart, it is also a part of the whole system of greenhouse temperature measurement analytics. The client-side of the application you can find [here](https://github.com/romanplkh/react-iot-thermistor)

Inspired by previous hack-a-thons, an application was created with the intent of learning more about IoT, network programming.

## Installation

Just open the application in your preferred IDE for Java and run it. Dependant libraries are already included in the project.

## Usage

- Fork and clone application
- Since it is server-side of a system, you may want to clone and fork client-side from my other repo [react-iot-thermistor](https://github.com/romanplkh/react-iot-thermistor) with additional instructions

- To be able to read data from MQTT server you will need an instance of MQTT Broker running on your localhost. You may download a version of an open-source MQTT Broker from [here](http://emqtt.io/downloads/)

## Credits:

Thank you [Chris Cusak](https://github.com/chrisecusack) for helping out with installation and configuration instructions for [EMQTT Broker](http://emqtt.io)

## License:

Copyright © by Roman Pelikh. You are 100% allowed to use this application for both personal or commercial use, but NOT to claim it as your own project.
A credit to the original author, Roman Pelikh, is of course highly appreciated!
