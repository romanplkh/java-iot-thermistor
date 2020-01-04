import React from "react";
import "./header.css";

export const Header = () => {
  const scrollHandler = () => {
    window.scrollTo({
      top: document.body.clientHeight - 550,
      left: 0,
      behavior: "smooth"
    });
  };
  return (
    <div>
      <header className="header">
        <div className="overlay">
          <div className="linear-pic"></div>
          <div className="header_title">
            <p>
              REAL-TIME <span className="cta">GREENHOUSE</span> TEMPERATURE DATA
              AND ANALYTICS
            </p>
          </div>
          <h3 className="header_subtitle">SMART CROPS GROWING </h3>
          <button className="btn btn-try" onClick={scrollHandler}>
            START MONITORING
          </button>
        </div>
      </header>
      <section className="features">
        <div className="feature-item">
          <i className="fa fa-gem "></i>
          <p>High Accuracy</p>
        </div>
        <div className="feature-item">
          <i className="fas fa-broadcast-tower"></i>
          <p>Great Scalability</p>
        </div>
        <div className="feature-item">
          <i className="fas fa-seedling"></i>
          <p>Outstanding resutls</p>
        </div>
        <div className="feature-item">
          <i className="fas fa-tv"></i>
          <p>Smart View</p>
        </div>
      </section>
    </div>
  );
};
