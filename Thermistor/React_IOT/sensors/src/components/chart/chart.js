import React, { useState, useEffect } from "react";
import CanvasJSReact from "../../assets/canvasjs.react";
const CanvasJSChart = CanvasJSReact.CanvasJSChart;

const Chart = ({ dynamic, client }) => {
  const [chartData, setChartData] = useState([
    {
      x: new Date(),
      y: 50,
      markerColor: "green"
    }
  ]);

  useEffect(() => {
    if (client != null) {
      client.onMessageArrived = message => {
        let coordsToSet = [...chartData];

        if (dynamic) {
          if (chartData.length > 10) {
            coordsToSet.shift();
          }
        }

        let markerColor = "green";

        if (+message.payloadString >= 25) {
          markerColor = "orange";
        }

        if (+message.payloadString >= 30) {
          markerColor = "red";
        }

        setChartData([
          ...coordsToSet,
          {
            x: new Date(),
            y: +message.payloadString,
            markerColor: markerColor
          }
        ]);
      };
    }
  }, [chartData, dynamic, client]);

  const options = {
    theme: "light",
    title: {
      text: "Temperature Reading"
    },
    axisY: {
      title: "Temperature",
      includeZero: true,
      suffix: "°C"
    },
    axisX: {
      title: "Current Time",
      valueFormatString: "HH:mm:ss",
      interval: 10
    },
    data: [
      {
        type: "line",
        name: "time",
        lineColor: "black",
        dataPoints: chartData
      }
    ]
  };

  return <CanvasJSChart options={options} />;
};

export default Chart;
