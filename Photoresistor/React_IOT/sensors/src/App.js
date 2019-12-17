import React, { Component } from "react";
import logo from "./logo.svg";
import "./App.css";
import CanvasJSReact from "./assets/canvasjs.react";

const CanvasJS = CanvasJSReact.CanvasJS;
const CanvasJSChart = CanvasJSReact.CanvasJSChart;

class App extends Component {
  state = {
    data: [{ x: 1, y: 13 }],
    increment: 1,
    dynamic: false
  };

  options = {};

  clickHandler = () => {
    const socket = new WebSocket("ws://127.0.0.1:8000");

    socket.onopen = () => {
      socket.send("hello from client\n");
    };

    socket.onmessage = msg => {
      console.log(msg.data);

      if (this.state.dynamic) {
        if (this.state.data.length > 10) {
          let oldData = this.state.data.slice();

          oldData.shift();

          this.setState({
            data: oldData
          });
        }
      }

      this.setState((prevState, props) => ({
        increment: prevState.increment + 1,
        data: [...prevState.data, { x: prevState.increment, y: +msg.data }]
      }));
    };
  };

  changeModeHandler = () => {
    this.setState((prevSate, props) => ({
      dynamic: !prevSate.dynamic
    }));
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
        prefix: "s"
        //interval: Date.now().toLocaleString()
      },
      data: [
        {
          type: "line",
          dataPoints: this.state.data
        }
      ]
    };
    // let options = {
    //   animationEnabled: true,
    //   exportEnabled: true,
    //   theme: "light2", // "light1", "dark1", "dark2"
    //   title: {
    //     text: "Bounce Rate by Week of Year"
    //   },
    //   axisY: {
    //     title: "Bounce Rate",
    //     includeZero: false,
    //     suffix: "%"
    //   },
    //   axisX: {
    //     title: "Week of Year",
    //     prefix: "s",
    //     interval: 1
    //   },
    //   data: [
    //     {
    //       type: "line",
    //       toolTipContent: "Week {x}: {y}%",
    //       dataPoints: [...this.state.data]
    //     }
    //   ]
    // };
    return (
      <div className="App">
        <header className="App-header">
          <button onClick={this.clickHandler}>Connect</button>
          <button onClick={this.changeModeHandler}>Change Data Mode</button>
        </header>
        <div>
          <CanvasJSChart
            options={options}
            /* onRef={ref => this.chart = ref} */
          />
          {/*You can get reference to the chart instance as shown above using onRef. This allows you to access all chart properties and methods*/}
        </div>
      </div>
    );
  }
}

export default App;
