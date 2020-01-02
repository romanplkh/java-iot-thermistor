import React, { useState, useEffect } from "react";
import { client } from "../../mqttClient/mqttClient.utils";
import CanvasJSReact from "../../assets/canvasjs.react";
const CanvasJSChart = CanvasJSReact.CanvasJSChart;

const Chart = ({ dynamic }) => {
  const [coords, setData] = useState([
    {
      x: new Date(),
      y: 50,
      markerColor: "green"
    }
  ]);

  useEffect(() => {
    client.onMessageArrived = message => {
      console.log(message.payloadString);

      let coordsToSet = [...coords];

      if (dynamic) {
        if (coords.length > 10) {
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

      setData([
        ...coordsToSet,
        {
          x: new Date(),
          y: +message.payloadString,
          markerColor: markerColor
        }
      ]);
    };
  }, [coords, dynamic]);

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
        dataPoints: coords
      }
    ]
  };
  return <CanvasJSChart options={options} />;
};

export default Chart;
