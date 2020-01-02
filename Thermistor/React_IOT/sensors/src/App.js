import React, { Component } from "react";
import "./App.css";
import { client } from "./mqttClient/mqttClient.utils";
import Chart from "./components/chart/chart";
import { Header } from "./components/header/header";

class App extends Component {
  constructor() {
    super();

    this.state = {
      dynamic: false
    };
  }

  connectClickHandler = () => {
    if (client.isConnected()) {
      console.log("Client already connected");
      return;
    }

    client.connect({
      onSuccess: () => {
        client.subscribe("temperatureData");
      },
      onFailure: err => {
        console.log(err);
      }
    });
  };

  changeModeHandler = () => {
    this.setState((prevSate, props) => ({
      dynamic: !prevSate.dynamic
    }));
  };

  diconnectClickHandler = () => {
    if (!client.isConnected()) return;
    client.disconnect();
  };

  render() {
    return (
      <div>
        <Header />

        <div className="chart-container">
          <Chart dynamic={this.state.dynamic} />
          <div className="btn-controls-group">
            <button
              onClick={this.connectClickHandler}
              className="btn btn-control btn-connect"
            >
              <i className="fas fa-plug"></i>
              Connect
            </button>
            <button
              onClick={this.changeModeHandler}
              className="btn btn-control  btn-mode"
            >
              <i className="fas fa-cogs"></i> Change Data Mode
            </button>
            <button
              onClick={this.diconnectClickHandler}
              className="btn btn-control btn-disconnect "
            >
              <i className="fas fa-ban"></i> Disconnect
            </button>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
