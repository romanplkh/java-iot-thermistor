import React, { Component } from "react";
import "./App.css";
import { Client } from "paho-mqtt";
import Chart from "./components/chart/chart";
import { Header } from "./components/header/header";
import { Footer } from "./components/footer/footer";
import { Spinner } from "./components/spinner/spinner";
import ErrorBoundary from "./components/error-boundary/error-boundary";

let mqttClient = null;

class App extends Component {
  constructor() {
    super();

    this.state = {
      dynamic: false,
      connecting: false
    };
  }

  componentDidMount() {
    mqttClient = new Client("127.0.0.1", Number("8083"), "reactClient");
  }

  connectClickHandler = () => {
    this.setState({ connecting: true });

    try {
      if (mqttClient.isConnected()) {
        alert("Client already connected");
        this.setState({ connecting: false });
        return;
      }

      mqttClient.connect({
        timeout: 30,
        onSuccess: () => {
          mqttClient.subscribe("temperatureData");
          this.setState({ connecting: false });
        },
        onFailure: err => {
          if (err.errorCode === 7) {
            alert("MQTT Broker is not available. Read GitHub documentation");
            this.setState({ connecting: false });
          }
        }
      });
    } catch (error) {
      this.setState({ connecting: false });
    }
  };

  changeModeHandler = () => {
    this.setState((prevSate, props) => ({
      dynamic: !prevSate.dynamic
    }));
  };

  disconnectClickHandler = () => {
    if (!mqttClient.isConnected()) return;
    mqttClient.disconnect();
  };

  render() {
    const { dynamic, connecting } = this.state;
    return (
      <div>
        <Header scrollHandler={this.scrollHandler} />
        {connecting && <Spinner />}
        <div className="chart-container">
          <ErrorBoundary>
            <Chart dynamic={dynamic} client={mqttClient} />
            <div className="btn-controls-group">
              <button
                onClick={this.connectClickHandler}
                className="btn btn-control btn-connect"
                title="Connect to MQTT Broker"
              >
                <i className="fas fa-plug"></i>
                Connect
              </button>
              <button
                onClick={this.changeModeHandler}
                className="btn btn-control  btn-mode"
                title="Toggles chart line representation"
              >
                <i className="fas fa-cogs"></i> Change View Mode
              </button>
              <button
                onClick={this.disconnectClickHandler}
                className="btn btn-control btn-disconnect "
                title="Disconnect from MQTT Broker"
              >
                <i className="fas fa-ban"></i> Disconnect
              </button>
            </div>
          </ErrorBoundary>
        </div>
        <Footer />
      </div>
    );
  }
}

export default App;
