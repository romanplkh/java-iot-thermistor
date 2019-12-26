import React, { Component } from "react";
import "./App.css";
import CanvasJSReact from "./assets/canvasjs.react";
import { client } from "./mqttClient/mqttClient.utils";

const CanvasJSChart = CanvasJSReact.CanvasJSChart;

class App extends Component {
  constructor() {
    super();

    this.state = {
      data: [
        {
          x: new Date(),
          y: 100
        }
      ],
      dynamic: false
    };
  }

  componentDidMount() {
    client.onMessageArrived = message => {
      let dataPoints = [...this.state.data];

      if (this.state.dynamic) {
        if (this.state.data.length > 10) {
          dataPoints.shift();
        }
      }

      this.setState({
        data: [
          ...dataPoints,
          {
            x: new Date(),
            y: +message.payloadString
          }
        ]
      });
    };
  }

  clickHandler = () => {
    if (client.isConnected()) return;
    client.connect({
      onSuccess: () => {
        client.subscribe("lightdata");
      }
    });
  };

  changeModeHandler = () => {
    this.setState((prevSate, props) => ({
      dynamic: !prevSate.dynamic
    }));
  };

  diconnectHandler = () => {
    if (!client.isConnected()) return;
    client.disconnect();
  };

  render() {
    const options = {
      theme: "dark2",
      title: {
        text: "Light Intensity"
      },
      axisY: {
        title: "Light Decreasing Resistance",
        includeZero: false,
        suffix: ""
      },
      axisX: {
        title: "Time",
        valueFormatString: "HH:mm:ss"
      },
      data: [
        {
          type: "line",
          dataPoints: this.state.data
        }
      ]
    };

    return (
      <div className="App">
        <header className="App-header">
          <button onClick={this.clickHandler}>Connect</button>
          <button onClick={this.changeModeHandler}>Change Data Mode</button>
          <button onClick={this.diconnectHandler}>Disconnect</button>
        </header>
        <div>
          <CanvasJSChart
            options={options}
            /* onRef={ref => this.chart = ref} */
          />
        </div>
      </div>
    );
  }
}

export default App;
