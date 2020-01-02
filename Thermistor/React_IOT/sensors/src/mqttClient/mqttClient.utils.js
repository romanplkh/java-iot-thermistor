import { Client } from "paho-mqtt";

export const client = new Client("127.0.0.1", Number("8083"), "reactClient");
