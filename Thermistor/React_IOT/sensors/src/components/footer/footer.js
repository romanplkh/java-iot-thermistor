import React from "react";
import "./footer.css";

export const Footer = () => {
  return (
    <div className="footer">
      <div className="footer-container">
        <p>&copy; Roman Pelikh, {new Date().getFullYear()}</p>
        <a
          href="https://github.com/romanplkh/IOT-reactjs-java-arduino"
          target="_blank"
          rel="noreferrer noopener"
          className="link"
        >
          GitHub <i className="fas fa-sign-in-alt"></i>
        </a>
      </div>
    </div>
  );
};
